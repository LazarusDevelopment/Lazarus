package me.qiooip.lazarus.hologram;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.qiooip.lazarus.utils.Tasks;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

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

    public void decrementId() {
        this.id--;
    }

    public void addEntry(String message, Location location) {
        int entityId = this.getEntityId();
        this.entries.add(new HologramEntry(entityId, message, location));
    }

    public void addEntry(int index, String message, Location location) {
        int entityId = this.getEntityId();
        this.entries.add(index, new HologramEntry(entityId, message, location));
    }

    public HologramEntry getEntry(int index) {
        return this.entries.get(index);
    }

    public void removeEntry(int index) {
        this.entries.remove(index);
    }

    public void updateMessage(int index, String message) {
        this.entries.get(index).setMessage(message);
    }

    public int getEntityId() {
        return ThreadLocalRandom.current().nextInt(100_000_000, 200_000_000);
    }

    public Location getLineLocation(Location parent) {
        return parent.clone().subtract(0, 0.25, 0);
    }

    public void sendHologram(Player player) {
        NmsUtils nmsUtils = NmsUtils.getInstance();

        for(HologramEntry entry : this.entries) {
            if(entry.getMessage().isEmpty()) continue;

            nmsUtils.sendHologramSpawnPacket(player,
                entry.getEntityId(), entry.getLocation(), entry.getMessage());
        }
    }

    public void removeHologram(Player player) {
        NmsUtils nmsUtils = NmsUtils.getInstance();

        this.entries.forEach(entry -> nmsUtils
            .sendHologramDestroyPacket(player, entry.getEntityId()));
    }

    public void teleportHologram(Player player) {
        NmsUtils nmsUtils = NmsUtils.getInstance();

        Location location = player.getEyeLocation().clone();
        this.location = location;

        for(HologramEntry entry : this.entries) {
            location = this.getLineLocation(location);
            entry.setLocation(location);

            if(!entry.getMessage().isEmpty()) {
                nmsUtils.sendHologramTeleportPacket(player, entry.getEntityId(), location);
            }
        }
    }

    public void refreshForViewers() {
        this.forEachViewer(viewer -> {
            this.removeHologram(viewer);
            this.sendHologram(viewer);
        });
    }

    public void refreshForViewersAsync() {
        Tasks.async(() -> this.refreshForViewers());
    }

    public void forEachViewer(Consumer<Player> action) {
        for(UUID viewer : this.viewers) {
            Player player = Bukkit.getPlayer(viewer);

            if(player != null) {
                action.accept(player);
            }
        }
    }

    public void forEachViewerAsync(Consumer<Player> action) {
        Tasks.async(() -> this.forEachViewer(action));
    }

    public boolean isInSameWorld(Player player) {
        return player.getWorld().getName().equals(this.location.getWorld().getName());
    }

    public abstract void createHologramLines();
}
