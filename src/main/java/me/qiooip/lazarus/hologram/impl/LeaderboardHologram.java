package me.qiooip.lazarus.hologram.impl;

import lombok.Getter;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.leaderboard.entry.UuidCacheEntry;
import me.qiooip.lazarus.hologram.Hologram;
import me.qiooip.lazarus.hologram.type.LeaderboardHologramType;
import org.bukkit.Location;

import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public class LeaderboardHologram extends Hologram {

    private LeaderboardHologramType leaderboardType;
    private final transient AtomicBoolean update;

    public LeaderboardHologram() {
        this.update = new AtomicBoolean();
    }

    public LeaderboardHologram(int id, Location location, LeaderboardHologramType leaderboardType) {
        super(id, location);

        this.leaderboardType = leaderboardType;
        this.update = new AtomicBoolean();

        this.createHologramLines();
    }

    public void updateHologramLines() {
        int headerEnds = this.leaderboardType.getHeader().size();
        int index = 1;

        for(UuidCacheEntry<Integer> entry : this.leaderboardType.getLeaderboard()) {
            this.updateMessage(headerEnds++, leaderboardType.getLineFormat()
                .replace("<number>", String.valueOf(index++))
                .replace("<player>", entry.getName())
                .replace("<value>", String.valueOf(entry.getValue())));
        }

        this.update.set(true);
    }

    @Override
    public void createHologramLines() {
        this.entries.clear();
        Location lineLocation = this.location.clone();

        for(String line : this.leaderboardType.getHeader()) {
            this.addEntry(line, lineLocation = this.getLineLocation(lineLocation));
        }

        int index = 1;

        for(UuidCacheEntry<Integer> entry : this.leaderboardType.getLeaderboard()) {
            lineLocation = this.getLineLocation(lineLocation);

            this.addEntry(this.leaderboardType.getLineFormat()
                .replace("<number>", String.valueOf(index++))
                .replace("<player>", entry.getName())
                .replace("<value>", String.valueOf(entry.getValue())), lineLocation);
        }

        if(index < 10) {
            for(int i = index; i <= 10; i++) {
                lineLocation = this.getLineLocation(lineLocation);

                this.addEntry(Language.HOLOGRAM_EMPTY_LINE_FORMAT
                    .replace("<number>", String.valueOf(i)), lineLocation);
            }
        }

        for(String line : this.leaderboardType.getFooter()) {
            this.addEntry(line, lineLocation = this.getLineLocation(lineLocation));
        }
    }
}
