package me.qiooip.lazarus.factions.event;

import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.claim.Claim;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;

import java.util.List;

@Getter
public class FactionClaimChangeEvent extends FactionEvent implements Cancellable {

    private final Faction faction;
    private final List<Claim> claims;
    private final ClaimChangeReason claimChangeReason;
    @Setter private boolean cancelled;

    public FactionClaimChangeEvent(Faction faction, List<Claim> claims, ClaimChangeReason claimChangeReason) {
        this.faction = faction;
        this.claims = claims;
        this.claimChangeReason = claimChangeReason;

        Bukkit.getPluginManager().callEvent(this);
    }

    public enum ClaimChangeReason {
        CLAIM, UNCLAIM, UNCLAIM_ALL, DISBAND
    }
}
