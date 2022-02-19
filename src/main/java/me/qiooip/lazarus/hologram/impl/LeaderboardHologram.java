package me.qiooip.lazarus.hologram.impl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.qiooip.lazarus.handlers.leaderboard.entry.UuidCacheEntry;
import me.qiooip.lazarus.hologram.Hologram;
import me.qiooip.lazarus.hologram.type.LeaderboardHologramType;
import org.bukkit.Location;

@Getter
@NoArgsConstructor
public class LeaderboardHologram extends Hologram {

    private LeaderboardHologramType leaderboardType;

    public LeaderboardHologram(int id, Location location, LeaderboardHologramType leaderboardType) {
        super(id, location);

        this.leaderboardType = leaderboardType;
        this.updateHologramLines();
    }

    @Override
    public void updateHologramLines() {
        this.entries.clear();
        Location lineLocation = this.location.clone();

        for(String line : this.leaderboardType.getHeader()) {
            this.addEntry(line, lineLocation = this.getLineLocation(lineLocation));
        }

        int index = 1;

        for(UuidCacheEntry<Integer> entry : this.leaderboardType.getLeaderboard()) {
            lineLocation = this.getLineLocation(lineLocation);

            this.addEntry(this.leaderboardType.getLineFormat()
                .replace("<number>", String.valueOf(index))
                .replace("<player>", entry.getName())
                .replace("<value>", String.valueOf(entry.getValue())), lineLocation);
        }

        for(String line : this.leaderboardType.getFooter()) {
            this.addEntry(line, lineLocation = this.getLineLocation(lineLocation));
        }
    }
}
