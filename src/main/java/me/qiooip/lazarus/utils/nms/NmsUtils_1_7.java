package me.qiooip.lazarus.utils.nms;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.abilities.AbilitiesManager;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.abilities.reflection.AbilitiesReflection_1_7;
import me.qiooip.lazarus.abilities.type.InvisibilityAbility;
import me.qiooip.lazarus.games.dragon.EnderDragon;
import me.qiooip.lazarus.games.dragon.nms.EnderDragon_1_7;
import me.qiooip.lazarus.games.loot.LootData;
import me.qiooip.lazarus.glass.GlassInfo;
import me.qiooip.lazarus.handlers.logger.CombatLogger;
import me.qiooip.lazarus.handlers.logger.nms.CombatLogger_1_7;
import me.qiooip.lazarus.scoreboard.PlayerScoreboard;
import me.qiooip.lazarus.scoreboard.nms.PlayerScoreboard_1_7;
import me.qiooip.lazarus.tab.PlayerTab;
import me.qiooip.lazarus.tab.nms.PlayerTab_1_7;
import me.qiooip.lazarus.utils.Tasks;
import me.qiooip.lazarus.utils.item.ItemBuilder;
import me.qiooip.lazarus.utils.item.ItemUtils;
import net.minecraft.server.v1_7_R4.BlockCocoa;
import net.minecraft.server.v1_7_R4.Blocks;
import net.minecraft.server.v1_7_R4.EntityLightning;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.EntityTypes;
import net.minecraft.server.v1_7_R4.GameProfileSerializer;
import net.minecraft.server.v1_7_R4.Item;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.minecraft.server.v1_7_R4.MobEffect;
import net.minecraft.server.v1_7_R4.MobEffectList;
import net.minecraft.server.v1_7_R4.NBTTagCompound;
import net.minecraft.server.v1_7_R4.NetworkManager;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInBlockDig;
import net.minecraft.server.v1_7_R4.PacketPlayInBlockPlace;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_7_R4.PacketPlayOutNamedSoundEffect;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntityWeather;
import net.minecraft.server.v1_7_R4.PlayerConnection;
import net.minecraft.server.v1_7_R4.PlayerList;
import net.minecraft.server.v1_7_R4.ScoreboardServer;
import net.minecraft.server.v1_7_R4.TileEntityBrewingStand;
import net.minecraft.server.v1_7_R4.TileEntitySkull;
import net.minecraft.util.io.netty.channel.Channel;
import net.minecraft.util.io.netty.channel.ChannelDuplexHandler;
import net.minecraft.util.io.netty.channel.ChannelHandlerContext;
import net.minecraft.util.io.netty.channel.ChannelPromise;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandMap;
import org.bukkit.craftbukkit.v1_7_R4.CraftChunk;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.CraftStatistic;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_7_R4.scheduler.CraftScheduler;
import org.bukkit.craftbukkit.v1_7_R4.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.v1_7_R4.util.CraftMagicNumbers;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadLocalRandom;

public class NmsUtils_1_7 extends NmsUtils implements Listener {

    private final Set<Material> clickableItems;
    private final Set<Material> kitmapClickables;
    private final Set<Material> exoticBoneClickables;

    public NmsUtils_1_7() {
        this.fetchBukkitExecutor();

        this.clickableItems = EnumSet.of(Material.ANVIL, Material.BEACON, Material.FIRE,
            Material.FENCE_GATE, Material.WOOD_BUTTON, Material.WOODEN_DOOR, Material.IRON_DOOR_BLOCK,
            Material.TRAPPED_CHEST, Material.FURNACE, Material.BURNING_FURNACE, Material.BREWING_STAND,
            Material.HOPPER, Material.DROPPER, Material.DISPENSER, Material.STONE_BUTTON, Material.BED_BLOCK,
            Material.ENCHANTMENT_TABLE, Material.LEVER, Material.TRAP_DOOR, Material.CHEST, Material.DIODE_BLOCK_ON,
            Material.DIODE_BLOCK_OFF, Material.REDSTONE_COMPARATOR_ON, Material.REDSTONE_COMPARATOR_OFF,
            Material.JUKEBOX, Material.WORKBENCH, Material.ENDER_CHEST);

        this.kitmapClickables = EnumSet.of(Material.LEVER, Material.WOOD_BUTTON, Material.STONE_BUTTON,
            Material.WOOD_PLATE, Material.STONE_PLATE, Material.IRON_PLATE, Material.GOLD_PLATE,
            Material.WORKBENCH, Material.FURNACE, Material.BURNING_FURNACE, Material.FENCE_GATE);

        this.exoticBoneClickables = EnumSet.of(Material.CHEST, Material.TRAPPED_CHEST, Material.HOPPER,
                Material.DROPPER, Material.DISPENSER, Material.TRAP_DOOR, Material.WORKBENCH,
                Material.ENDER_CHEST, Material.WOODEN_DOOR, Material.FENCE_GATE);

        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());

