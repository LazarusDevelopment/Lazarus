package me.qiooip.lazarus.userdata;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.userdata.settings.Settings;
import org.apache.commons.lang.time.FastDateFormat;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Userdata {

    private UUID uuid;
    private String name;

    private int kills;
    private int deaths;
    private int killstreak;

    private int balance;
    private int lives;

    private boolean reclaimUsed;
    private Settings settings;

    private List<UUID> ignoring;
    private List<String> notes;
    private List<String> lastDeaths;
    private Map<String, Long> kitDelays;

    private transient Location lastLocation;

    public Userdata(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;

        this.balance = Config.DEFAULT_BALANCE_PLAYER;
        this.lives = Config.DEFAULT_LIVES;

        this.settings = new Settings();

        this.ignoring = new ArrayList<>();
        this.notes = new ArrayList<>();
        this.lastDeaths = new ArrayList<>();
        this.kitDelays = new HashMap<>();
    }

    public void addKill() {
        this.kills++;
    }

    public void addDeath() {
        this.deaths++;
    }

    public void addKillstreak() {
        this.killstreak++;
    }

    public int resetKillstreak() {
        int temp = this.killstreak;
        this.killstreak = 0;

        return temp;
    }

    public void addLives(int amount) {
        this.lives += amount;
    }

    public void subtractLives(int amount) {
        this.lives -= amount;
    }

    public boolean isIgnoring(Player player) {
        return this.ignoring.contains(player.getUniqueId());
    }

    public void addIgnoring(Player player) {
        this.ignoring.add(player.getUniqueId());
    }

    public void removeIgnoring(Player player) {
        this.ignoring.remove(player.getUniqueId());
    }

    public void addLastDeath(String deathMessage) {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance(Config
        .DATE_FORMAT, Config.TIMEZONE, Locale.ENGLISH);

        deathMessage = deathMessage.replace('§', '&');

        if(this.lastDeaths.size() < 5) {
            this.lastDeaths.add(0, fastDateFormat.format(System.currentTimeMillis()) + " - " + deathMessage);
            return;
        }

        this.lastDeaths.remove(4);
        this.lastDeaths.add(0, fastDateFormat.format(System.currentTimeMillis()) + " - " + deathMessage);
    }
}
