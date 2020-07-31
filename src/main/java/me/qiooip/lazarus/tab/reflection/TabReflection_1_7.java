package me.qiooip.lazarus.tab.reflection;

import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;
import net.minecraft.util.com.mojang.authlib.GameProfile;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

public class TabReflection_1_7 {

    public static class PacketPlayOutPlayerInfoWrapper {

        private static MethodHandle PLAYER_SETTER;
        private static MethodHandle USERNAME_SETTER;
        private static MethodHandle ACTION_SETTER;

        static {
            try {
                MethodHandles.Lookup lookup = MethodHandles.lookup();

                PLAYER_SETTER = lookup.unreflectSetter(setAccessibleAndGet(PacketPlayOutPlayerInfo.class, "player"));
                USERNAME_SETTER = lookup.unreflectSetter(setAccessibleAndGet(PacketPlayOutPlayerInfo.class, "username"));
                ACTION_SETTER = lookup.unreflectSetter(setAccessibleAndGet(PacketPlayOutPlayerInfo.class, "action"));
            } catch(Throwable t) {
                t.printStackTrace();
            }
        }

        public static PacketPlayOutPlayerInfo newAddPacket(GameProfile gameProfile) {
            PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();

            try {
                PLAYER_SETTER.invokeExact(packet, gameProfile);
                USERNAME_SETTER.invokeExact(packet, gameProfile.getName());
                ACTION_SETTER.invokeExact(packet, 0);
            } catch(Throwable t) {
                t.printStackTrace();
            }

            return packet;
        }

        public static PacketPlayOutPlayerInfo newRemovePacket(GameProfile gameProfile) {
            PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();

            try {
                PLAYER_SETTER.invokeExact(packet, gameProfile);
                USERNAME_SETTER.invokeExact(packet, gameProfile.getName());
                ACTION_SETTER.invokeExact(packet, 4);
            } catch(Throwable t) {
                t.printStackTrace();
            }

            return packet;
        }
    }

    private static Field setAccessibleAndGet(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);

        return field;
    }
}
