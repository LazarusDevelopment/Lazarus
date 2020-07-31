package me.qiooip.lazarus.selection;

import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.claim.Claim;
import me.qiooip.lazarus.games.Cuboid;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@Getter
public class Selection implements Cloneable {

    private final UUID playerId;
    private final SelectionType type;
    private final SelectionCheck check;
    private final ItemStack wand;

    @Setter private Location posOne;
    @Setter private Location posTwo;

    Selection(Player player, SelectionType type, SelectionCheck check, ItemStack wand) {
        this.playerId = player.getUniqueId();
        this.type = type;
        this.check = check;
        this.wand = wand;
    }

    public void clearSelection() {
        this.posOne = null;
        this.posTwo = null;
    }

    public boolean areBothPositionsSet() {
        return this.posOne != null && this.posTwo != null;
    }

    public Claim toClaim(Faction faction) {
        return new Claim(faction, this.posOne, this.posTwo);
    }

    public Cuboid toCuboid() {
        return new Cuboid(this.posOne, this.posTwo);
    }

    public int sizeX() {
        return Math.abs(this.posOne.getBlockX() - this.posTwo.getBlockX());
    }

    public int sizeZ() {
        return Math.abs(this.posOne.getBlockZ() - this.posTwo.getBlockZ());
    }

    public int[] getSize(boolean leftClick, Location location) {
        if(leftClick) {
            if(this.posTwo == null) return new int[] { -1, -1 };

            int sizeX = Math.abs(this.posTwo.getBlockX() - location.getBlockX());
            int sizeZ = Math.abs(this.posTwo.getBlockZ() - location.getBlockZ());

            return new int[] { Math.min(sizeX, sizeZ), Math.max(sizeX, sizeZ) };
        } else {
            if(this.posOne == null) return new int[] { -1, -1 };

            int sizeX = Math.abs(this.posOne.getBlockX() - location.getBlockX());
            int sizeZ = Math.abs(this.posOne.getBlockZ() - location.getBlockZ());

            return new int[] { Math.min(sizeX, sizeZ), Math.max(sizeX, sizeZ) };
        }
    }

    @Override
    public Selection clone() {
        try {
            return (Selection) super.clone();
        } catch(CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
