package me.qiooip.lazarus.abilities.type;

import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.abilities.utils.AbilityUtils;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class PocketBardAbility extends AbilityItem {

    private int distance;
    private List<PotionEffect> effects;

    public PocketBardAbility(ConfigFile config) {
        super(AbilityType.POCKET_BARD, "POCKET_BARD", config);

        this.overrideActivationMessage();
    }

    @Override
    protected void disable() {
        this.effects.clear();
    }

    @Override
    protected void loadAdditionalData(ConfigurationSection abilitySection) {
        int distance = abilitySection.getInt("DISTANCE");
        this.distance = distance * distance;

        this.effects = AbilityUtils.loadEffects(abilitySection);
    }

    public void sendActivationMessage(Player player, int teammateAmount) {
        this.activationMessage.forEach(line -> player.sendMessage(line
            .replace("<abilityName>", this.displayName)
            .replace("<amount>", String.valueOf(teammateAmount))
            .replace("<effects>", AbilityUtils.getEffectList(this.effects, Language.ABILITIES_POCKET_BARD_EFFECT_FORMAT))
            .replace("<cooldown>", StringUtils.formatDurationWords(this.cooldown * 1000L))));
    }

    @Override
    protected boolean onItemClick(Player player, PlayerInteractEvent event) {
        PlayerFaction playerFaction = FactionsManager.getInstance().getPlayerFaction(player);
        int amountOfTeammates = 0;

        if(playerFaction == null) {
            this.addEffects(player, this.effects);
        } else {
            for(Player member : playerFaction.getOnlinePlayers()) {
                if(player.getWorld() != member.getWorld()) continue;
                if(player.getLocation().distanceSquared(member.getLocation()) > this.distance) continue;

                this.addEffects(member, this.effects);

                if(member != player) {
                    amountOfTeammates++;
                }
            }
        }

        this.sendActivationMessage(player, amountOfTeammates);

        event.setCancelled(true);
        return true;
    }
}
