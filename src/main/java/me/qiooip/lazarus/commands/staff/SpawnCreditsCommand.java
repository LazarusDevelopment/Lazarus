package me.qiooip.lazarus.commands.staff;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.userdata.Userdata;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCreditsCommand extends BaseCommand {

    public SpawnCreditsCommand() {
        super("spawncredits", "lazarus.spawncredits");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 3) {
            Language.SPAWN_CREDITS_USAGE.forEach(sender::sendMessage);
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if(!this.checkPlayer(sender, target, args[1])) return;

        if(!this.checkNumber(sender, args[2])) return;
        int newAmount = Math.abs(Integer.parseInt(args[2]));

        switch(args[0].toLowerCase()) {
            case "give":
            case "add": {
                this.modifyAmount(sender, target, 1, newAmount);
                return;
            }
            case "remove": {
                this.modifyAmount(sender, target, 2, newAmount);
                return;
            }
            case "set": {
                this.modifyAmount(sender, target, 3, newAmount);
                return;
            }
            default: {
                Language.SPAWN_CREDITS_USAGE.forEach(sender::sendMessage);
            }
        }
    }

    private void modifyAmount(CommandSender sender, Player target, int action, int newAmount) {
        Userdata userdata = Lazarus.getInstance().getUserdataManager().getUserdata(target);
        int amount = userdata.getSpawnCredits();

        switch(action) {
            case 1: {
                userdata.changeSpawnCredits(amount + newAmount);
                break;
            }
            case 2: {
                userdata.changeSpawnCredits(Math.abs(amount - newAmount));
                break;
            }
            case 3: {
                userdata.changeSpawnCredits(Math.abs(newAmount));
            }
        }

        int newSpawnCredits = userdata.getSpawnCredits();

        sender.sendMessage(Language.PREFIX + Language.SPAWN_CREDITS_AMOUNT_CHANGED_STAFF
            .replace("<player>", target.getName())
            .replace("<sender>", sender.getName())
            .replace("<amount>", String.valueOf(newSpawnCredits)));

        target.sendMessage(Language.PREFIX + Language.SPAWN_CREDITS_AMOUNT_CHANGED
            .replace("<sender>", sender.getName())
            .replace("<amount>", String.valueOf(newSpawnCredits)));
    }
}
