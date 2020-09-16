package me.qiooip.lazarus.utils.nms.packet;

import me.qiooip.lazarus.utils.ReflectionUtils;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

public class PacketPlayOutEntityEquipmentWrapper_1_8 {

    private static MethodHandle ENTITYID_GETTER;
    private static MethodHandle SLOT_GETTER;
    private static MethodHandle ITEMSTACK_GETTER;

    private static MethodHandle ENTITYID_SETTER;
    private static MethodHandle SLOT_SETTER;
    private static MethodHandle ITEMSTACK_SETTER;

    static {
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();

            ENTITYID_GETTER = lookup.unreflectGetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutEntityEquipment.class, "a"));
            SLOT_GETTER = lookup.unreflectGetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutEntityEquipment.class, "b"));
            ITEMSTACK_GETTER = lookup.unreflectGetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutEntityEquipment.class, "c"));

            ENTITYID_SETTER = lookup.unreflectSetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutEntityEquipment.class, "a"));
            SLOT_SETTER = lookup.unreflectSetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutEntityEquipment.class, "b"));
            ITEMSTACK_SETTER = lookup.unreflectSetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutEntityEquipment.class, "c"));

        } catch(Throwable t) {
            t.printStackTrace();
        }
    }

    public static PacketPlayOutEntityEquipment createEquipmentPacket(int entityId, int slot, ItemStack itemStack) {
        PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment();

        try {
            ENTITYID_SETTER.invokeExact(packet, entityId);
            SLOT_SETTER.invokeExact(packet, slot);
            ITEMSTACK_SETTER.invokeExact(packet, itemStack);
        } catch(Throwable t) {
            t.printStackTrace();
        }

        return packet;
    }
    
    public static PacketPlayOutEntityEquipment createEquipmentPacket(Player player, int slot, boolean remove) {
        PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment();
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        
        try {
            ENTITYID_SETTER.invokeExact(packet, player.getEntityId());
            SLOT_SETTER.invokeExact(packet, slot);
            ITEMSTACK_SETTER.invokeExact(packet, remove ? null : entityPlayer.inventory.armor[slot - 1]);
        } catch(Throwable t) {
            t.printStackTrace();
        }

        return packet;
    }

    public static int getEntityId(PacketPlayOutEntityEquipment packet) throws Throwable {
        return (int) ENTITYID_GETTER.invokeExact(packet);
    }

    public static int getSlot(PacketPlayOutEntityEquipment packet) throws Throwable {
        return (int) SLOT_GETTER.invokeExact(packet);
    }

    public static ItemStack getItemStack(PacketPlayOutEntityEquipment packet) throws Throwable {
        return (ItemStack) ITEMSTACK_GETTER.invokeExact(packet);
    }
}
