package me.qiooip.lazarus.factions.type;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.StringJoiner;

@Getter
@Setter
@NoArgsConstructor
public class SystemFaction extends Faction {

    private ChatColor color;
    private boolean safezone;
    private boolean enderpearls;

    public SystemFaction(String name) {
        super(name);

        this.color = ChatColor.AQUA;
        this.enderpearls = true;
    }

    @Override
    public String getName(CommandSender sender) {
        return this.color + super.getName();
    }

    @Override
    public boolean isSafezone() {
        return this.safezone;
    }

    @Override
    public boolean shouldCancelPvpTimerEntrance(Player player) {
        return !this.isSafezone();
    }

    @Override
    public void showInformation(CommandSender sender) {
        StringJoiner claimInfo = new StringJoiner("\n");

        this.getClaims().forEach(claim -> {
            String locationString = StringUtils.getLocationNameWithWorldWithoutY(claim.getCenter());
            claimInfo.add(Language.FACTIONS_SYSTEM_CLAIM_FORMAT.replace("<claimLocation>", locationString));
        });

        String claimInfoString = claimInfo.length() != 0
            ? claimInfo.toString()
            : Language.FACTIONS_SYSTEM_CLAIM_FORMAT.replace("<claimLocation>", "None");

        Language.FACTIONS_SYSTEM_FACTION_SHOW.forEach(line -> sender.sendMessage(line
            .replace("<factionName>", this.getName(sender))
            .replace("<claims>", claimInfoString)));
    }
}
