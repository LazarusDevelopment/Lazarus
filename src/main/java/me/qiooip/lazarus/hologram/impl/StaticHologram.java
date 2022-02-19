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

    public Location getLineLocation() {
        return this.getLineLocation(this.getLastLineLocation());
    }

    public Location getLastLineLocation() {
        return this.entries.get(this.entries.size() - 1).getLocation();
    }

    public void addLine(String line) {
        this.lines.add(line);
        this.addEntry(line, this.getLineLocation());
    }

    public void addLine(int index, String line) {
        this.lines.add(line);
        this.addEntry(index, line, this.getLineLocation());
    }

    public void removeLine(int index) {
        this.lines.remove(index);
        this.removeEntry(index);
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
