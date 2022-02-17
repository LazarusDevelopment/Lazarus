package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpeedCommand extends BaseCommand {

    public SpeedCommand() {
        super("speed", "lazarus.speed", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if(args.length < 1) {
            player.sendMessage(Language.PREFIX + Language.SPEED_USAGE);
            return;
        }

        this.setSpeed(player, player.isFlying(), args[0]);
    }

    private void setSpeed(Player player, boolean flying, String arg) {
        if(!this.checkNumber(player, arg)) return;

        int amount = Math.abs(Integer.parseInt(arg));

        if(amount > 10) {
            player.sendMessage(Language.PREFIX + Language.SPEED_LIMIT_REACHED);
            return;
        }

        String type;

        if(flying) {
            player.setFlySpeed(amount * 0.1f);
            type = "fly";
        } else {
            player.setWalkSpeed(amount * 0.1f);
            type = "walk";
        }

        player.sendMessage(Language.PREFIX + Language.SPEED_SPEED_CHANGED
            .replace("<type>", type)
            .replace("<value>", String.valueOf(amount)));
    }
}
