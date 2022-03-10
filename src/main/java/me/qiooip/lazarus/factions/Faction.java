package me.qiooip.lazarus.factions;

import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.factions.claim.Claim;
import me.qiooip.lazarus.factions.enums.Relation;
import me.qiooip.lazarus.factions.event.FactionRenameEvent;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.factions.type.SystemFaction;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public abstract class Faction {

    protected UUID id;

    private String name;
    private boolean deathban;

    private transient List<Claim> claims;

    public Faction() {
        this.claims = new ArrayList<>();
    }

    protected Faction(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.deathban = true;

        this.claims = new ArrayList<>();
    }

    public boolean setName(CommandSender sender, String name, boolean forceRename) {
        FactionRenameEvent event = new FactionRenameEvent(name, this, sender, forceRename);
        if(event.isCancelled()) return false;

        this.name = name;
        return true;
    }

    private Relation getRelation(CommandSender sender) {
        return sender instanceof Player ? this.getRelation(FactionsManager
            .getInstance().getPlayerFaction((Player) sender)) : Relation.ENEMY;
    }

    private Relation getRelation(Faction other) {
        if(!(other instanceof PlayerFaction) || !(this instanceof PlayerFaction)) return Relation.ENEMY;

        PlayerFaction faction = (PlayerFaction) this;
        PlayerFaction playerFaction = (PlayerFaction) other;

        return faction == playerFaction ? Relation.MEMBER : faction
            .isAlly(playerFaction) ? Relation.ALLY : Relation.ENEMY;
    }

    private String getRelationColor(CommandSender sender) {
        if(this instanceof SystemFaction) {
            return ((SystemFaction) this).getColor();
        }

        return this.getRelation(sender).getColor();
    }

    public String getName(CommandSender sender) {
        return this.getRelationColor(sender) + this.name;
    }

    public String getDisplayName(CommandSender sender) {
        return this.getName(sender);
    }

    public String getDeathbanString() {
        return this.deathban ? ChatColor.RED + "Deathban" : ChatColor.GREEN + "Non-Deathban";
    }

    public void addClaim(Claim claim) {
        this.claims.add(claim);
    }

    public void removeClaim(Claim claim) {
        this.claims.remove(claim);
    }

    public boolean isSafezone() {
        return false;
    }

    public boolean areEnderpearlEnabled() {
        return true;
    }

    public boolean shouldCancelPvpTimerEntrance(Player player) {
        return true;
    }

    public void showInformation(CommandSender sender) {

    }

    @Override
    public boolean equals(Object other) {
        if(this == other) return true;
        if(!(other instanceof Faction)) return false;

        Faction faction = (Faction) other;
        return Objects.equals(this.id, faction.getId());
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