        Tasks.asyncLater(() -> Bukkit.getOnlinePlayers().forEach(this::injectPacketInterceptor), 20L);
    }

    @Override
    public void disable() {
        Bukkit.getOnlinePlayers().forEach(this::deinjectPacketInterceptor);
    }

    @Override
    public boolean isSpigot18() {
        return false;
    }

    @Override
    public Thread getMainThread() {
        return MinecraftServer.getServer().primaryThread;
    }

    @Override
    public Executor getBukkitExecutor() {
        return this.bukkitExecutor;
    }

    @Override
    public boolean isCustomSpigot() throws NoSuchFieldException {
        Field customField = TileEntityBrewingStand.class.getDeclaredField("brewSpeedMultiplier");
        customField.setAccessible(true);

        return Modifier.isStatic(customField.getModifiers());
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
    public Set<Material> getKitmapClickables() {
        return this.kitmapClickables;
    }

    @Override
    public Set<Material> getExoticBoneClickables() {
        return this.exoticBoneClickables;
    }

    @Override
    public void registerCombatLogger() {
        this.setEntityTypesField("d", CombatLogger_1_7.class, "CombatLogger");
        this.setEntityTypesField("f", CombatLogger_1_7.class, 51);
    }

    @Override
    public void registerEnderDragon() {
        this.setEntityTypesField("d", EnderDragon_1_7.class, "CustomDragon");
        this.setEntityTypesField("f", EnderDragon_1_7.class, 63);
    }

    @Override
    public boolean isCombatLogger(Entity entity) {
        return ((CraftEntity) entity).getHandle() instanceof CombatLogger_1_7;
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
        return ((CraftPlayer) player).getHandle().playerConnection.networkManager.getVersion();
    }

    @Override
    public int getPing(Player player) {
        return ((CraftPlayer) player).getHandle().ping;
    }

    @Override
    public PlayerScoreboard getNewPlayerScoreboard(Player player) {
        return new PlayerScoreboard_1_7(player);
    }

    @Override
    public PlayerTab getNewPlayerTab(Player player) {
        return new PlayerTab_1_7(player);
    }

    @Override
    public void increaseStatistic(Player player, Statistic statistic, Material material) {
        net.minecraft.server.v1_7_R4.Statistic nmsStatistic = CraftStatistic.getMaterialStatistic(statistic, material);

        if(nmsStatistic == null) {
            return;
        }

        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        handle.getStatisticManager().b(handle, nmsStatistic, 1);
    }

    @Override
    public void decreaseStatistic(Player player, Statistic statistic, Material material) {
        net.minecraft.server.v1_7_R4.Statistic nmsStatistic = CraftStatistic.getMaterialStatistic(statistic, material);

        if(nmsStatistic == null) {
            return;
        }

        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        handle.getStatisticManager().b(handle, nmsStatistic, -1);
    }

    @Override
    public boolean isInvulnerable(Player player) {
        return ((CraftPlayer) player).getHandle().isInvulnerable();
    }

    @Override
    public void toggleInvulnerable(Player player) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        Class<?> superclass = entityPlayer.getClass().getSuperclass();

        do {
            superclass = superclass.getSuperclass();
        } while(superclass != net.minecraft.server.v1_7_R4.Entity.class);

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
        ItemBuilder itemBuilder = new ItemBuilder(Material.MOB_SPAWNER, 1, durability);

        if(name != null) {
            itemBuilder.setName(name);
        }

        return itemBuilder.build();
    }

    @Override
    public PotionEffect getPotionEffect(Player player, PotionEffectType type) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        MobEffect nmsEffect = entityPlayer.getEffect(MobEffectList.byId[type.getId()]);

        return new PotionEffect(PotionEffectType.getById(nmsEffect.getEffectId()),
        nmsEffect.getDuration(), nmsEffect.getAmplifier(), nmsEffect.isAmbient());
    }

    @Override
    public void addPotionEffect(Player player, PotionEffect effect) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        MobEffect mobEffect = new MobEffect(effect.getType().getId(), effect.getDuration(), effect.getAmplifier());

        entityPlayer.removeEffect(mobEffect.getEffectId());

        if(Thread.currentThread() == this.getMainThread()) {
            entityPlayer.addEffect(mobEffect);
        } else {
            Tasks.sync(() -> entityPlayer.addEffect(mobEffect));
        }
    }

    @Override
    public void removeInfinitePotionEffect(Player player, PotionEffect effect) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        MobEffect nmsEffect = entityPlayer.getEffect(MobEffectList.byId[effect.getType().getId()]);

        if(nmsEffect == null) return;
        if(effect.getAmplifier() != nmsEffect.getAmplifier() || nmsEffect.getDuration() < 12000) return;

        entityPlayer.removeEffect(nmsEffect.getEffectId());
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

        net.minecraft.server.v1_7_R4.Block nmsBlock = CraftMagicNumbers.getBlock(block);

        if(nmsBlock == Blocks.AIR || !this.itemCausesDrops(itemInHand, nmsBlock)) {
            return drops;
        }

        if(itemInHand != null && itemInHand.containsEnchantment(Enchantment.SILK_TOUCH)) {
            drops.add(new ItemStack(block.getType(), 1, block.getData()));
            return drops;
        }

        net.minecraft.server.v1_7_R4.Chunk nmsChunk = ((CraftChunk) block.getChunk()).getHandle();

        int fortuneLevel = itemInHand == null ? 0 : itemInHand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);

        int count = nmsBlock.getDropCount(fortuneLevel, nmsChunk.world.random);
        byte data = block.getData();

        for(int i = 0; i < count; i++) {
            Item item = nmsBlock.getDropType(data, nmsChunk.world.random, fortuneLevel);

            if(nmsBlock == Blocks.SKULL) {
                int dropData = nmsBlock.getDropData(nmsChunk.world, block.getX(), block.getY(), block.getZ());
                net.minecraft.server.v1_7_R4.ItemStack nmsStack = new net.minecraft.server.v1_7_R4.ItemStack(item, 1, dropData);
                TileEntitySkull tileentityskull = (TileEntitySkull) nmsChunk.world.getTileEntity(block.getX(), block.getY(), block.getZ());

                if(tileentityskull.getSkullType() == 3 && tileentityskull.getGameProfile() != null) {
                    nmsStack.setTag(new NBTTagCompound());
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    GameProfileSerializer.serialize(nbttagcompound, tileentityskull.getGameProfile());
                    nmsStack.getTag().set("SkullOwner", nbttagcompound);
                }

                drops.add(CraftItemStack.asBukkitCopy(nmsStack));
            } else if(nmsBlock == Blocks.COCOA) {
                int dropAmount = BlockCocoa.c(data) >= 2 ? 3 : 1;

                for(int j = 0; j < dropAmount; ++j) {
                    drops.add(new ItemStack(Material.INK_SACK, 1, (short)3));
                }
            } else {
                drops.add(new ItemStack(CraftMagicNumbers.getMaterial(item), 1, (short) nmsBlock.getDropData(data)));
            }
        }

        return drops;
    }

    @Override
    public void damageItemInHand(Player player) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

        net.minecraft.server.v1_7_R4.ItemStack itemInHand = entityPlayer.inventory.getItemInHand();

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
        try {
            Method method = Bukkit.getServer().getClass().getDeclaredMethod("setViewDistance", int.class);
            method.setAccessible(true);

            method.invoke(Bukkit.getServer(), amount);
        } catch(ReflectiveOperationException e) {
            ((CraftServer) Bukkit.getServer()).getServer().getPropertyManager().setProperty("view-distance", amount);
        }
    }

    @Override
    public CombatLogger spawnCombatLogger(World world, Player player) {
        return new CombatLogger_1_7(world, player);
    }

    @Override
    public EnderDragon spawnEnderDragon(Location location, LootData loot) {
        return new EnderDragon_1_7(location, loot);
    }

    @Override
    public void injectPacketInterceptor(Player player) {
        if(!player.isOnline()) return;

        CraftPlayer cplayer = (CraftPlayer) player;

        Channel channel = this.getChannel(cplayer);
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

        InvisibilityAbility ability = (InvisibilityAbility) AbilitiesManager.getInstance().getAbilityItemByType(AbilityType.INVISIBILITY);

        if(ability != null) {
            ability.hidePlayers(player);
        }
    }

    @Override
    public void deinjectPacketInterceptor(Player player) {
        CraftPlayer cplayer = (CraftPlayer) player;

        Channel channel = this.getChannel(cplayer);
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
            PacketPlayOutEntityEquipment equipment = AbilitiesReflection_1_7.createEquipmentPacket(player, slot, remove);
            packets.add(equipment);
        }

        return packets;
    }

    @Override
    public void sendPacket(Player player, Object packet) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket((Packet) packet);
    }

    private PacketPlayOutEntityEquipment handlePlayOutEntityEquipmentPacket(Player player, PacketPlayOutEntityEquipment equipmentPacket) {
        InvisibilityAbility ability = (InvisibilityAbility) AbilitiesManager.getInstance().getAbilityItemByType(AbilityType.INVISIBILITY);

        if(ability != null) {

            try {
                int entityId = AbilitiesReflection_1_7.getEntityId(equipmentPacket);
                net.minecraft.server.v1_7_R4.Entity sender = ((CraftPlayer) player).getHandle().world.getEntity(entityId);

                if(sender instanceof EntityPlayer && ability.getPlayers().contains(sender.getUniqueID())) {
                    int slot = AbilitiesReflection_1_7.getSlot(equipmentPacket);
                    net.minecraft.server.v1_7_R4.ItemStack itemStack = AbilitiesReflection_1_7.getItemStack(equipmentPacket);

                    // Make sure we only cancel the armor packets
                    if(itemStack != null && slot != 0) {
                        return null;
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        return equipmentPacket;
    }

    private boolean handlePlayInBlockDigPacket(Player player, PacketPlayInBlockDig digPacket) {
        if(digPacket.g() != 0 && digPacket.g() != 2) {
            return false;
        }

        Location location = new Location(player.getWorld(), digPacket.c(), digPacket.d(), digPacket.e());
        GlassInfo glassInfo = Lazarus.getInstance().getGlassManager().getGlassAt(player, location);

        if(glassInfo != null) {
            player.sendBlockChange(location, glassInfo.getMaterial(), glassInfo.getData());
            return true;
        }

        return false;
    }

    private boolean handlePlayInBlockPlacePacket(Player player, PacketPlayInBlockPlace placePacket) {
        Location location = new Location(player.getWorld(), placePacket.c(), placePacket.d(), placePacket.e());
        GlassInfo glassInfo = Lazarus.getInstance().getGlassManager().getGlassAt(player, location);

        if(glassInfo != null) {
            player.sendBlockChange(location, glassInfo.getMaterial(), glassInfo.getData());
            return true;
        }

        return false;
    }

    private Channel getChannel(CraftPlayer cplayer) {
        NetworkManager networkManager = cplayer.getHandle().playerConnection.networkManager;

        try {
            Field channelField = networkManager.getClass().getDeclaredField("m");
            channelField.setAccessible(true);

            return (Channel) channelField.get(networkManager);
        } catch(NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
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
            Constructor<?> constructor = CraftScoreboard.class.getDeclaredConstructor(net.minecraft.server.v1_7_R4.Scoreboard.class);
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

    private boolean itemCausesDrops(ItemStack item, net.minecraft.server.v1_7_R4.Block nmsBlock) {
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
