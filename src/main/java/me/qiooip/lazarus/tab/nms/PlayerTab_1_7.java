package me.qiooip.lazarus.tab.nms;

import lombok.Getter;
import me.qiooip.lazarus.scoreboard.base.ScoreboardBase_1_7;
import me.qiooip.lazarus.tab.PlayerTab;
import me.qiooip.lazarus.tab.TabManager;
import me.qiooip.lazarus.utils.Color;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.minecraft.util.com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

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
            this.gameProfiles.forEach(profile -> this.removePlayerInfo(profile));
        }
    }

    private void setup(CraftPlayer cplayer) {
        this.clientVersion = cplayer.getHandle().playerConnection.networkManager.getVersion();

        this.teamNames = new String[80];
        this.contents = new String[80];

        if(this.clientVersion >= 47) {
            this.gameProfiles = new ArrayList<>();
            IntStream.rangeClosed(1, 80).forEach(i -> this.setupTabEntry(cplayer, i));
        } else {
            this.removePlayersFromTab(cplayer);

            IntStream.rangeClosed(1, 20).forEach(y -> IntStream.range(0, 3)
            .forEach(x -> this.setupTabEntry(cplayer, (x * 20) + y)));
        }
    }

    @Override
    public void set(int index, String line) {
        line = Color.translate(line);

        if(this.contents[index-1] != null && this.contents[index-1].equals(line)) return;
        if(index > 60 && this.clientVersion < 47) return;

        Team team = this.getTeam(this.teamNames[index-1]);

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
        this.contents[index-1] = line;
    }

    private void setupTabEntry(CraftPlayer cplayer, int index) {
        String teamName = this.getTeamName(index);
        this.teamNames[index-1] = teamName;

        GameProfile profile = new GameProfile(UUID.randomUUID(), teamName);

        if(this.clientVersion >= 47) {
            this.gameProfiles.add(profile);

            profile.getProperties().removeAll("textures");
            profile.getProperties().put("textures", new Property("textures",
                TabManager.VALUE, TabManager.SIGNATURE));
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
        return ChatColor.values()[index / 10].toString()
            + ChatColor.values()[index % 10].toString()
            + ChatColor.RESET.toString();
    }

    private void removePlayersFromTab(CraftPlayer cplayer) {
        Bukkit.getOnlinePlayers().forEach(online -> {
            PacketPlayOutPlayerInfo removePacket = PacketPlayOutPlayerInfo
            .removePlayer(((CraftPlayer) online).getHandle());

            cplayer.getHandle().playerConnection.sendPacket(removePacket);
        });
    }
}
