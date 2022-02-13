package me.qiooip.lazarus.tab.nms;

import lombok.Getter;
import me.qiooip.lazarus.scoreboard.base.ScoreboardBase_1_7;
import me.qiooip.lazarus.tab.PlayerTab;
import me.qiooip.lazarus.tab.reflection.TabReflection_1_7;
import me.qiooip.lazarus.utils.Color;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerTab_1_7 extends ScoreboardBase_1_7 implements PlayerTab {

    @Getter
    private int clientVersion;

    private List<GameProfile> gameProfiles;

    private String[] teamNames;
    private String[] contents;

    public PlayerTab_1_7(Player player) {
        super(player, NmsUtils.getInstance().getPlayerScoreboard(player));
        this.setup((CraftPlayer) player);
    }

    @Override
    public void unregister() {
        if(this.clientVersion >= 47) {
            for(GameProfile gameProfile : this.gameProfiles) {
                this.removePlayerInfo(gameProfile);
            }
        }
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    private void setup(CraftPlayer cplayer) {
        this.clientVersion = NmsUtils.getInstance().getClientVersion(cplayer);

        this.teamNames = new String[80];
        this.contents = new String[80];

        if(this.clientVersion >= 47) {
            this.gameProfiles = new ArrayList<>();
            for(int i = 1; i <= 80; i++) {
                this.setupTabEntry(i);
            }
        } else {
            this.removePlayersFromTab(cplayer);

            for(int y = 1; y <= 20; y++) {
                for(int x = 0; x < 3; x++) {
                    this.setupTabEntry((x * 20) + y);
                }
            }
        }
    }

    @Override
    public void set(int index, String line) {
        line = Color.translate(line);

        int reduced = index - 1;

        if(this.contents[reduced] != null && this.contents[reduced].equals(line)) return;
        if(index > 60 && this.clientVersion < 47) return;

        Team team = this.getTeam(this.teamNames[reduced]);

        String prefix;
        String suffix;

        if(line.length() > 16) {
            int split = line.charAt(15) == ChatColor.COLOR_CHAR ? 15 : 16;

            prefix = line.substring(0, split);
            suffix = ChatColor.getLastColors(prefix) + line.substring(split);
        } else {
            prefix = line;
            suffix = "";
        }

        this.updateTeam(team.getName(), prefix, suffix.length() > 16 ? suffix.substring(0, 16) : suffix);
        this.contents[reduced] = line;
    }

    private void setupTabEntry(int index) {
        String teamName = this.getTeamName(index);
        this.teamNames[index - 1] = teamName;

        GameProfile profile = new GameProfile(UUID.randomUUID(), teamName);

        if(this.clientVersion >= 47) {
            this.gameProfiles.add(profile);

            profile.getProperties().removeAll("textures");
            profile.getProperties().put("textures", TabReflection_1_7.BLANK_SKIN);
        }

        this.createPlayerInfo(profile);
        this.getTeam(teamName).addEntry(teamName);
    }

    private Team getTeam(String name) {
        synchronized(this.scoreboard) {
            Team team = this.scoreboard.getTeam(name);
            return (team == null) ? this.scoreboard.registerNewTeam(name) : team;
        }
    }

    private String getTeamName(int index) {
        return Color.CHAT_COLOR_CACHE[index / 10].toString()
            + Color.CHAT_COLOR_CACHE[index % 10].toString()
            + ChatColor.RESET;
    }

    private void removePlayersFromTab(CraftPlayer cplayer) {
        for(Player online : Bukkit.getOnlinePlayers()) {
            NmsUtils.getInstance().sendPacket(cplayer, PacketPlayOutPlayerInfo.removePlayer(((CraftPlayer) online).getHandle()));
        }
    }
}
