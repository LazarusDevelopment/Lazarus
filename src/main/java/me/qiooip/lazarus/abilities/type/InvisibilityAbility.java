package me.qiooip.lazarus.abilities.type;

import lombok.Getter;
import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.abilities.reflection.AbilitiesReflection_1_7;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.utils.ServerUtils;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityEquipment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class InvisibilityAbility extends AbilityItem {

    // TODO: maknut nms use

    @Getter private final Set<UUID> players;
    private final Set<UUID> offline;

    public InvisibilityAbility(ConfigFile config) {
        super(AbilityType.INVISIBILITY, "INVISIBILITY", config);

        this.players = new HashSet<>();
        this.offline = new HashSet<>();
    }

    @Override
    protected void onItemClick(Player player) {
        this.hidePlayer(player);
    }

    private void hidePlayer(Player player) {
        new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0).apply(player);

        this.updateArmor(player, true);

        this.players.add(player.getUniqueId());
    }

    private void showPlayer(Player player) {
        this.players.remove(player.getUniqueId());

        player.removePotionEffect(PotionEffectType.INVISIBILITY);

        this.updateArmor(player, false);
    }

    private void updateArmor(Player player, boolean remove) {
        Set<PacketPlayOutEntityEquipment> packets = new HashSet<>();

        for (int slot = 1; slot < 5; slot++) {
            PacketPlayOutEntityEquipment equipment = AbilitiesReflection_1_7.createEquipmentPacket(player, slot, remove);
            packets.add(equipment);
        }

        for(Player other : player.getWorld().getPlayers()) {
            if(other == player) continue;

            for(PacketPlayOutEntityEquipment packet : packets) {
                ServerUtils.sendPacket(other, packet);
            }
        }

        player.updateInventory();
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
}
