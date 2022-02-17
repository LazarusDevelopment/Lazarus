package me.qiooip.lazarus.abilities.type;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.cooldown.CooldownTimer;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AntiTrapStarAbility extends AbilityItem implements Listener {

    private final Cache<UUID, UUID> playerHits;
    private int hitCache;

    public AntiTrapStarAbility(ConfigFile config) {
        super(AbilityType.ANTI_TRAP_STAR, "ANTI_TRAP_STAR", config);

        this.playerHits = CacheBuilder.newBuilder()
            .expireAfterAccess(this.hitCache, TimeUnit.SECONDS).build();

        this.overrideActivationMessage();
    }

    @Override
    protected void loadAdditionalData(ConfigurationSection abilitySection) {
        this.hitCache = abilitySection.getInt("HIT_CACHE");
    }

    public void sendActivationMessage(Player player, Player target) {
        this.activationMessage.forEach(line -> player.sendMessage(line
            .replace("<abilityName>", this.displayName)
            .replace("<target>", target.getName())
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
        target.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_ANTI_REDSTONE_TARGET_ACTIVATED // TODO
            .replace("<player>", target.getName())
            .replace("<abilityName>", this.displayName)
            .replace("<cooldown>", StringUtils.formatDurationWords(this.cooldown * 1000L)));

        player.teleport(target.getLocation());

        this.sendActivationMessage(player, target);
    }

    @Override
    protected boolean onItemClick(Player player, PlayerInteractEvent event) {
        UUID target = this.playerHits.getIfPresent(player.getUniqueId());
        if(target != null) {
            //this.activateAbilityOnTarget();

            event.setCancelled(true);
        }

        return true;
    }
}
