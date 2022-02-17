package me.qiooip.lazarus.commands.staff;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.StringUtils;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ViewDistanceCommand extends BaseCommand {

    private boolean running;

    public ViewDistanceCommand() {
        super("viewdistance", Collections.singletonList("vd"), "lazarus.viewdistance");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            Language.VIEW_DISTANCE_USAGE.forEach(line -> sender.sendMessage(line
                .replace("<chunks>", String.valueOf(Bukkit.getViewDistance()))));
            return;
        }

        if(!StringUtils.isInteger(args[0])) {
            sender.sendMessage(Language.PREFIX + Language.COMMANDS_INVALID_NUMBER);
            return;
        }

        if(this.running) {
            sender.sendMessage(Language.PREFIX + Language.VIEW_DISTANCE_ALREADY_RUNNING);
            return;
        }

        new ChangeDistanceTask(Math.abs(Integer.parseInt(args[0])), sender);

        Language.VIEW_DISTANCE_STARTED.forEach(line -> sender.sendMessage(line
            .replace("<seconds>", String.valueOf(Bukkit.getOnlinePlayers().size() / 20))));
    }

    class ChangeDistanceTask extends BukkitRunnable {

        private final List<UUID> players;
        private final int viewDistance;
        private final CommandSender sender;

        private ChangeDistanceTask(int viewDistance, CommandSender sender) {
            this.viewDistance = viewDistance;
            this.sender = sender;

            NmsUtils.getInstance().setViewDistance(viewDistance);
            this.players = Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId).collect(Collectors.toList());

            running = true;

            this.runTaskTimer(Lazarus.getInstance(), 0L, 1L);
        }

        @Override
        public void run() {
            if(this.players.isEmpty()) {
                this.cancel();
                this.sender.sendMessage(Language.PREFIX + Language.VIEW_DISTANCE_FINISHED);
                running = false;
                return;
            }

            Player player = Bukkit.getPlayer(players.get(0));
            if(player != null) player.spigot().setViewDistance(this.viewDistance);

            this.players.remove(0);
        }
    }
}
