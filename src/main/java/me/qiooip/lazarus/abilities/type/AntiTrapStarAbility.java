package me.qiooip.lazarus.abilities.type;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.utils.StringUtils;
import me.qiooip.lazarus.utils.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AntiTrapStarAbility extends AbilityItem implements Listener {

    private final String cooldownName;
    private final Cache<UUID, UUID> playerHits;

    private int delay;
    private int hitCache;

    public AntiTrapStarAbility(ConfigFile config) {
        super(AbilityType.ANTI_TRAP_STAR, "ANTI_TRAP_STAR", config);

        this.cooldownName = "AntiTrapStar";

        this.playerHits = CacheBuilder.newBuilder()
            .expireAfterAccess(this.hitCache, TimeUnit.SECONDS).build();

        this.overrideActivationMessage();
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

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;

        Player target = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();

        this.playerHits.put(target.getUniqueId(), damager.getUniqueId());
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
        UUID targetUuid = this.playerHits.getIfPresent(player.getUniqueId());

        if(targetUuid == null) {
            player.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_ANTI_TRAP_STAR_CANNOT_USE
                .replace("<time>", StringUtils.formatDurationWords(this.hitCache * 1000L)));
            return false;
        }

        Player target = Bukkit.getPlayer(targetUuid);
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
