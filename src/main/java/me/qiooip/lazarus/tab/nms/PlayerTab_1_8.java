package me.qiooip.lazarus.tab.nms;

import com.mojang.authlib.GameProfile;
import lombok.Getter;
import me.qiooip.lazarus.scoreboard.base.ScoreboardBase_1_8;
import me.qiooip.lazarus.tab.PlayerTab;
import me.qiooip.lazarus.tab.reflection.TabReflection_1_8;
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

    @Override
    public Player getPlayer() {
        return this.player;
    }

    private void setup(CraftPlayer cplayer) {
        this.clientVersion = NmsUtils.getInstance().getClientVersion(cplayer);

        this.profiles = new GameProfile[80];
        this.teamNames = new String[60];

        this.contents = new String[80];

        if(this.clientVersion >= 47) {
            for(int i = 1; i <= 80; i++) {
                this.setupTabEntry(i);
            }
        } else {
            this.removePlayersFromTab(cplayer);

            for(int y = 1; y <= 20; y++) {
                for(int x = 0; x < 3; x++) {
                    this.setupTabEntry_1_7((x * 20) + y);
                }
            }
        }
    }

    @Override
    public void set(int index, String line) {
        line = Color.translate(line);

        int reduced = index - 1;

        if(this.contents[reduced] != null && this.contents[reduced].equals(line)) return;

        if(this.clientVersion >= 47) {
            this.updateDisplayName(this.profiles[reduced], line);
        } else {
            if(index > 60) return;

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
        }

        this.contents[reduced] = line;
    }

    private void setupTabEntry(int index) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), this.getTeamName(index));
        this.profiles[index - 1] = profile;

        profile.getProperties().removeAll("textures");
        profile.getProperties().put("textures", TabReflection_1_8.BLANK_SKIN);

        this.createPlayerInfo(profile);
    }

    private void setupTabEntry_1_7(int index) {
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
        return Color.CHAT_COLOR_CACHE[index / 10].toString()
            + Color.CHAT_COLOR_CACHE[index % 10].toString()
            + ChatColor.RESET;
    }

    private void removePlayersFromTab(CraftPlayer cplayer) {
        List<PacketPlayOutPlayerInfo> delayedPackets = new ArrayList<>();

        for(Player online : Bukkit.getOnlinePlayers()) {
            NmsUtils.getInstance().sendPacket(cplayer, new PacketPlayOutPlayerInfo(
                    EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) online).getHandle()));

            delayedPackets.add(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER,
                    ((CraftPlayer) online).getHandle()));
        }

        Tasks.asyncLater(() -> {
            for(PacketPlayOutPlayerInfo delayedPacket : delayedPackets) {
                NmsUtils.getInstance().sendPacket(cplayer, delayedPacket);
            }
        }, 5L);
    }
}
