package me.qiooip.lazarus.hologram.reflection;

import me.qiooip.lazarus.utils.ReflectionUtils;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Location;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

public class HologramReflection_1_8 {

    public static class PacketPlayOutEntityTeleportWrapper {

        private static MethodHandle ENTITY_ID_SETTER;
        private static MethodHandle LOCATION_X_SETTER;
        private static MethodHandle LOCATION_Y_SETTER;
        private static MethodHandle LOCATION_Z_SETTER;
        private static MethodHandle ON_GROUND_SETTER;

        static {
            try {
                MethodHandles.Lookup lookup = MethodHandles.lookup();

                ENTITY_ID_SETTER = lookup.unreflectSetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutEntityTeleport.class, "a"));
                LOCATION_X_SETTER = lookup.unreflectSetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutEntityTeleport.class, "b"));
                LOCATION_Y_SETTER = lookup.unreflectSetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutEntityTeleport.class, "c"));
                LOCATION_Z_SETTER = lookup.unreflectSetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutEntityTeleport.class, "d"));
                ON_GROUND_SETTER = lookup.unreflectSetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutEntityTeleport.class, "g"));
            } catch(Throwable t) {
                t.printStackTrace();
            }
        }

        public static PacketPlayOutEntityTeleport newTeleportPacket(int entityId, Location location) {
            PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport();

            try {
                ENTITY_ID_SETTER.invokeExact(packet, entityId);
                LOCATION_X_SETTER.invokeExact(packet, MathHelper.floor(location.getX() * 32.0));
                LOCATION_Y_SETTER.invokeExact(packet, MathHelper.floor((location.getY()) * 32.0));
                LOCATION_Z_SETTER.invokeExact(packet, MathHelper.floor(location.getZ() * 32.0));
                ON_GROUND_SETTER.invokeExact(packet, true);
            } catch(Throwable t) {
                t.printStackTrace();
            }

            return packet;
        }
    }

    public static class PacketPlayOutSpawnEntityLivingWrapper {

        private static MethodHandle ENTITY_ID_SETTER;
        private static MethodHandle ENTITY_TYPE_SETTER;
        private static MethodHandle LOCATION_X_SETTER;
        private static MethodHandle LOCATION_Y_SETTER;
        private static MethodHandle LOCATION_Z_SETTER;
        private static MethodHandle DATA_WATCHER_SETTER;

        static {
            try {
                MethodHandles.Lookup lookup = MethodHandles.lookup();

                ENTITY_ID_SETTER = lookup.unreflectSetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutSpawnEntityLiving.class, "a"));
                ENTITY_TYPE_SETTER = lookup.unreflectSetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutSpawnEntityLiving.class, "b"));
                LOCATION_X_SETTER = lookup.unreflectSetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutSpawnEntityLiving.class, "c"));
                LOCATION_Y_SETTER = lookup.unreflectSetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutSpawnEntityLiving.class, "d"));
                LOCATION_Z_SETTER = lookup.unreflectSetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutSpawnEntityLiving.class, "e"));
                DATA_WATCHER_SETTER = lookup.unreflectSetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutSpawnEntityLiving.class, "l"));
            } catch(Throwable t) {
                t.printStackTrace();
            }
        }

        public static PacketPlayOutSpawnEntityLiving newEntitySpawnPacket(int entityId, Location location, DataWatcher watcher) {
            PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving();

            try {
                ENTITY_ID_SETTER.invokeExact(packet, entityId);
                ENTITY_TYPE_SETTER.invokeExact(packet, 30);
                LOCATION_X_SETTER.invokeExact(packet, MathHelper.floor(location.getX() * 32.0));
                LOCATION_Y_SETTER.invokeExact(packet, MathHelper.floor((location.getY()) * 32.0));
                LOCATION_Z_SETTER.invokeExact(packet, MathHelper.floor(location.getZ() * 32.0));
                DATA_WATCHER_SETTER.invokeExact(packet, watcher);
            } catch(Throwable t) {
                t.printStackTrace();
            }

            return packet;
        }
    }
}
