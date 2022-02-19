package me.qiooip.lazarus.hologram;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@AllArgsConstructor
public abstract class Hologram {

    protected int id;
    protected Location location;

    protected transient Set<UUID> viewers;
    protected transient List<HologramEntry> entries;

    public Hologram() {
        this.viewers = new HashSet<>();
        this.entries = new ArrayList<>();
    }

    public Hologram(int id, Location location) {
        this(id, location, new HashSet<>(), new ArrayList<>());
    }

    public void addEntry(String message, Location location) {
        if(message.isEmpty()) return;

        int entityId = this.getEntityId();
        this.entries.add(new HologramEntry(entityId, message, location));
    }

    public void addEntry(int index, String message, Location location) {
        if(index > this.entries.size() - 1 || message.isEmpty()) return;

        int entityId = this.getEntityId();
        this.entries.add(index, new HologramEntry(entityId, message, location));
    }

    public void removeEntry(int index) {
        this.entries.remove(index);
    }

    public int getEntityId() {
        return ThreadLocalRandom.current().nextInt(100_000_000, 200_000_000);
    }

    public Location getLineLocation(Location parent) {
        return parent.clone().subtract(0, 0.4, 0);
    }

    public void sendHologram(Player player) {
        NmsUtils nmsUtils = NmsUtils.getInstance();

        this.entries.forEach(entry -> nmsUtils.sendHologramSpawnPacket(
            player, entry.getEntityId(), entry.getLocation(), entry.getMessage()));
    }

    public void removeHologram(Player player) {
        NmsUtils nmsUtils = NmsUtils.getInstance();

        this.entries.forEach(entry -> nmsUtils
            .sendHologramDestroyPacket(player, entry.getEntityId()));
    }

    public abstract void updateHologramLines();
}
