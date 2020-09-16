package me.qiooip.lazarus.abilities.type;

import lombok.Getter;
import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.utils.ServerUtils;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionEffectExpireEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class InvisibilityAbility extends AbilityItem {

    @Getter private final Set<UUID> players;
    private final Set<UUID> offline;

    private int duration;

    public InvisibilityAbility(ConfigFile config) {
        super(AbilityType.INVISIBILITY, "INVISIBILITY", config);

        this.players = new HashSet<>();
        this.offline = new HashSet<>();
    }

    @Override
    protected void loadAdditionalData(ConfigurationSection section) {
        this.duration = section.getInt("DURATION");
    }

    @Override
    protected void onItemClick(Player player) {
        this.hidePlayer(player);
    }

    private void hidePlayer(Player player) {
        PotionEffect effect = new PotionEffect(PotionEffectType.INVISIBILITY, this.duration * 20, 1);
        player.addPotionEffect(effect);

        NmsUtils.getInstance().updateArmor(player, true);

        this.players.add(player.getUniqueId());
    }

    private void showPlayer(Player player) {
        this.players.remove(player.getUniqueId());

        player.removePotionEffect(PotionEffectType.INVISIBILITY);

        NmsUtils.getInstance().updateArmor(player, false);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        Player target = (Player) event.getEntity();

        if(!this.players.contains(target.getUniqueId())) return;

        this.showPlayer(target);
        // TODO: message
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(!this.players.contains(player.getUniqueId())) return;

        this.showPlayer(player);

        this.offline.add(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(!this.offline.contains(player.getUniqueId())) return;

        this.hidePlayer(player);

        this.offline.remove(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if(!this.players.contains(player.getUniqueId())) return;

        this.showPlayer(player);
    }

    @EventHandler
    public void onPotionEffectExpire(PotionEffectExpireEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        if(!this.players.contains(player.getUniqueId())) return;

        PotionEffect effect = NmsUtils.getInstance().getPotionEffect(player, ServerUtils.getEffect(event).getType());
        if(effect == null || ServerUtils.getEffect(event).getType().getId() != 14) return;

        this.showPlayer(player);
    }
}
