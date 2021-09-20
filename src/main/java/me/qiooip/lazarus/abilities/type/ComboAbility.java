package me.qiooip.lazarus.abilities.type;

import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.abilities.utils.AbilityUtils;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ComboAbility extends AbilityItem implements Listener {

    private int applyAfter;
    private int maximum;
    private int duration;

    private final String cooldownName;

    private final Map<UUID, Integer> playerHits;
    private List<PotionEffect> effects;

    public ComboAbility(ConfigFile config) {
        super(AbilityType.COMBO, "COMBO", config);

        this.cooldownName = "Combo";
        this.playerHits = new HashMap<>();

        this.overrideActivationMessage();
    }

    @Override
    protected void disable() {
        this.playerHits.clear();
        this.effects.clear();
    }

    @Override
    protected void loadAdditionalData(ConfigurationSection abilitySection) {
        this.applyAfter = abilitySection.getInt("APPLY_AFTER");
        this.maximum = abilitySection.getInt("MAXIMUM_DURATION");
        this.duration = abilitySection.getInt("PER_HIT");

        this.effects = AbilityUtils.loadEffects(abilitySection);
    }

    public void sendActivationMessage(Player player, int duration) {
        this.activationMessage.forEach(line -> player.sendMessage(line
            .replace("<abilityName>", this.displayName)
            .replace("<duration>", StringUtils.formatDurationWords(duration * 1000L))
            .replace("<cooldown>", StringUtils.formatDurationWords(this.cooldown * 1000L))));
    }

    @Override
    protected boolean onItemClick(Player player, PlayerInteractEvent event) {
        this.playerHits.put(player.getUniqueId(), 0);

        TimerManager.getInstance().getCooldownTimer().activate(player, this.cooldownName,
            this.applyAfter, null, () -> this.applyEffect(player.getUniqueId()));

        this.sendActivationMessage(player, this.applyAfter);

        event.setCancelled(true);
        return true;
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;

        Player damager = (Player) event.getDamager();
        if(!this.playerHits.containsKey(damager.getUniqueId())) return;

        this.playerHits.put(damager.getUniqueId(), this.playerHits.get(damager.getUniqueId()) + 1);
    }

    private void applyEffect(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if(player == null) return;

        int hits = this.playerHits.get(player.getUniqueId());
        List<PotionEffect> finalEffects;

        if(hits > 0) {
            finalEffects = new ArrayList<>();

            for(PotionEffect effect : this.effects) {
                int duration = Math.min(this.maximum * 20, effect.getDuration() + (this.duration * hits * 20));
                finalEffects.add(new PotionEffect(effect.getType(), duration, effect.getAmplifier()));
            }
        } else {
            finalEffects = this.effects;
        }

        this.addEffects(player, finalEffects);

        Language.ABILITIES_COMBO_APPLY_EFFECTS.forEach(line -> player.sendMessage(line
                .replace("<effects>", AbilityUtils.getEffectList(finalEffects, Language.ABILITIES_COMBO_EFFECT_FORMAT))));
    }
}
