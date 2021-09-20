package me.qiooip.lazarus.abilities.type;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.cooldown.CooldownTimer;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

public class AntiRedstoneAbility extends AbilityItem implements Listener {

    private int duration;
    private int hits;
    private final String cooldownName;

    private final Set<Material> clickables;
    private final Set<Material> physical;

    private final Table<UUID, UUID, Integer> playerHits;

    public AntiRedstoneAbility(ConfigFile config) {
        super(AbilityType.ANTI_REDSTONE, "ANTI_REDSTONE", config);

        this.cooldownName = "AntiRedstone";

        this.clickables = EnumSet.of(Material.LEVER, Material.STONE_BUTTON, Material.WOOD_BUTTON);
        this.physical = EnumSet.of(Material.GOLD_PLATE, Material.IRON_PLATE, Material.STONE_PLATE, Material.WOOD_PLATE);
        this.playerHits = HashBasedTable.create();

        this.overrideActivationMessage();
    }

    @Override
    protected void disable() {
        this.clickables.clear();
        this.physical.clear();

        this.playerHits.clear();
    }

    @Override
    protected void loadAdditionalData(ConfigurationSection abilitySection) {
        this.duration = abilitySection.getInt("DURATION");
        this.hits = abilitySection.getInt("HITS") - 1;
    }

    public void sendActivationMessage(Player player, Player target) {
        this.activationMessage.forEach(line -> player.sendMessage(line
            .replace("<abilityName>", this.displayName)
            .replace("<target>", target.getName())
            .replace("<duration>", StringUtils.formatDurationWords(this.duration * 1000L))
            .replace("<cooldown>", StringUtils.formatDurationWords(this.cooldown * 1000L))));
    }

    @Override
    protected boolean onPlayerItemHit(Player damager, Player target, EntityDamageByEntityEvent event) {
        UUID damagerUUID = damager.getUniqueId();
        UUID targetUUID = target.getUniqueId();

        if(this.playerHits.contains(damagerUUID, targetUUID)) {
            int hitsNeeded = this.playerHits.get(damagerUUID, targetUUID) - 1;

            if(hitsNeeded == 0) {
                this.activateAbilityOnTarget(damager, target);
                this.playerHits.remove(damagerUUID, targetUUID);
                return true;
            }

            this.playerHits.put(damagerUUID, targetUUID, hitsNeeded);
            return false;
        }

        this.playerHits.put(damagerUUID, targetUUID, this.hits);
        return false;
    }

    private void activateAbilityOnTarget(Player damager, Player target) {
        TimerManager.getInstance().getCooldownTimer().activate(target, this.cooldownName, this.duration,
            Language.ABILITIES_PREFIX + Language.ABILITIES_ANTI_REDSTONE_TARGET_EXPIRED);

        target.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_ANTI_REDSTONE_TARGET_ACTIVATED
            .replace("<player>", damager.getName())
            .replace("<abilityName>", this.displayName)
            .replace("<duration>", StringUtils.formatDurationWords(this.duration * 1000L)));

        this.sendActivationMessage(damager, target);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.useInteractedBlock() == Event.Result.DENY || !event.hasBlock()) return;

        Player player = event.getPlayer();

        CooldownTimer cooldownTimer = TimerManager.getInstance().getCooldownTimer();
        if(!cooldownTimer.isActive(player, this.cooldownName)) return;

        Block block = event.getClickedBlock();

        if(event.getAction() == Action.RIGHT_CLICK_BLOCK && this.clickables.contains(block.getType())) {
            player.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_ANTI_REDSTONE_CANNOT_USE
                .replace("<time>", cooldownTimer.getDynamicTimeLeft(player, this.cooldownName)));

            event.setCancelled(true);
            return;
        }

        if(event.getAction() == Action.PHYSICAL && this.physical.contains(block.getType())) {
            this.sendDelayedMessage(player, Language.ABILITIES_PREFIX + Language.ABILITIES_ANTI_REDSTONE_CANNOT_USE
                .replace("<time>", cooldownTimer.getDynamicTimeLeft(player, this.cooldownName)));

            event.setCancelled(true);
        }
    }
}
