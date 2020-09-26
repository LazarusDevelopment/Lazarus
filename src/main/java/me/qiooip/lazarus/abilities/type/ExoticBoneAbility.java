package me.qiooip.lazarus.abilities.type;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.cooldown.CooldownTimer;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

public class ExoticBoneAbility extends AbilityItem implements Listener {

    private int duration;
    private int hits;
    private final String cooldownName;

    private final Table<UUID, UUID, Integer> playerHits;

    public ExoticBoneAbility(ConfigFile config) {
        super(AbilityType.EXOTIC_BONE, "EXOTIC_BONE", config);

        this.cooldownName = "ExoticBone";
        this.playerHits = HashBasedTable.create();

        this.overrideActivationMessage();
    }

    @Override
    protected void disable() {
        this.playerHits.clear();
    }

    @Override
    protected void loadAdditionalData(ConfigurationSection abilitySection) {
        this.duration = abilitySection.getInt("DURATION");
        this.hits = abilitySection.getInt("HITS");
    }

    public void sendActivationMessage(Player player, Player target) {
        this.activationMessage.forEach(line -> player.sendMessage(line
            .replace("<abilityName>", this.displayName)
            .replace("<target>", target.getName())
            .replace("<duration>", DurationFormatUtils.formatDurationWords(this.duration * 1000, true, true))
            .replace("<cooldown>", DurationFormatUtils.formatDurationWords(this.cooldown * 1000, true, true))));
    }

    @Override
    protected boolean onPlayerItemHit(Player damager, Player target, EntityDamageByEntityEvent event) {
        if(this.playerHits.contains(damager.getUniqueId(), target.getUniqueId())) {
            int hitsNeeded = this.playerHits.get(damager.getUniqueId(), target.getUniqueId()) - 1;

            if(hitsNeeded == 0) {
                this.activateAbilityOnTarget(damager, target);
                this.playerHits.remove(damager.getUniqueId(), target.getUniqueId());
                return true;
            }

            this.playerHits.put(damager.getUniqueId(), target.getUniqueId(), hitsNeeded);
            return false;
        }

        this.playerHits.put(damager.getUniqueId(), target.getUniqueId(), --this.hits);
        return false;
    }

    private void activateAbilityOnTarget(Player damager, Player target) {
        TimerManager.getInstance().getCooldownTimer().activate(target, this.cooldownName, this.duration,
        Language.ABILITIES_PREFIX + Language.ABILITIES_EXOTIC_BONE_TARGET_EXPIRED);

        target.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_EXOTIC_BONE_TARGET_ACTIVATED
            .replace("<player>", damager.getName())
            .replace("<abilityName>", this.displayName)
            .replace("<duration>", DurationFormatUtils.formatDurationWords(this.duration * 1000, true, true)));

        this.sendActivationMessage(damager, target);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        CooldownTimer cooldownTimer = TimerManager.getInstance().getCooldownTimer();
        if(!cooldownTimer.isActive(player, this.cooldownName)) return;

        event.setCancelled(true);
        player.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_EXOTIC_BONE_CANNOT_INTERACT);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        CooldownTimer cooldownTimer = TimerManager.getInstance().getCooldownTimer();
        if(!cooldownTimer.isActive(player, this.cooldownName)) return;

        event.setCancelled(true);
        player.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_EXOTIC_BONE_CANNOT_INTERACT);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.useInteractedBlock() == Event.Result.DENY || !event.hasBlock()) return;
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(!NmsUtils.getInstance().getExoticBoneClickables().contains(event.getClickedBlock().getType())) return;

        Player player = event.getPlayer();

        CooldownTimer cooldownTimer = TimerManager.getInstance().getCooldownTimer();
        if(!cooldownTimer.isActive(player, this.cooldownName)) return;

        event.setCancelled(true);
        player.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_EXOTIC_BONE_CANNOT_INTERACT);
    }
}
