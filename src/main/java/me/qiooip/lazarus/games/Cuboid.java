package me.qiooip.lazarus.games;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.stream.Stream;

@Getter
public class Cuboid {
	
	private String worldName;

	private int minX, maxX;
	private int minY, maxY;
	private int minZ, maxZ;

	private transient Location center;

	public Cuboid() { }
	
	public Cuboid(Location loc1, Location loc2) {
		this.worldName = loc1.getWorld().getName();

		this.minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
		this.maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());

		this.minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
		this.maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());

		this.minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
		this.maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
	}

	public Location getCenter() {
		if(this.center != null) {
			return this.center;
		}

		int x2 = this.maxX + 1;
		int y2 = this.maxY + 1;
		int z2 = this.maxZ + 1;
		
		return this.center = new Location(
		    this.getWorld(),
		    this.minX + (x2 - this.minX) / 2.0,
		    this.minY + (y2 - this.minY) / 2.0,
		    this.minZ + (z2 - this.minZ) / 2.0
		);
	}

	public Location getCenterWithMinY() {
		int x2 = this.maxX + 1;
		int y2 = this.maxY + 1;
		int z2 = this.maxZ + 1;

		return new Location(this.getWorld(), this.minX + (x2 - this.minX) / 2.0, this.minY, this.minZ + (z2 - this.minZ) / 2.0);
	}

	public World getWorld() {
		return Bukkit.getWorld(worldName);
	}

	private boolean contains(World world, int x, int y, int z) {
		return world.getName().equals(this.worldName) && x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ;
	}

	public boolean contains(Location loc) {
		return this.contains(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}
	
	public int[] getFirstLocation() {
		return new int[] { minX, minY, minZ };
	}
	
	public int[] getSecondLocation() {
		return new int[] { maxX, maxY, maxZ };
	}

	public Stream<? extends Player> getPlayers() {
		return Bukkit.getOnlinePlayers().stream().filter(player -> this.contains(player.getLocation()));
	}
}