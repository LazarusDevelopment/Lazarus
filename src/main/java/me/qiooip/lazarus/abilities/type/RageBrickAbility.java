package me.qiooip.lazarus.abilities.type;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.abilities.utils.AbilityUtils;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class RageBrickAbility extends AbilityItem {

    private int distance;
    private int duration;
    private int maximum;

    private List<PotionEffect> effects;

    public RageBrickAbility(ConfigFile config) {
        super(AbilityType.RAGE_BRICK, "RAGE_BRICK", config);

        this.overrideActivationMessage();
    }

    @Override
    protected void disable() {
        this.effects.clear();
    }

    @Override
    protected void loadAdditionalData(ConfigurationSection abilitySection) {
        this.distance = abilitySection.getInt("DISTANCE");
        this.duration = abilitySection.getInt("PER_PLAYER");
        this.maximum = abilitySection.getInt("MAXIMUM_DURATION");

        this.effects = AbilityUtils.loadEffects(abilitySection);
    }

    public void sendActivationMessage(Player player, int enemies, List<PotionEffect> effects) {
        this.activationMessage.forEach(line -> player.sendMessage(line
            .replace("<abilityName>", this.displayName)
            .replace("<amount>", String.valueOf(enemies))
            .replace("<effects>", AbilityUtils.getEffectList(effects, Language.ABILITIES_RAGE_BRICK_EFFECT_FORMAT))
            .replace("<cooldown>", StringUtils.formatDurationWords(this.cooldown * 1000L))));
    }

    @Override
    protected boolean onItemClick(Player player, PlayerInteractEvent event) {
        PlayerFaction playerFaction = FactionsManager.getInstance().getPlayerFaction(player);

        int amountOfEnemies = 0;

        for(Entity nearby : player.getNearbyEntities(this.distance, this.distance, this.distance)) {
            if(!(nearby instanceof Player) || player == nearby) continue;

            Player enemy = (Player) nearby;
            if(Lazarus.getInstance().getStaffModeManager().isInStaffModeOrVanished(enemy)) continue;
            if(ClaimManager.getInstance().getFactionAt(enemy).isSafezone()) continue;
            if(TimerManager.getInstance().getPvpProtTimer().isActive(enemy)) continue;

            PlayerFaction enemyFaction = FactionsManager.getInstance().getPlayerFaction(enemy);
            if(playerFaction != null && (playerFaction == enemyFaction || playerFaction.isAlly(enemyFaction))) continue;

            amountOfEnemies++;
        }

        List<PotionEffect> finalEffects;

        if(amountOfEnemies > 0) {
            finalEffects = new ArrayList<>();

            for(PotionEffect effect : this.effects) {
                int duration = Math.min(this.maximum * 20, effect.getDuration() + (this.duration * amountOfEnemies * 20));
                finalEffects.add(new PotionEffect(effect.getType(), duration, effect.getAmplifier()));
            }
        } else {
            finalEffects = this.effects;
        }

        this.addEffects(player, finalEffects);
        this.sendActivationMessage(player, amountOfEnemies, finalEffects);

        event.setCancelled(true);
        return true;
    }
}
