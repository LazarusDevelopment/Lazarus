package me.qiooip.lazarus.tab.reflection;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.Collections;

public class TabReflection_1_8 {

    public static class PacketPlayOutPlayerInfoWrapper {

        private static MethodHandle PLAYER_INFO_SETTER;
        private static MethodHandle ACTION_SETTER;

        static {
            try {
                MethodHandles.Lookup lookup = MethodHandles.lookup();

                PLAYER_INFO_SETTER = lookup.unreflectSetter(setAccessibleAndGet(PacketPlayOutPlayerInfo.class, "b"));
                ACTION_SETTER = lookup.unreflectSetter(setAccessibleAndGet(PacketPlayOutPlayerInfo.class, "a"));
            } catch(Throwable t) {
                t.printStackTrace();
            }
        }

        public static PacketPlayOutPlayerInfo newAddPacket(com.mojang.authlib.GameProfile gameProfile) {
            PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();

            try {
                PacketPlayOutPlayerInfo.PlayerInfoData infoData = packet.new PlayerInfoData(gameProfile, 0,
                EnumGamemode.NOT_SET, new ChatComponentText(gameProfile.getName()));

                PLAYER_INFO_SETTER.invokeExact(packet, Collections.singletonList(infoData));
                ACTION_SETTER.invokeExact(packet, EnumPlayerInfoAction.ADD_PLAYER);
            } catch(Throwable t) {
                t.printStackTrace();
            }

            return packet;
        }

        public static PacketPlayOutPlayerInfo newRemovePacket(GameProfile gameProfile) {
            PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();

            try {
                PacketPlayOutPlayerInfo.PlayerInfoData infoData = packet.new PlayerInfoData(gameProfile, 0,
                EnumGamemode.NOT_SET, new ChatComponentText(gameProfile.getName()));

                PLAYER_INFO_SETTER.invokeExact(packet, Collections.singletonList(infoData));
                ACTION_SETTER.invokeExact(packet, EnumPlayerInfoAction.REMOVE_PLAYER);
            } catch(Throwable t) {
                t.printStackTrace();
            }

            return packet;
        }

        public static PacketPlayOutPlayerInfo updateDisplayName(GameProfile profile, String displayName) {
            PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();

            try {
                PacketPlayOutPlayerInfo.PlayerInfoData infoData = packet.new PlayerInfoData(profile, 0,
                EnumGamemode.NOT_SET, new ChatComponentText(displayName));

                PLAYER_INFO_SETTER.invokeExact(packet, Collections.singletonList(infoData));
                ACTION_SETTER.invokeExact(packet, EnumPlayerInfoAction.UPDATE_DISPLAY_NAME);
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
