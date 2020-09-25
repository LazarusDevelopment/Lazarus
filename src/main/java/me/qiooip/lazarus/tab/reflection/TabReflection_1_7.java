package me.qiooip.lazarus.tab.reflection;

import me.qiooip.lazarus.tab.TabManager;
import me.qiooip.lazarus.utils.ReflectionUtils;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.minecraft.util.com.mojang.authlib.properties.Property;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

public class TabReflection_1_7 {

    public static final Property BLANK_SKIN = new Property("textures", TabManager.VALUE, TabManager.SIGNATURE);

    public static class PacketPlayOutPlayerInfoWrapper {

        private static MethodHandle PLAYER_SETTER;
        private static MethodHandle USERNAME_SETTER;
        private static MethodHandle ACTION_SETTER;

        static {
            try {
                MethodHandles.Lookup lookup = MethodHandles.lookup();

                PLAYER_SETTER = lookup.unreflectSetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutPlayerInfo.class, "player"));
                USERNAME_SETTER = lookup.unreflectSetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutPlayerInfo.class, "username"));
                ACTION_SETTER = lookup.unreflectSetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutPlayerInfo.class, "action"));
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
}
