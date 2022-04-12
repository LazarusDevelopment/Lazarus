package me.qiooip.lazarus.utils.nms;

import lombok.Getter;
import me.qiooip.lazarus.games.dragon.EnderDragon;
import me.qiooip.lazarus.games.loot.LootData;
import me.qiooip.lazarus.handlers.logger.CombatLogger;
import me.qiooip.lazarus.handlers.logger.CombatLoggerType;
import me.qiooip.lazarus.scoreboard.PlayerScoreboard;
import me.qiooip.lazarus.tab.PlayerTab;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

public abstract class NmsUtils {

    @Getter private static NmsUtils instance;
    protected Executor bukkitExecutor;

    protected static final String HANDLER_NAME = "packet_handler";
    protected static final String LISTENER_NAME = "glass_listener";

    public static void init() {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        if(version.equalsIgnoreCase("v1_7_R4")) {
            instance = new NmsUtils_1_7();
        } else {
            instance = new NmsUtils_1_8();
        }
    }

    public abstract void disable();

    public abstract boolean isSpigot18();

    public abstract boolean isMainThread();

    public abstract Executor getBukkitExecutor();

    public abstract boolean isCustomSpigot() throws NoSuchFieldException;

    public abstract CommandMap getCommandMap();

    public abstract Set<Material> getClickableItems();

    public abstract Set<Material> getPurgeClickableItems();

    public abstract Set<Material> getKitmapClickables();

    public abstract Set<Material> getExoticBoneClickables();

    public abstract void registerCombatLogger(CombatLoggerType loggerType);

    public abstract void registerEnderDragon();

    public abstract boolean isCombatLogger(Entity entity);

    public abstract void strikeLightningEffect(Player player, Location loc);

    public abstract int getClientVersion(Player player);

    public abstract int getPing(Player player);

    public abstract PlayerScoreboard getNewPlayerScoreboard(Player player);

    public abstract PlayerTab getNewPlayerTab(Player player);

    public abstract void increaseStatistic(Player player, Statistic statistic, Material material);

    public abstract void decreaseStatistic(Player player, Statistic statistic, Material material);

    public abstract boolean isInvulnerable(Player player);

    public abstract void toggleInvulnerable(Player player);

    public abstract ItemStack createMobSpawnerItemStack(EntityType spawnerType, String name);

    public abstract int getPotionEffectDuration(Player player, PotionEffectType type);

    public abstract PotionEffect getPotionEffect(Player player, PotionEffectType type);

    public abstract void addPotionEffect(Player player, PotionEffect effect);

    public abstract void removeInfinitePotionEffect(Player player, PotionEffect effect);

    public abstract Scoreboard getPlayerScoreboard(Player player);

    public abstract String getItemName(ItemStack item);

    public abstract List<ItemStack> getBlockDrops(ItemStack itemInHand, Block block);

    public abstract void damageItemInHand(Player player);

    public abstract void changeServerSlots(int amount);

    public abstract void setViewDistance(int amount);

    public abstract CombatLogger spawnCombatLogger(World world, Player player, CombatLoggerType loggerType);

    public abstract EnderDragon spawnEnderDragon(Location location, LootData loot);

    public abstract void sendHeaderAndFooter(Player player);

    public abstract void injectPacketInterceptor(Player player);

    public abstract void deinjectPacketInterceptor(Player player);

    public abstract void updateArmor(Player player, boolean remove);

    public abstract void updateArmorFor(Player player, Player target, boolean remove);

    public abstract void sendPacket(Player player, Object packet);

    public abstract void sendPackets(Player player, Object... packets);

    public abstract void sendHologramTeleportPacket(Player player, int entityId, Location location);

    public abstract void sendHologramSpawnPacket(Player player, int entityId, Location location, String message);

    public abstract void sendHologramMessagePacket(Player player, int entityId, String message);

    public abstract void sendHologramDestroyPacket(Player player, int entityId);
}
