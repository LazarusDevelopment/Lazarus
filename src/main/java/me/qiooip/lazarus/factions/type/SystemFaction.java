package me.qiooip.lazarus.factions.type;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.utils.Color;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.StringJoiner;

@Getter
@Setter
@NoArgsConstructor
public class SystemFaction extends Faction {

    private String color;
    private boolean safezone;

    private boolean enderpearls;
    private boolean abilities;

    public SystemFaction(String name) {
        super(name);

        this.enderpearls = true;
        this.abilities = true;
        this.setColor(Color.translate("&b"));
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String value) {
        this.color = value;
    }

    @Override
    public String getName(CommandSender sender) {
        return this.getColor() + super.getName();
    }

    @Override
    public boolean isSafezone() {
        return this.safezone;
    }

    @Override
    public boolean areEnderpearlEnabled() {
        return this.enderpearls;
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
            : Language.FACTIONS_SYSTEM_CLAIM_FORMAT.replace("<claimLocation>", Language.NONE_PLACEHOLDER);

        Language.FACTIONS_SYSTEM_FACTION_SHOW.forEach(line -> sender.sendMessage(line
            .replace("<factionName>", this.getName(sender))
            .replace("<deathban>", this.getDeathbanString())
            .replace("<safezone>", Language.getTrueOrFalse(this.isSafezone()))
            .replace("<enderpearls>", Language.getEnabledOrDisabled(this.isEnderpearls()))
            .replace("<abilities>", Language.getEnabledOrDisabled(this.isAbilities()))
            .replace("<claims>", claimInfoString)));
    }
}
