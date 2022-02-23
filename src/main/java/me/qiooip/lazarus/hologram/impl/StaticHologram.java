package me.qiooip.lazarus.hologram.impl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.qiooip.lazarus.hologram.Hologram;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class StaticHologram extends Hologram {

    private List<String> lines;

    public StaticHologram(int id, Location location) {
        super(id, location);

        this.lines = new ArrayList<>();
        this.lines.add("Hologram " + id + " (edit me)");

        this.updateHologramLines();
    }

    public Location getLastLineLocation() {
        return this.getLineLocationAtIndex(this.entries.size() - 1);
    }

    public Location getLineLocationAtIndex(int index) {
        if(this.entries.isEmpty()) {
            return this.location;
        }

        return this.entries.get(index).getLocation();
    }

    public void addLine(String line) {
        this.lines.add(line);
        this.addEntry(line, this.getLineLocation(this.getLastLineLocation()));
    }

    public boolean addLine(int index, String line) {
        if(index < 0 || index > this.entries.size() - 1) {
            return false;
        }

        Location location = this.getLineLocationAtIndex(index);

        this.lines.add(index, line);
        this.addEntry(index, line, location);

        for(int i = index; i < this.entries.size(); i++) {
            this.getEntry(i).setLocation(location = this.getLineLocation(location));
        }

        return true;
    }

    public boolean removeLine(int index) {
        if(index < 0 || index > this.entries.size() - 1) {
            return false;
        }

        this.lines.remove(index);
        this.removeEntry(index);
        return true;
    }

    @Override
    public void updateHologramLines() {
        this.entries.clear();
        Location lineLocation = this.location.clone();

        for(String line : this.lines) {
            this.addEntry(line, lineLocation = this.getLineLocation(lineLocation));
        }
    }
}
