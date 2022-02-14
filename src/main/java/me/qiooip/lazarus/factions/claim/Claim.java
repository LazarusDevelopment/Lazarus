package me.qiooip.lazarus.factions.claim;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.SystemFaction;
import me.qiooip.lazarus.glass.GlassInfo;
import me.qiooip.lazarus.glass.GlassManager.GlassType;
import org.bson.types.ObjectId;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Getter
@Setter
@NoArgsConstructor
public class Claim implements Cloneable {

    private transient ObjectId id;

    private UUID ownerId;
    private String worldName;

    private int minX;
    private int maxX;

    private int minZ;
    private int maxZ;

    public Claim(Faction owner, Location loc1, Location loc2) {
        this(owner, loc1.getWorld(), loc1.getBlockX(), loc2.getBlockX(), loc1.getBlockZ(), loc2.getBlockZ());
    }

    public Claim(Faction owner, World world, int x1, int x2, int z1, int z2) {
        this.ownerId = owner.getId();
        this.worldName = world.getName();

        this.minX = Math.min(x1, x2);
        this.maxX = Math.max(x1, x2);

        this.minZ = Math.min(z1, z2);
        this.maxZ = Math.max(z1, z2);
    }

    public World getWorld() {
        return Bukkit.getWorld(this.worldName);
    }

    public Faction getOwner() {
        return FactionsManager.getInstance().getFactionByUuid(this.ownerId);
    }

    public boolean contains(Location location) {
        return this.contains(location.getWorld(), location.getBlockX(), location.getBlockZ());
    }

    public boolean contains(World world, int x, int z) {
        return this.worldName.equals(world.getName()) && this.maxX >= x && this.minX <= x && this.maxZ >= z && this.minZ <= z;
    }

    public Location getMinimumPoint() {
        return new Location(this.getWorld(), this.minX, 0, this.minZ);
    }

    public Location getMaximumPoint() {
        return new Location(this.getWorld(), this.maxX, 0, this.maxZ);
    }

    public int sizeX() {
        return Math.abs(this.maxX - this.minX);
    }

    public int sizeZ() {
        return Math.abs(this.maxZ - this.minZ);
    }

    public Location getCenter() {
        int x2 = this.maxX + 1;
        int z2 = this.maxZ + 1;

        return new Location(this.getWorld(), this.minX + (x2 - this.minX) / 2.0, 128, this.minZ + (z2 - this.minZ) / 2.0);
    }

    Location[] getCorners() {
        return new Location[] {
            new Location(this.getWorld(), this.minX, 0, this.minZ),
            new Location(this.getWorld(), this.minX, 0, this.maxZ),
            new Location(this.getWorld(), this.maxX, 0, this.minZ),
            new Location(this.getWorld(), this.maxX, 0, this.maxZ),
        };
    }

    public List<Location> getClosestSides(Location loc) {
        List<Location> closestEdges = new ArrayList<>();

        int closestX = this.closestX(loc);
        int closestZ = this.closestZ(loc);

        if(Math.abs(closestZ - loc.getBlockZ()) <= 5) {
            for(int x = Math.max(this.minX, loc.getBlockX() - 5); x <= Math.min(this.maxX, loc.getBlockX() + 5); x++) {
                closestEdges.add(new Location(loc.getWorld(), x, loc.getBlockY(), closestZ));
            }
        }

        if(Math.abs(closestX - loc.getBlockX()) <= 5) {
            for(int z = Math.max(this.minZ, loc.getBlockZ() - 5); z <= Math.min(this.maxZ, loc.getBlockZ() + 5); z++) {
                closestEdges.add(new Location(loc.getWorld(), closestX, loc.getBlockY(), z));
            }
        }

        return closestEdges;
    }

    private int closestX(Location loc) {
        return Math.abs(loc.getBlockX() - this.minX) < Math.abs(loc.getBlockX() - this.maxX) ? this.minX : this.maxX;
    }

    private int closestZ(Location loc) {
        return Math.abs(loc.getBlockZ() - this.minZ) < Math.abs(loc.getBlockZ() - this.maxZ) ? this.minZ : this.maxZ;
    }

    Claim expand(int amount) {
        this.minX -= amount;
        this.maxX += amount;
        this.minZ -= amount;
        this.maxZ += amount;
        return this;
    }

    public Stream<? extends Player> getPlayers() {
        return Bukkit.getOnlinePlayers().stream().filter(player -> this.contains(player.getLocation()));
    }

    void showPillars(Player player, Material material) {
        byte durability;

        if(this.getOwner().isSafezone()) {
            durability = 1;
        } else if(this.getOwner() instanceof SystemFaction) {
            durability = 3;
        } else if(this.getOwner() == FactionsManager.getInstance().getPlayerFaction(player)) {
            durability = 5;
        } else {
            durability = 14;
        }

        for(Location corner : this.getCorners()) {
            for(int i = 0; i < player.getLocation().getBlockY() + 60; i++) {
                Location location = corner.clone();
                location.setY(i);

                Lazarus.getInstance().getGlassManager().generateGlassVisual(player, new GlassInfo(GlassType
                    .CLAIM_MAP, location, i % 4 == 0 ? material : Material.STAINED_GLASS, durability));
            }
        }
    }

    Location getFirstSafePosition() {
        int maxX = this.maxX < 0 ? this.minX - 2 : this.maxX + 2;
        int maxZ = this.maxZ < 0 ? this.minZ - 2 : this.maxZ + 2;
        int y = this.getWorld().getHighestBlockYAt(maxX, maxZ);

        return new Location(this.getWorld(), maxX, y, maxZ);
    }

    @Override
    public boolean equals(Object other) {
        if(this == other) return true;
        if(!(other instanceof Claim)) return false;

        Claim claim = (Claim) other;

        if(!this.ownerId.equals(claim.getOwnerId())) return false;
        if(!this.worldName.equals(claim.getWorldName())) return false;

        return this.minX == claim.getMinX()
            && this.maxX == claim.getMaxX()
            && this.minZ == claim.getMinZ()
            && this.maxZ == claim.getMaxZ();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.ownerId, this.worldName, this.minX, this.maxX, this.minZ, this.maxZ);
    }

    @Override
    public Claim clone() {
        try {
            return (Claim) super.clone();
        } catch(CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
