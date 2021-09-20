package me.qiooip.lazarus.abilities.type;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;

public class StarvationFleshAbility extends AbilityItem implements Listener {

    private boolean classes;
    private int hunger;
    private int hits;

    private final Table<UUID, UUID, Integer> playerHits;

    public StarvationFleshAbility(ConfigFile config) {
        super(AbilityType.STARVATION_FLESH, "STARVATION_FLESH", config);

        this.playerHits = HashBasedTable.create();

        this.overrideActivationMessage();
    }

    @Override
    protected void disable() {
        this.playerHits.clear();
    }

    @Override
    protected void loadAdditionalData(ConfigurationSection abilitySection) {
        this.classes = abilitySection.getBoolean("CLASSES");
        this.hunger = abilitySection.getInt("HUNGER");
        this.hits = abilitySection.getInt("HITS") - 1;
    }

    @Override
    protected boolean onPlayerItemHit(Player damager, Player target, EntityDamageByEntityEvent event) {
        UUID damagerUUID = damager.getUniqueId();
        UUID targetUUID = target.getUniqueId();

        if(!this.classes && Lazarus.getInstance().getPvpClassManager().getActivePvpClass(target) != null) {
            damager.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_STARVATION_FLESH_TARGET_EQUIPPED_CLASS
                .replace("<player>", target.getName()));
            return false;
        }

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

    public void sendActivationMessage(Player player, Player target) {
        this.activationMessage.forEach(line -> player.sendMessage(line
                .replace("<abilityName>", this.displayName)
                .replace("<target>", target.getName())
                .replace("<hunger>", String.valueOf(this.hunger))
                .replace("<cooldown>", StringUtils.formatDurationWords(this.cooldown * 1000L))));
    }

    private void activateAbilityOnTarget(Player damager, Player target) {
        target.setFoodLevel(this.hunger);

        target.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_STARVATION_FLESH_TARGET_ACTIVATED
            .replace("<player>", damager.getName())
            .replace("<abilityName>", this.displayName)
            .replace("<hunger>", String.valueOf(this.hunger)));

        this.sendActivationMessage(damager, target);
    }
}
