package me.qiooip.lazarus.abilities.type;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.ServerUtils;
import me.qiooip.lazarus.utils.nms.NmsUtils;
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
        this.duration = abilitySection.getInt("DURATION");
    }

    @Override
    protected boolean onItemClick(Player player, PlayerInteractEvent event) {
        this.hidePlayer(player);

        event.setCancelled(true);
        return true;
    }

    private void hidePlayer(Player player) {
        PotionEffect effect = new PotionEffect(PotionEffectType.INVISIBILITY, this.duration * 20, 1);
        Lazarus.getInstance().getPvpClassManager().addPotionEffect(player, effect);

        NmsUtils.getInstance().updateArmor(player, true);

        this.players.add(player.getUniqueId());
    }

    private void showPlayer(Player player, boolean forced) {
        this.players.remove(player.getUniqueId());

        if(forced) {
            PotionEffect effect = Lazarus.getInstance().getPvpClassManager()
                .getPotionEffect(player, PotionEffectType.INVISIBILITY);

            if(effect == null) {
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
            } else {
                player.addPotionEffect(effect, true);
            }
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
            this.hidePlayer(player);
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
