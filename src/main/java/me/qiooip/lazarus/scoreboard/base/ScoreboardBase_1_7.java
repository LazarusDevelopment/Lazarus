package me.qiooip.lazarus.scoreboard.base;

import me.qiooip.lazarus.scoreboard.reflection.ScoreboardReflection_1_7;
import me.qiooip.lazarus.tab.reflection.TabReflection_1_7;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutScoreboardScore;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardBase_1_7 {

    protected Player player;

    protected final Scoreboard scoreboard;
    protected final net.minecraft.server.v1_7_R4.Scoreboard nmsScoreboard;

    public ScoreboardBase_1_7(Player player, Scoreboard scoreboard) {
        this.player = player;

        this.scoreboard = scoreboard;
        this.nmsScoreboard = ((CraftScoreboard) scoreboard).getHandle();
    }

    protected void updateTeam(String name, String prefix, String suffix) {
        this.sendPacket(ScoreboardReflection_1_7.PacketPlayOutScoreboardTeamWrapper.updateTeam(name, prefix, suffix));
    }

    protected void setScore(String entry, int value) {
        this.sendPacket(ScoreboardReflection_1_7.PacketPlayOutScoreboardScoreWrapper.createNewScore(entry , value));
    }

    protected void resetScore(String score) {
        this.sendPacket(new PacketPlayOutScoreboardScore(score));
    }

    protected void createPlayerInfo(GameProfile gameProfile) {
        this.sendPacket(TabReflection_1_7.PacketPlayOutPlayerInfoWrapper.newAddPacket(gameProfile));
    }

    protected void removePlayerInfo(GameProfile gameProfile) {
        this.sendPacket(TabReflection_1_7.PacketPlayOutPlayerInfoWrapper.newRemovePacket(gameProfile));
    }

    private void sendPacket(Packet packet) {
        if(this.player == null) return;

        ((CraftPlayer) this.player).getHandle().playerConnection.sendPacket(packet);
    }
}
