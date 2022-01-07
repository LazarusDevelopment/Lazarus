package me.qiooip.lazarus.games.koth;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.games.Capzone;
import me.qiooip.lazarus.games.Placeholder;
import me.qiooip.lazarus.games.koth.event.KothCappedEvent;
import me.qiooip.lazarus.games.koth.event.KothKnockedEvent;
import me.qiooip.lazarus.games.koth.event.KothStartedCappingEvent;
import me.qiooip.lazarus.utils.Messages;
import me.qiooip.lazarus.utils.StringUtils;
import me.qiooip.lazarus.utils.Tasks;
import org.bukkit.entity.Player;

public class RunningKoth {

    @Getter private final KothData kothData;
    @Getter private final Capzone capzone;

    private int initialCapTime;

    private final long startTime;
    private long capperChange;

    private long antiSpamDelay;

    RunningKoth(KothData kothData, int time) {
        this.kothData = kothData;
        this.capzone = new Capzone(kothData.getCuboid(), time);

        this.initialCapTime = time;
        this.startTime = this.capperChange = System.currentTimeMillis();

        this.capzone.getCuboid().getPlayers().forEach(player -> this.onPlayerEnterCapzone(player));
    }

    public void changeCapTime(int newTime) {
        if(newTime < this.capzone.getTime()) this.capzone.setTime(newTime);
        this.initialCapTime = newTime;
    }

    private void handleWin() {
        Player capper = this.capzone.getCapper();
        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(capper);

        String uptime = StringUtils.formatMillis(System.currentTimeMillis() - this.startTime);

        if(faction == null) {
            Language.KOTH_CAPPED_NO_FACTION.forEach(line -> Messages.sendMessage(Placeholder.RunningKothReplacer
            .parse(this, line.replace("<uptime>", uptime))));
        } else {
            Language.KOTH_CAPPED_WITH_FACTION.forEach(line -> Messages.sendMessage(Placeholder.RunningKothReplacer
            .parse(this, line
                .replace("<faction>", faction.getDisplayName(capper))
                .replace("<uptime>", uptime))));
        }

        Tasks.sync(() -> {
            new KothCappedEvent(this.kothData, capper);
            Lazarus.getInstance().getKothManager().stopKoth(this);
        });
    }

    private void handlePlayerStartedCapping(Player player) {
        if(player == null) return;

        player.sendMessage(Language.KOTH_PREFIX + Placeholder.RunningKothReplacer
            .parse(this, Language.KOTH_YOU_STARTED_CAPPING));

        if((this.antiSpamDelay - System.currentTimeMillis()) <= 0) {
            Messages.sendMessageWithoutPlayer(player, Language.KOTH_PREFIX + Placeholder.RunningKothReplacer
                .parse(this, Language.KOTH_SOMEONE_STARTED_CAPPING));

            this.antiSpamDelay = System.currentTimeMillis() + Config.KOTH_ANTI_SPAM_MESSAGE_DELAY;
        }

        new KothStartedCappingEvent(this, player);
    }

    private void handlePlayerKnocked(Player player) {
        this.capzone.setTime(this.initialCapTime);

        long capTimeMillis = System.currentTimeMillis() - this.capperChange;
        this.capperChange = System.currentTimeMillis();

        if((this.antiSpamDelay - System.currentTimeMillis()) <= 0) {
            Messages.sendMessage(Language.KOTH_PREFIX + Placeholder.RunningKothReplacer
                .parse(this, Language.KOTH_KNOCKED));

            this.antiSpamDelay = System.currentTimeMillis() + Config.KOTH_ANTI_SPAM_MESSAGE_DELAY;
        }

        new KothKnockedEvent(this, player, capTimeMillis);
    }

    void onPlayerEnterCapzone(Player player) {
        if(Lazarus.getInstance().getStaffModeManager().isInStaffModeOrVanished(player)) return;

        if(this.capzone.hasNoPlayers()) {
            this.handlePlayerStartedCapping(player);
        }

        this.capzone.addPlayer(player);
    }

    void onPlayerLeaveCapzone(Player player) {
        if(!this.capzone.getPlayers().contains(player.getName())) return;

        if(this.capzone.isCapper(player)) {
            this.handlePlayerKnocked(player);

            if(this.capzone.getPlayers().size() > 1) {
                this.handlePlayerStartedCapping(this.capzone.getNextCapper());
            }
        }

        this.capzone.removePlayer(player);
    }

    void tick() {
        if(this.capzone.hasNoPlayers()) {
            int timePassed = (int) ((System.currentTimeMillis() - this.capperChange) / 1000);
            int nobodyCappingInterval = Config.KOTH_NOBODY_CAPPING_MESSAGE_INTERVAL;

            if(nobodyCappingInterval != 0 && timePassed > 0 && timePassed % nobodyCappingInterval == 0) {
                Messages.sendMessage(Language.KOTH_PREFIX + Placeholder
                    .RunningKothReplacer.parse(this, Language.KOTH_NO_ONE_CAPPING));
            }

            return;
        }

        if(this.capzone.decreaseTime() <= 0) {
            this.handleWin();
            return;
        }

        int cappingInterval = Config.KOTH_CAPPING_MESSAGE_INTERVAL;

        if(cappingInterval != 0 && this.capzone.getTime() % cappingInterval == 0) {
            Player capper = this.capzone.getCapper();

            capper.sendMessage(Language.KOTH_PREFIX + Placeholder
                .RunningKothReplacer.parse(this, Language.KOTH_YOU_ARE_CAPPING));

            Messages.sendMessageWithoutPlayer(capper, Language.KOTH_PREFIX + Placeholder
                .RunningKothReplacer.parse(this, Language.KOTH_SOMEONE_IS_CAPPING));
        }
    }

    public String getScoreboardEntry() {
        return this.capzone.getTimeLeft();
    }
}
