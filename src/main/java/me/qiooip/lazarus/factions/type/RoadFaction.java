package me.qiooip.lazarus.factions.type;

import lombok.NoArgsConstructor;
import lombok.Setter;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.Color;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Setter
@NoArgsConstructor
public class RoadFaction extends SystemFaction {

    private transient String displayName;

    public RoadFaction(String name) {
        super(name);

        this.setColor(Color.translate("&c"));
        this.setupDisplayName();
    }

    public void setupDisplayName() {
        switch(this.getName()) {
            case "NorthRoad": this.displayName = "North Road"; break;
            case "EastRoad": this.displayName = "East Road"; break;
            case "WestRoad": this.displayName = "West Road"; break;
            case "SouthRoad": this.displayName = "South Road"; break;
        }
    }

    @Override
    public String getDisplayName(CommandSender sender) {
        return this.getColor() + this.displayName;
    }

    @Override
    public boolean shouldCancelPvpTimerEntrance(Player player) {
        return false;
    }

    @Override
    public void showInformation(CommandSender sender) {
        String claimInfoString = Language.FACTIONS_SYSTEM_CLAIM_FORMAT.replace("<claimLocation>", Language.NONE_PLACEHOLDER);

        Language.FACTIONS_SYSTEM_FACTION_SHOW.forEach(line -> sender.sendMessage(line
            .replace("<factionName>", this.getName(sender))
            .replace("<deathban>", this.getDeathbanString())
            .replace("<safezone>", String.valueOf(this.isSafezone()))
            .replace("<enderpearls>", String.valueOf(this.isEnderpearls()))
            .replace("<claims>", claimInfoString)));
    }
}
