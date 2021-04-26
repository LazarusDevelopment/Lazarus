package me.qiooip.lazarus.factions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.qiooip.lazarus.factions.enums.ChatType;
import me.qiooip.lazarus.factions.enums.Role;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class FactionPlayer {

    private UUID uuid;

    private UUID factionId;
    private Role role;
    private ChatType chatType;

    public FactionPlayer(UUID uuid, PlayerFaction faction) {
        this.uuid = uuid;

        this.factionId = faction.getId();
        this.chatType = ChatType.PUBLIC;
        this.role = Role.MEMBER;
    }

    public PlayerFaction getFaction() {
        return FactionsManager.getInstance().getPlayerFactionByUuid(this.factionId);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public boolean isOnline() {
        return this.getPlayer() != null;
    }

    public String getName() {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(this.uuid);
        return offlinePlayer.hasPlayedBefore() || offlinePlayer.isOnline() ? offlinePlayer.getName() : null;
    }

    public void sendMessage(String message) {
        Player player = this.getPlayer();

        if(player != null) {
            player.sendMessage(message);
        }
    }
}
