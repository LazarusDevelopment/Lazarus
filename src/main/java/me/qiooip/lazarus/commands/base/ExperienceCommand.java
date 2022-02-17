package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ExperienceCommand extends BaseCommand {

    public ExperienceCommand() {
        super("experience", Arrays.asList("exp", "xp"), "lazarus.experience");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 3) {
            Language.EXPERIENCE_USAGE.forEach(sender::sendMessage);
            return;
        }

        switch(args[0].toLowerCase()) {
            case "add": {
                Player target = Bukkit.getPlayer(args[1]);

                if(!this.checkPlayer(sender, target, args[1])) return;
                if(!this.checkNumber(sender, args[2])) return;

                int level = Math.abs(Integer.parseInt(args[2]));

                target.setLevel(target.getLevel() + level);

                sender.sendMessage(Language.PREFIX + Language.EXPERIENCE_ADDED
                    .replace("<player>", target.getName())
                    .replace("<level>", String.valueOf(level)));
                return;
            }
            case "remove": {
                Player target = Bukkit.getPlayer(args[1]);

                if(!this.checkPlayer(sender, target, args[1])) return;
                if(!this.checkNumber(sender, args[2])) return;

                int level = Math.abs(Integer.parseInt(args[2]));

                target.setLevel(Math.max(0, target.getLevel() - level));

                sender.sendMessage(Language.PREFIX + Language.EXPERIENCE_REMOVED
                    .replace("<player>", target.getName())
                    .replace("<level>", String.valueOf(level)));
                return;
            }
            case "set": {
                Player target = Bukkit.getPlayer(args[1]);

                if(!this.checkPlayer(sender, target, args[1])) return;
                if(!this.checkNumber(sender, args[2])) return;

                int level = Math.abs(Integer.parseInt(args[2]));

                target.setLevel(level);

                sender.sendMessage(Language.PREFIX + Language.EXPERIENCE_SET
                    .replace("<player>", target.getName())
                    .replace("<level>", String.valueOf(level)));
                return;
            }
            default: Language.EXPERIENCE_USAGE.forEach(sender::sendMessage);
        }
    }
}
