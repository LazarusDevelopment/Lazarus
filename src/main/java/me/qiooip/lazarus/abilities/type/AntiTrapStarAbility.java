package me.qiooip.lazarus.abilities.type;

import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.utils.StringUtils;
import me.qiooip.lazarus.utils.Tasks;
import me.qiooip.lazarus.utils.cache.CacheEntry;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AntiTrapStarAbility extends AbilityItem implements Listener {

    private final String cooldownName;
    private final Map<UUID, CacheEntry<UUID>> playerHits;

    private int delay;
    private int hitCache;

    public AntiTrapStarAbility(ConfigFile config) {
        super(AbilityType.ANTI_TRAP_STAR, "ANTI_TRAP_STAR", config);

        this.cooldownName = "AntiTrapStar";
        this.playerHits = new HashMap<>();

        this.overrideActivationMessage();
    }

    @Override
    protected void disable() {
        this.playerHits.clear();
    }

    @Override
    protected void loadAdditionalData(ConfigurationSection abilitySection) {
        this.delay = abilitySection.getInt("DELAY");
        this.hitCache = abilitySection.getInt("HIT_CACHE");
    }

    public void sendActivationMessage(Player player, Player target) {
        this.activationMessage.forEach(line -> player.sendMessage(line
            .replace("<abilityName>", this.displayName)
            .replace("<target>", target.getName())
            .replace("<duration>", StringUtils.formatDurationWords(this.delay * 1000L))
            .replace("<cooldown>", StringUtils.formatDurationWords(this.cooldown * 1000L))));
    }

    private CacheEntry<UUID> getHitCache(UUID uuid) {
        return new CacheEntry<>(uuid, System.currentTimeMillis() + (this.hitCache * 1000L));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;

        Player target = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();

        this.playerHits.put(target.getUniqueId(), this.getHitCache(damager.getUniqueId()));
    }

    private void activateAbilityOnTarget(Player player, Player target) {
        target.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_ANTI_TRAP_STAR_TARGET_ACTIVATED
            .replace("<player>", target.getName())
            .replace("<abilityName>", this.displayName)
            .replace("<delay>", StringUtils.formatDurationWords(this.delay * 1000L)));

        String message = Language.ABILITIES_PREFIX + Language.ABILITIES_ANTI_TRAP_STAR_PLAYER_TELEPORTED
            .replace("<player>", target.getName());

        TimerManager.getInstance().getCooldownTimer().activate(player, this.cooldownName,
            this.delay, message, () -> Tasks.sync(() -> {
                player.teleport(target.getLocation());

                target.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_ANTI_TRAP_STAR_TARGET_TELEPORTED
                    .replace("<player>", player.getName()));
            }));

        this.sendActivationMessage(player, target);
    }

    @Override
    protected boolean onItemClick(Player player, PlayerInteractEvent event) {
        CacheEntry<UUID> targetCache = this.playerHits.get(player.getUniqueId());

        if(targetCache == null || targetCache.isExpired()) {
            player.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_ANTI_TRAP_STAR_CANNOT_USE
                .replace("<time>", StringUtils.formatDurationWords(this.hitCache * 1000L)));
            return false;
        }

        Player target = Bukkit.getPlayer(targetCache.getKey());

        if(target == null) {
            player.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_ANTI_TRAP_STAR_CANNOT_USE
                .replace("<time>", StringUtils.formatDurationWords(this.hitCache * 1000L)));
            return false;
        }

        this.activateAbilityOnTarget(player, target);
        event.setCancelled(true);
        return true;
    }
}
