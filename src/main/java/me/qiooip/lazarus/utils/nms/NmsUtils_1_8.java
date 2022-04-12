package me.qiooip.lazarus.utils.nms;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.abilities.AbilitiesManager;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.abilities.reflection.AbilitiesReflection_1_8;
import me.qiooip.lazarus.abilities.type.DecoyAbility;
import me.qiooip.lazarus.abilities.type.InvisibilityAbility;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.games.dragon.EnderDragon;
import me.qiooip.lazarus.games.dragon.nms.EnderDragon_1_8;
import me.qiooip.lazarus.games.loot.LootData;
import me.qiooip.lazarus.glass.GlassInfo;
import me.qiooip.lazarus.hologram.reflection.HologramReflection_1_8.PacketPlayOutEntityTeleportWrapper;
import me.qiooip.lazarus.hologram.reflection.HologramReflection_1_8.PacketPlayOutSpawnEntityLivingWrapper;
import me.qiooip.lazarus.handlers.logger.CombatLogger;
import me.qiooip.lazarus.handlers.logger.CombatLoggerType;
import me.qiooip.lazarus.handlers.logger.nms.SkeletonCombatLogger_1_8;
import me.qiooip.lazarus.handlers.logger.nms.VillagerCombatLogger_1_8;
import me.qiooip.lazarus.scoreboard.PlayerScoreboard;
import me.qiooip.lazarus.scoreboard.nms.PlayerScoreboard_1_8;
import me.qiooip.lazarus.tab.PlayerTab;
import me.qiooip.lazarus.tab.nms.PlayerTab_1_8;
import me.qiooip.lazarus.utils.Tasks;
import me.qiooip.lazarus.utils.item.ItemUtils;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.BlockCocoa;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.EntityLightning;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntityTypes;
import net.minecraft.server.v1_8_R3.GameProfileSerializer;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.MobEffect;
import net.minecraft.server.v1_8_R3.MobEffectList;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketListener;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig.EnumPlayerDigType;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedSoundEffect;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityWeather;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.PlayerList;
import net.minecraft.server.v1_8_R3.ScoreboardServer;
import net.minecraft.server.v1_8_R3.TileEntitySkull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.CommandMap;
import org.bukkit.craftbukkit.v1_8_R3.CraftChunk;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftStatistic;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.scheduler.CraftScheduler;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import protocolsupport.api.ProtocolSupportAPI;
import us.myles.ViaVersion.api.Via;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class NmsUtils_1_8 extends NmsUtils implements Listener {

    private final Set<Material> clickableItems;
    private final Set<Material> purgeClickableItems;
    private final Set<Material> kitmapClickables;
    private final Set<Material> exoticBoneClickables;

    private final Function<Player, Integer> clientVersionFunction;

    public NmsUtils_1_8() {
        Plugin plugin;

        if((plugin = Bukkit.getPluginManager().getPlugin("ViaVersion")) != null && plugin.isEnabled()) {
            this.clientVersionFunction = player -> Via.getAPI().getPlayerVersion(player.getUniqueId());
        } else if((plugin = Bukkit.getPluginManager().getPlugin("ProtocolSupport")) != null && plugin.isEnabled()) {
            this.clientVersionFunction = player -> ProtocolSupportAPI.getProtocolVersion(player).getId();
        } else {
            this.clientVersionFunction = player -> 47;
        }

        this.fetchBukkitExecutor();

        this.clickableItems = EnumSet.of(Material.ANVIL, Material.BEACON, Material.FIRE,
            Material.FENCE_GATE, Material.WOOD_BUTTON, Material.WOODEN_DOOR, Material.IRON_DOOR_BLOCK,
            Material.TRAPPED_CHEST, Material.FURNACE, Material.BURNING_FURNACE, Material.BREWING_STAND,
            Material.HOPPER, Material.DROPPER, Material.DISPENSER, Material.STONE_BUTTON, Material.BED_BLOCK,
            Material.ENCHANTMENT_TABLE, Material.LEVER, Material.TRAP_DOOR, Material.CHEST, Material.DIODE_BLOCK_ON,
            Material.DIODE_BLOCK_OFF, Material.REDSTONE_COMPARATOR_ON, Material.REDSTONE_COMPARATOR_OFF,
            Material.JUKEBOX, Material.WORKBENCH, Material.ENDER_CHEST, Material.ACACIA_FENCE_GATE,
            Material.BIRCH_FENCE_GATE, Material.DARK_OAK_FENCE_GATE, Material.JUNGLE_FENCE_GATE,
            Material.SPRUCE_FENCE_GATE, Material.ACACIA_DOOR, Material.BIRCH_DOOR, Material.DARK_OAK_DOOR,
            Material.JUNGLE_DOOR, Material.SPRUCE_DOOR);

        this.purgeClickableItems = EnumSet.of(Material.ANVIL, Material.BEACON,
            Material.FENCE_GATE, Material.WOOD_BUTTON, Material.WOODEN_DOOR, Material.IRON_DOOR_BLOCK,
            Material.TRAPPED_CHEST, Material.FURNACE, Material.BURNING_FURNACE, Material.BREWING_STAND,
            Material.HOPPER, Material.DROPPER, Material.DISPENSER, Material.STONE_BUTTON,
            Material.ENCHANTMENT_TABLE, Material.LEVER, Material.TRAP_DOOR, Material.CHEST,
            Material.JUKEBOX, Material.WORKBENCH, Material.ENDER_CHEST, Material.ACACIA_FENCE_GATE,
            Material.BIRCH_FENCE_GATE, Material.DARK_OAK_FENCE_GATE, Material.JUNGLE_FENCE_GATE,
            Material.SPRUCE_FENCE_GATE, Material.ACACIA_DOOR, Material.BIRCH_DOOR, Material.DARK_OAK_DOOR,
            Material.JUNGLE_DOOR, Material.SPRUCE_DOOR);

        this.kitmapClickables = EnumSet.of(Material.LEVER, Material.WOOD_BUTTON, Material.STONE_BUTTON,
            Material.WOOD_PLATE, Material.STONE_PLATE, Material.IRON_PLATE, Material.GOLD_PLATE,
            Material.WORKBENCH, Material.FURNACE, Material.BURNING_FURNACE, Material.FENCE_GATE,
            Material.SPRUCE_FENCE_GATE, Material.JUNGLE_FENCE_GATE, Material.DARK_OAK_FENCE_GATE,
            Material.BIRCH_FENCE_GATE, Material.ACACIA_FENCE_GATE);

        this.exoticBoneClickables = EnumSet.of(Material.CHEST, Material.TRAPPED_CHEST, Material.HOPPER,
            Material.DROPPER, Material.DISPENSER, Material.TRAP_DOOR, Material.WORKBENCH,
            Material.ENDER_CHEST, Material.ACACIA_DOOR, Material.BIRCH_DOOR, Material.DARK_OAK_DOOR,
            Material.JUNGLE_DOOR, Material.SPRUCE_DOOR, Material.WOODEN_DOOR, Material.FENCE_GATE,
            Material.ACACIA_FENCE_GATE, Material.BIRCH_FENCE_GATE, Material.DARK_OAK_FENCE_GATE,
            Material.JUNGLE_FENCE_GATE, Material.SPRUCE_FENCE_GATE);

        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
        Tasks.asyncLater(() -> Bukkit.getOnlinePlayers().forEach(this::injectPacketInterceptor), 20L);
    }

    @Override
    public void disable() {
        Bukkit.getOnlinePlayers().forEach(this::deinjectPacketInterceptor);
    }

    @Override
    public boolean isSpigot18() {
        return true;
    }

    @Override
    public boolean isMainThread() {
        return MinecraftServer.getServer().primaryThread == Thread.currentThread();
    }

    @Override
    public Executor getBukkitExecutor() {
        return this.bukkitExecutor;
    }

    @Override
    public boolean isCustomSpigot() {
        return false;
    }

    @Override
    public CommandMap getCommandMap() {
        try {
            CraftServer craftServer = ((CraftServer) Bukkit.getServer());

            Field commandMapField = craftServer.getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);

            return (CommandMap) commandMapField.get(craftServer);
        } catch(NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<Material> getClickableItems() {
        return this.clickableItems;
    }

    @Override
    public Set<Material> getPurgeClickableItems() {
        return this.purgeClickableItems;
    }

    @Override
    public Set<Material> getKitmapClickables() {
        return this.kitmapClickables;
    }

    @Override
    public Set<Material> getExoticBoneClickables() {
        return this.exoticBoneClickables;
    }

    @Override
    public void registerCombatLogger(CombatLoggerType loggerType) {
        if(loggerType == CombatLoggerType.SKELETON) {
            this.setEntityTypesField("d", SkeletonCombatLogger_1_8.class, "CombatLogger");
            this.setEntityTypesField("f", SkeletonCombatLogger_1_8.class, 51);
        } else {
            this.setEntityTypesField("d", VillagerCombatLogger_1_8.class, "CombatLogger");
            this.setEntityTypesField("f", VillagerCombatLogger_1_8.class, 120);
        }
    }

    @Override
    public void registerEnderDragon() {
        this.setEntityTypesField("d", EnderDragon_1_8.class, "CustomDragon");
        this.setEntityTypesField("f", EnderDragon_1_8.class, 63);
    }

    @Override
    public boolean isCombatLogger(Entity entity) {
        return ((CraftEntity) entity).getHandle() instanceof SkeletonCombatLogger_1_8;
    }

    @Override
    public void strikeLightningEffect(Player player, Location loc) {
        EntityLightning lightning = new EntityLightning(((CraftWorld) player.getWorld())
            .getHandle(), loc.getX(), loc.getY(), loc.getZ(), true);

        PacketPlayOutSpawnEntityWeather lightningPacket = new PacketPlayOutSpawnEntityWeather(lightning);

        PacketPlayOutNamedSoundEffect thunderPacket = new PacketPlayOutNamedSoundEffect("ambient.weather.thunder",
            loc.getX(), loc.getY(), loc.getZ(), 10000.0F, 0.8F + ThreadLocalRandom.current().nextFloat() * 0.2F);

        Bukkit.getOnlinePlayers().forEach(online -> {
            if(player.getWorld() != online.getWorld() || !Lazarus.getInstance().getUserdataManager()
                .getUserdata(online).getSettings().isLightning()) return;

            double x = loc.getX() - online.getLocation().getX();
            double y = loc.getY() - online.getLocation().getY();
            double z = loc.getZ() - online.getLocation().getZ();

            if (x * x + y * y + z * z < 16384) {
                PlayerConnection connection = ((CraftPlayer) online).getHandle().playerConnection;

                connection.sendPacket(lightningPacket);
                connection.sendPacket(thunderPacket);
            }
        });
    }

    @Override
    public int getClientVersion(Player player) {
        return this.clientVersionFunction.apply(player);
    }

    @Override
    public int getPing(Player player) {
        return ((CraftPlayer) player).getHandle().ping;
    }

    @Override
    public PlayerScoreboard getNewPlayerScoreboard(Player player) {
        return new PlayerScoreboard_1_8(player);
    }

    @Override
    public PlayerTab getNewPlayerTab(Player player) {
        return new PlayerTab_1_8(player);
    }

    @Override
    public void increaseStatistic(Player player, Statistic statistic, Material material) {
        net.minecraft.server.v1_8_R3.Statistic nmsStatistic = CraftStatistic.getMaterialStatistic(statistic, material);

        if(nmsStatistic == null) {
            return;
        }

        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        handle.getStatisticManager().b(handle, nmsStatistic, 1);
    }

    @Override
    public void decreaseStatistic(Player player, Statistic statistic, Material material) {
        net.minecraft.server.v1_8_R3.Statistic nmsStatistic = CraftStatistic.getMaterialStatistic(statistic, material);

        if(nmsStatistic == null) {
            return;
        }

        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        handle.getStatisticManager().b(handle, nmsStatistic, -1);
    }

    @Override
    public boolean isInvulnerable(Player player) {
        return ((CraftPlayer) player).getHandle().isInvulnerable(DamageSource.GENERIC);
    }

    @Override
    public void toggleInvulnerable(Player player) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        Class<?> superclass = entityPlayer.getClass().getSuperclass();

        do {
            superclass = superclass.getSuperclass();
        } while(superclass != net.minecraft.server.v1_8_R3.Entity.class);

        try {
            Field invulnerableField = superclass.getDeclaredField("invulnerable");
            invulnerableField.setAccessible(true);

            invulnerableField.set(entityPlayer, !this.isInvulnerable(player));
        } catch(NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ItemStack createMobSpawnerItemStack(EntityType spawnerType, String name) {
        int durability = ItemUtils.getSpawnerDurability(spawnerType);
        ItemStack spawner = new ItemStack(Material.MOB_SPAWNER, 1, (short) durability);

        BlockStateMeta blockStateMeta = (BlockStateMeta) spawner.getItemMeta();

        if(name != null) {
            blockStateMeta.setDisplayName(name);
        }

        CreatureSpawner creatureSpawner = (CreatureSpawner) blockStateMeta.getBlockState();

        creatureSpawner.setSpawnedType(spawnerType);
        blockStateMeta.setBlockState(creatureSpawner);

        spawner.setItemMeta(blockStateMeta);
        return spawner;
    }

    @Override
    public int getPotionEffectDuration(Player player, PotionEffectType type) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        MobEffect nmsEffect = entityPlayer.getEffect(MobEffectList.byId[type.getId()]);

        return nmsEffect != null ? nmsEffect.getDuration() : -1;
    }

    @Override
    public PotionEffect getPotionEffect(Player player, PotionEffectType type) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        MobEffect nmsEffect = entityPlayer.getEffect(MobEffectList.byId[type.getId()]);

        return nmsEffect != null ? new PotionEffect(PotionEffectType.getById(nmsEffect.getEffectId()),
            nmsEffect.getDuration(), nmsEffect.getAmplifier(), nmsEffect.isAmbient()) : null;
    }

    @Override
    public void addPotionEffect(Player player, PotionEffect effect) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        MobEffect mobEffect = new MobEffect(effect.getType().getId(), effect.getDuration(), effect.getAmplifier());

        entityPlayer.removeEffect(mobEffect.getEffectId());

        if(this.isMainThread()) {
            entityPlayer.addEffect(mobEffect);
        } else {
            Tasks.sync(() -> entityPlayer.addEffect(mobEffect));
        }
    }

    @Override
    public void removeInfinitePotionEffect(Player player, PotionEffect toRemove) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        MobEffect nmsEffect = entityPlayer.getEffect(MobEffectList.byId[toRemove.getType().getId()]);

        if(nmsEffect == null) return;
        if(toRemove.getAmplifier() != nmsEffect.getAmplifier() || nmsEffect.getDuration() < 12000) return;

        Lazarus.getInstance().getPvpClassManager().getPotionEffectRestorer()
            .removePlayerEffect(player, toRemove.getType());
    }

    @Override
    public Scoreboard getPlayerScoreboard(Player player) {
        return player.getScoreboard() == Bukkit.getScoreboardManager()
            .getMainScoreboard() ? newScoreboard() : player.getScoreboard();
    }

    @Override
    public String getItemName(ItemStack item) {
        if(item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            return item.getItemMeta().getDisplayName();
        }

        return CraftItemStack.asNMSCopy(item).getName();
    }

    @Override
    public List<ItemStack> getBlockDrops(ItemStack itemInHand, Block block) {
        List<ItemStack> drops = new ArrayList<>();

        net.minecraft.server.v1_8_R3.Block nmsBlock = CraftMagicNumbers.getBlock(block);

        if(nmsBlock == Blocks.AIR || !this.itemCausesDrops(itemInHand, nmsBlock)) {
            return drops;
        }

        if(itemInHand != null && itemInHand.containsEnchantment(Enchantment.SILK_TOUCH)) {
            drops.add(new ItemStack(block.getType(), 1, block.getData()));
            return drops;
        }

        net.minecraft.server.v1_8_R3.Chunk nmsChunk = ((CraftChunk) block.getChunk()).getHandle();

        int fortuneLevel = itemInHand == null ? 0 : itemInHand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);

        int count = nmsBlock.getDropCount(fortuneLevel, nmsChunk.world.random);
        byte data = block.getData();

        for(int i = 0; i < count; i++) {
            net.minecraft.server.v1_8_R3.Item item = nmsBlock.getDropType(nmsBlock.fromLegacyData(data), nmsChunk.world.random, fortuneLevel);

            if(nmsBlock == Blocks.SKULL) {
                int dropData = nmsBlock.getDropData(nmsChunk.world, new BlockPosition(block.getX(), block.getY(), block.getZ()));
                net.minecraft.server.v1_8_R3.ItemStack nmsStack = new net.minecraft.server.v1_8_R3.ItemStack(item, 1, dropData);
                TileEntitySkull tileentityskull = (TileEntitySkull) nmsChunk.world.getTileEntity(new BlockPosition(block.getX(), block.getY(), block.getZ()));

                if(tileentityskull.getSkullType() == 3 && tileentityskull.getGameProfile() != null) {
                    nmsStack.setTag(new NBTTagCompound());
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    GameProfileSerializer.serialize(nbttagcompound, tileentityskull.getGameProfile());
                    nmsStack.getTag().set("SkullOwner", nbttagcompound);
                }

                drops.add(org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack.asBukkitCopy(nmsStack));
            } else if (nmsBlock == Blocks.COCOA) {
                int age = nmsBlock.fromLegacyData(data).get(BlockCocoa.AGE);
                int dropAmount = age >= 2 ? 3 : 1;

                for(int j = 0; j < dropAmount; ++j) {
                    drops.add(new ItemStack(Material.INK_SACK, 1, (short)3));
                }
            } else {
                drops.add(new ItemStack(CraftMagicNumbers.getMaterial(item), 1, (short) nmsBlock.getDropData(nmsBlock.fromLegacyData(data))));
            }
        }

        return drops;
    }

    @Override
    public void damageItemInHand(Player player) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

        net.minecraft.server.v1_8_R3.ItemStack itemInHand = entityPlayer.inventory.getItemInHand();

        if(itemInHand != null) {
            itemInHand.damage(1, entityPlayer);
        }
    }

    @Override
    public void changeServerSlots(int amount) {
        PlayerList playerList = ((CraftServer) Bukkit.getServer()).getHandle();

        try {
            Field maxPlayersField = playerList.getClass().getSuperclass().getDeclaredField("maxPlayers");
            maxPlayersField.setAccessible(true);

            maxPlayersField.set(playerList, amount);
        } catch(NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setViewDistance(int amount) {
        ((CraftServer) Bukkit.getServer()).getServer().getPropertyManager().setProperty("view-distance", amount);
    }

    @Override
    public CombatLogger spawnCombatLogger(World world, Player player, CombatLoggerType loggerType) {
        return loggerType == CombatLoggerType.SKELETON
            ? new SkeletonCombatLogger_1_8(world, player)
            : new VillagerCombatLogger_1_8(world, player);
    }

    @Override
    public EnderDragon spawnEnderDragon(Location location, LootData loot) {
        return new EnderDragon_1_8(location, loot);
    }

    @Override
    public void sendHeaderAndFooter(Player player) {
        player.setPlayerListHeaderFooter(
            TextComponent.fromLegacyText(Config.TAB_HEADER),
            TextComponent.fromLegacyText(Config.TAB_FOOTER)
        );
    }

    @Override
    public void injectPacketInterceptor(Player player) {
        if(!player.isOnline()) return;

        CraftPlayer cplayer = (CraftPlayer) player;

        Channel channel = cplayer.getHandle().playerConnection.networkManager.channel;
        if(channel == null) return;

        ChannelDuplexHandler handler = new ChannelDuplexHandler() {

            @Override
            public void write(ChannelHandlerContext context, Object packet, ChannelPromise promise) throws Exception {
                if(packet instanceof PacketPlayOutEntityEquipment) {
                    packet = handlePlayOutEntityEquipmentPacket(player, (PacketPlayOutEntityEquipment) packet);
                }

                if(packet != null) {
                    super.write(context, packet, promise);
                }
            }

            @Override
            public void channelRead(ChannelHandlerContext context, Object packet) throws Exception {
                if(packet instanceof PacketPlayInBlockDig) {
                    if(handlePlayInBlockDigPacket(player, (PacketPlayInBlockDig) packet)) return;
                } else if(packet instanceof PacketPlayInBlockPlace) {
                    if(handlePlayInBlockPlacePacket(player, (PacketPlayInBlockPlace) packet)) return;
                }

                super.channelRead(context, packet);
            }
        };

        if(channel.pipeline().get(LISTENER_NAME) == null) {
            try {
                channel.pipeline().addBefore(HANDLER_NAME, LISTENER_NAME, handler);
            } catch(NoSuchElementException ignored) { }
        }

        AbilitiesManager manager = AbilitiesManager.getInstance();

        InvisibilityAbility invisibilityAbility = (InvisibilityAbility) manager.getAbilityItemByType(AbilityType.INVISIBILITY);
        if(invisibilityAbility != null) {
            invisibilityAbility.hidePlayers(player);
        }

        DecoyAbility decoyAbility = (DecoyAbility) manager.getAbilityItemByType(AbilityType.DECOY);
        if(decoyAbility != null) {
            decoyAbility.hidePlayers(player);
        }
    }

    @Override
    public void deinjectPacketInterceptor(Player player) {
        CraftPlayer cplayer = (CraftPlayer) player;

        Channel channel = cplayer.getHandle().playerConnection.networkManager.channel;
        if(channel == null) return;

        if(channel.pipeline().get(LISTENER_NAME) != null) {
            channel.pipeline().remove(LISTENER_NAME);
        }
    }

    @Override
    public void updateArmor(Player player, boolean remove) {
        Set<PacketPlayOutEntityEquipment> packets = this.getEquipmentPackets(player, remove);

        for(Player other : player.getWorld().getPlayers()) {
            if(other == player) continue;

            for(PacketPlayOutEntityEquipment packet : packets) {
                this.sendPacket(other, packet);
            }
        }

        player.updateInventory();
    }

    @Override
    public void updateArmorFor(Player player, Player target, boolean remove) {
        Set<PacketPlayOutEntityEquipment> packets = this.getEquipmentPackets(target, remove);

        for(PacketPlayOutEntityEquipment packet : packets) {
            this.sendPacket(player, packet);
        }
    }

    private Set<PacketPlayOutEntityEquipment> getEquipmentPackets(Player player, boolean remove) {
        Set<PacketPlayOutEntityEquipment> packets = new HashSet<>();

        for (int slot = 1; slot < 5; slot++) {
            PacketPlayOutEntityEquipment equipment = AbilitiesReflection_1_8.createEquipmentPacket(player, slot, remove);
            packets.add(equipment);
        }

        return packets;
    }

    @Override
    public void sendPacket(Player player, Object packet) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket((Packet<? extends PacketListener>) packet);
    }

    @Override
    public void sendPackets(Player player, Object... packets) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;

        for(Object packet : packets) {
            connection.sendPacket((Packet<? extends PacketListener>) packet);
        }
    }

    @Override
    public void sendHologramTeleportPacket(Player player, int entityId, Location location) {
        PacketPlayOutEntityTeleport teleport = PacketPlayOutEntityTeleportWrapper
            .newTeleportPacket(entityId, location);

        this.sendPacket(player, teleport);
    }

    @Override
    public void sendHologramSpawnPacket(Player player, int entityId, Location location, String message) {
        DataWatcher watcher = new DataWatcher(null);
        watcher.a(0, (byte) 0x20);
        watcher.a(2, message);
        watcher.a(3, (byte) 1);
        watcher.a(10, (byte) 0x16);

        PacketPlayOutSpawnEntityLiving armorStand = PacketPlayOutSpawnEntityLivingWrapper
            .newEntitySpawnPacket(entityId, location, watcher);

        this.sendPacket(player, armorStand);
    }

    @Override
    public void sendHologramMessagePacket(Player player, int entityId, String message) {
        DataWatcher watcher = new DataWatcher(null);
        watcher.a(2, message);

        this.sendPacket(player, new PacketPlayOutEntityMetadata(entityId, watcher, true));
    }

    @Override
    public void sendHologramDestroyPacket(Player player, int entityId) {
        this.sendPacket(player, new PacketPlayOutEntityDestroy(entityId));
    }

    private PacketPlayOutEntityEquipment handlePlayOutEntityEquipmentPacket(Player player, PacketPlayOutEntityEquipment equipmentPacket) {
        try {
            int entityId = AbilitiesReflection_1_8.getEntityId(equipmentPacket);
            net.minecraft.server.v1_8_R3.Entity sender = ((CraftPlayer) player).getHandle().world.a(entityId);

            if(!(sender instanceof EntityPlayer)) {
                return equipmentPacket;
            }

            AbilitiesManager manager = AbilitiesManager.getInstance();

            InvisibilityAbility invisibilityAbility = (InvisibilityAbility) manager.getAbilityItemByType(AbilityType.INVISIBILITY);
            DecoyAbility decoyAbility = (DecoyAbility) manager.getAbilityItemByType(AbilityType.DECOY);

            boolean shouldCancel = (invisibilityAbility != null && invisibilityAbility.getPlayers().contains(sender.getUniqueID()))
                || (decoyAbility != null && decoyAbility.getPlayers().contains(sender.getUniqueID()));

            if(shouldCancel) {
                int slot = AbilitiesReflection_1_8.getSlot(equipmentPacket);
                net.minecraft.server.v1_8_R3.ItemStack itemStack = AbilitiesReflection_1_8.getItemStack(equipmentPacket);

                // Make sure we only cancel the armor packets
                if(itemStack != null && slot != 0) {
                    return null;
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return equipmentPacket;
    }

    private boolean handlePlayInBlockDigPacket(Player player, PacketPlayInBlockDig digPacket) {
        if(digPacket.c() != EnumPlayerDigType.START_DESTROY_BLOCK && digPacket.c() != EnumPlayerDigType.STOP_DESTROY_BLOCK) {
            return false;
        }

        Location location = new Location(player.getWorld(), digPacket.a().getX(), digPacket.a().getY(), digPacket.a().getZ());
        GlassInfo glassInfo = Lazarus.getInstance().getGlassManager().getGlassAt(player, location);

        if(glassInfo != null) {
            player.sendBlockChange(location, glassInfo.getMaterial(), glassInfo.getData());
            return true;
        }

        return false;
    }

    private boolean handlePlayInBlockPlacePacket(Player player, PacketPlayInBlockPlace placePacket) {
        BlockPosition position = placePacket.a();
        Location location = new Location(player.getWorld(), position.getX(), position.getY(), position.getZ());

        GlassInfo glassInfo = Lazarus.getInstance().getGlassManager().getGlassAt(player, location);
        if(glassInfo == null) return false;

        player.sendBlockChange(location, glassInfo.getMaterial(), glassInfo.getData());
        int blockFace = placePacket.getFace();

        if(blockFace != 255 && this.shouldCancelFakePlace(player.getItemInHand().getType())) {
            this.adjustPlaceLocation(location, blockFace);

            player.sendBlockChange(location, Material.AIR, (byte) 0);
            player.updateInventory();
        }

        return true;
    }

    private boolean shouldCancelFakePlace(Material material) {
        return material.isBlock() || material == Material.LAVA_BUCKET || material == Material.WATER_BUCKET;
    }

    private void adjustPlaceLocation(Location location, int blockFace) {
        switch(blockFace) {
            case 0: location.subtract(0, 1, 0); break;
            case 1: location.add(0, 1, 0); break;
            case 2: location.subtract(0, 0, 1); break;
            case 3: location.add(0, 0, 1); break;
            case 4: location.subtract(1, 0, 0); break;
            case 5: location.add(1, 0, 0); break;
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void setEntityTypesField(String fieldName, Object value1, Object value2) {
        try {
            Field field = EntityTypes.class.getDeclaredField(fieldName);
            field.setAccessible(true);

            Map map = (Map) field.get(null);
            map.put(value1, value2);
        } catch(NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Scoreboard newScoreboard() {
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();

        try {
            Constructor<?> constructor = CraftScoreboard.class.getDeclaredConstructor(net.minecraft.server.v1_8_R3.Scoreboard.class);
            constructor.setAccessible(true);

            return (Scoreboard) constructor.newInstance(new ScoreboardServer(server));
        } catch(NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return Bukkit.getScoreboardManager().getMainScoreboard();
    }

    private void fetchBukkitExecutor() {
        try {
            Field executorField = CraftScheduler.class.getDeclaredField("executor");
            executorField.setAccessible(true);

            this.bukkitExecutor = (Executor) executorField.get(Bukkit.getScheduler());
        } catch(ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean itemCausesDrops(ItemStack item, net.minecraft.server.v1_8_R3.Block nmsBlock) {
        Item itemType = item != null ? Item.getById(item.getTypeId()) : null;
        return nmsBlock != null && (nmsBlock.getMaterial().isAlwaysDestroyable() || itemType != null && itemType.canDestroySpecialBlock(nmsBlock));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.bukkitExecutor.execute(() -> this.injectPacketInterceptor(event.getPlayer()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Tasks.async(() -> this.deinjectPacketInterceptor(event.getPlayer()));
    }
}
