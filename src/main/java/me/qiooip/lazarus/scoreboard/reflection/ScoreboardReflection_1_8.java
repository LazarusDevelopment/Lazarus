package me.qiooip.lazarus.scoreboard.reflection;

import me.qiooip.lazarus.utils.ReflectionUtils;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardScore.EnumScoreboardAction;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

public class ScoreboardReflection_1_8 {

    public static class PacketPlayOutScoreboardTeamWrapper {

        private static MethodHandle NAME_SETTER;
        private static MethodHandle DISPLAY_NAME_SETTER;
        private static MethodHandle PREFIX_SETTER;
        private static MethodHandle SUFFIX_SETTER;
        private static MethodHandle ACTION_SETTER;

        static {
            try {
                MethodHandles.Lookup lookup = MethodHandles.lookup();

                NAME_SETTER = lookup.unreflectSetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutScoreboardTeam.class, "a"));
                DISPLAY_NAME_SETTER = lookup.unreflectSetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutScoreboardTeam.class, "b"));
                PREFIX_SETTER = lookup.unreflectSetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutScoreboardTeam.class, "c"));
                SUFFIX_SETTER = lookup.unreflectSetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutScoreboardTeam.class, "d"));
                ACTION_SETTER = lookup.unreflectSetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutScoreboardTeam.class, "h"));
            } catch(Throwable t) {
                t.printStackTrace();
            }
        }

        public static PacketPlayOutScoreboardTeam updateTeam(String name, String prefix, String suffix) {
            PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();

            try {
                NAME_SETTER.invokeExact(packet, name);
                DISPLAY_NAME_SETTER.invokeExact(packet, name);
                PREFIX_SETTER.invokeExact(packet, prefix);
                SUFFIX_SETTER.invokeExact(packet, suffix);
                ACTION_SETTER.invokeExact(packet, 2);
            } catch(Throwable t) {
                t.printStackTrace();
            }

            return packet;
        }
    }

    public static class PacketPlayOutScoreboardScoreWrapper {

        private static MethodHandle ENTRY_SETTER;
        private static MethodHandle OBJECTIVE_SETTER;
        private static MethodHandle VALUE_SETTER;
        private static MethodHandle ACTION_SETTER;

        static {
            try {
                MethodHandles.Lookup lookup = MethodHandles.lookup();

                ENTRY_SETTER = lookup.unreflectSetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutScoreboardScore.class, "a"));
                OBJECTIVE_SETTER = lookup.unreflectSetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutScoreboardScore.class, "b"));
                VALUE_SETTER = lookup.unreflectSetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutScoreboardScore.class, "c"));
                ACTION_SETTER = lookup.unreflectSetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutScoreboardScore.class, "d"));
            } catch(Throwable t) {
                t.printStackTrace();
            }
        }

        public static PacketPlayOutScoreboardScore createNewScore(String entry, int value) {
            PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore();

            try {
                ENTRY_SETTER.invokeExact(packet, entry);
                OBJECTIVE_SETTER.invokeExact(packet, "lazarus");
                VALUE_SETTER.invokeExact(packet, value);
                ACTION_SETTER.invokeExact(packet, EnumScoreboardAction.CHANGE);
            } catch(Throwable t) {
                t.printStackTrace();
            }

            return packet;
        }
    }
}
