package me.qiooip.lazarus.tab.nms;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import me.qiooip.lazarus.scoreboard.base.ScoreboardBase_1_8;
import me.qiooip.lazarus.tab.PlayerTab;
import me.qiooip.lazarus.tab.TabManager;
import me.qiooip.lazarus.utils.Color;
import me.qiooip.lazarus.utils.Tasks;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public class PlayerTab_1_8 extends ScoreboardBase_1_8 implements PlayerTab {

    @Getter
    private int clientVersion;
    private GameProfile[] profiles;

    private String[] teamNames;
    private String[] contents;

    public PlayerTab_1_8(Player player) {
        super(player, NmsUtils.getInstance().getPlayerScoreboard(player));
        this.setup((CraftPlayer) player);
    }

    @Override
    public void unregister() {
        if(this.clientVersion < 47) return;

        for(GameProfile profile : this.profiles) {
            this.removePlayerInfo(profile);
        }
    }

    private void setup(CraftPlayer cplayer) {
        this.clientVersion = NmsUtils.getInstance().getClientVersion(cplayer);

        this.profiles = new GameProfile[80];
        this.teamNames = new String[60];

        this.contents = new String[80];

        if(this.clientVersion >= 47) {
            IntStream.rangeClosed(1, 80).forEach(i -> this.setupTabEntry(cplayer, i));
        } else {
            this.removePlayersFromTab(cplayer);

            IntStream.rangeClosed(1, 20).forEach(y -> IntStream.range(0, 3)
            .forEach(x -> this.setupTabEntry_1_7(cplayer, (x * 20) + y)));
        }
    }

    @Override
    public void set(int index, String line) {
        line = Color.translate(line);

        if(this.contents[index - 1] != null && this.contents[index - 1].equals(line)) return;

        if(this.clientVersion >= 47) {

            this.updateDisplayName(this.profiles[index - 1], line);

        } else {
            if(index > 60) return;

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
        }

        this.contents[index - 1] = line;
    }

    private void setupTabEntry(CraftPlayer cplayer, int index) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), this.getTeamName(index));
        this.profiles[index-1] = profile;

        profile.getProperties().removeAll("textures");
        profile.getProperties().put("textures", new Property("textures",
            TabManager.VALUE, TabManager.SIGNATURE));

        this.createPlayerInfo(profile);
    }

    private void setupTabEntry_1_7(CraftPlayer cplayer, int index) {
        String teamName = this.getTeamName(index);
        this.teamNames[index-1] = teamName;

        GameProfile profile = new GameProfile(UUID.randomUUID(), teamName);

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
        List<PacketPlayOutPlayerInfo> delayedPackets = new ArrayList<>();

        Bukkit.getOnlinePlayers().forEach(online -> {
            cplayer.getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(
                EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) online).getHandle()));

            delayedPackets.add(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER,
                ((CraftPlayer) online).getHandle()));
        });

        Tasks.asyncLater(() -> delayedPackets.forEach(cplayer.getHandle().playerConnection::sendPacket), 5L);
    }
}
