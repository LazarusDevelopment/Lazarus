package me.qiooip.lazarus.abilities.type;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.ServerUtils;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionEffectExpireEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class InvisibilityAbility extends AbilityItem implements Listener {

    @Getter private final Set<UUID> players;
    private final Set<UUID> offline;

    private int duration;

    public InvisibilityAbility(ConfigFile config) {
        super(AbilityType.INVISIBILITY, "INVISIBILITY", config);

        this.players = new HashSet<>();
        this.offline = new HashSet<>();
    }

    @Override
    protected void disable() {
        this.players.clear();
        this.offline.clear();
    }

    @Override
    protected void loadAdditionalData(ConfigurationSection abilitySection) {
        this.duration = abilitySection.getInt("DURATION") * 20;
    }

    @Override
    protected boolean onItemClick(Player player, PlayerInteractEvent event) {
        this.hidePlayer(player, this.duration);

        event.setCancelled(true);
        return true;
    }

    public void hidePlayer(Player player, int duration) {
        PotionEffect effect = new PotionEffect(PotionEffectType.INVISIBILITY, duration, 1);
        
        NmsUtils.getInstance().addPotionEffect(player, effect);
        NmsUtils.getInstance().updateArmor(player, true);

        this.players.add(player.getUniqueId());
    }

    public void hidePlayers(Player player) {
        String worldName = player.getWorld().getName();

        for(UUID uuid : this.players) {
            Player online = Bukkit.getPlayer(uuid);
            if(!worldName.equals(online.getWorld().getName())) continue;

            NmsUtils.getInstance().updateArmorFor(player, online, true);
        }
    }

    private void showPlayer(Player player, boolean forced) {
        this.players.remove(player.getUniqueId());

        if(forced) {
            Lazarus.getInstance().getPvpClassManager().getPotionEffectRestorer()
                .removePlayerEffect(player, PotionEffectType.INVISIBILITY);
        }

        NmsUtils.getInstance().updateArmor(player, false);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        Player target = (Player) event.getEntity();

        if(this.players.contains(target.getUniqueId())) {
            this.showPlayer(target, true);
            target.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_INVISIBILITY_BECOME_VISIBLE_ON_DAMAGE);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if(this.players.contains(player.getUniqueId())) {
            this.showPlayer(player, true);
            this.offline.add(player.getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if(this.offline.remove(player.getUniqueId())) {
            this.hidePlayer(player, this.duration);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if(this.players.contains(player.getUniqueId())) {
            this.showPlayer(player, true);
        }
    }

    @EventHandler
    public void onPotionEffectExpire(PotionEffectExpireEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        if(ServerUtils.getEffect(event).getType().getId() != 14) return;

        Player player = (Player) event.getEntity();

        if(this.players.contains(player.getUniqueId())) {
            this.showPlayer(player, false);
        }
    }
}
