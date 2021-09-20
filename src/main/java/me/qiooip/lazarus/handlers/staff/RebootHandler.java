package me.qiooip.lazarus.handlers.staff;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.event.LazarusKickEvent;
import me.qiooip.lazarus.handlers.event.LazarusKickEvent.KickType;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.utils.Messages;
import me.qiooip.lazarus.utils.StringUtils;
import me.qiooip.lazarus.utils.StringUtils.FormatType;
import me.qiooip.lazarus.utils.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.SpigotConfig;

import java.io.File;

public class RebootHandler extends Handler {

    private RebootTask rebootTask;

    public boolean isRebooting() {
        return this.rebootTask != null;
    }

    public void startReboot(int time) {
        this.rebootTask = new RebootTask(time);
    }

    public void cancelReboot() {
        this.rebootTask.cancel();
        this.rebootTask = null;

        Language.REBOOT_CANCELED.forEach(Messages::sendMessage);
    }

    public String getScoreboardEntry() {
        return StringUtils.formatTime(this.rebootTask.getSeconds(), FormatType.SECONDS_TO_MINUTES);
    }

    private static class RebootTask extends BukkitRunnable {

        @Getter private int seconds;

        RebootTask(int seconds) {
            this.seconds = seconds;
            this.broadcast();

            this.runTaskTimerAsynchronously(Lazarus.getInstance(), 0L, 20L);
        }

        @Override
        public void run() {
            if(--this.seconds <= 0) {
                this.cancel();

                Tasks.sync(() -> {
                    this.kickOnlinePlayers();
                    this.setupRestartScript();
                });

                Tasks.syncLater(() -> Bukkit.getServer().shutdown(), 20L);
                return;
            }

            if(this.seconds <= 10 || this.seconds == 15 || this.seconds == 30 || this.seconds == 45 || this.seconds % 60 == 0) {
                this.broadcast();
            }
        }

        private void broadcast() {
            Language.REBOOT_BROADCAST.forEach(line -> Messages.sendMessage(line
                .replace("<time>", StringUtils.formatDurationWords(this.seconds * 1000L))));
        }

        private void kickOnlinePlayers() {
            String kickReason = Language.REBOOT_PLAYER_KICK_MESSAGE;

            Bukkit.getOnlinePlayers().forEach(player -> {
                LazarusKickEvent event = new LazarusKickEvent(player, KickType.REBOOT, kickReason);

                if(!event.isCancelled()) {
                    player.kickPlayer(kickReason);
                }
            });
        }

        private void setupRestartScript() {
            File restartScript = new File(SpigotConfig.restartScript);

            Thread shutdownHook = new Thread(() -> {
                try {
                    String os = System.getProperty("os.name").toLowerCase();

                    if (os.contains("win")) {
                        Runtime.getRuntime().exec("cmd /c start " + restartScript.getPath());
                    } else {
                        Runtime.getRuntime().exec(new String[] { "sh", restartScript.getPath() });
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            });

            shutdownHook.setDaemon(true);
            Runtime.getRuntime().addShutdownHook(shutdownHook);
        }
    }
}
