package me.qiooip.lazarus.kits.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.commands.manager.SubCommandExecutor;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.kits.kit.KitData;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

public class KitCommandExecutor extends SubCommandExecutor {

    public KitCommandExecutor() {
        super("kit", Collections.singletonList("kits"), Language.KITS_COMMAND_USAGE_ADMIN);

        this.setPrefix(Language.KIT_PREFIX);

        this.addSubCommand(new KitCreateCommand());
        this.addSubCommand(new KitDelayCommand());
        this.addSubCommand(new KitEditCommand());
        this.addSubCommand(new KitGiveCommand());
        this.addSubCommand(new KitListCommand());
        this.addSubCommand(new KitRemoveCommand());
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if(args.length == 0) {
            if(sender.hasPermission("lazarus.kits.admin")) {
                this.getUsageMessage(sender).forEach(sender::sendMessage);
                return true;
            }

            Language.KITS_COMMAND_USAGE_PLAYER.forEach(sender::sendMessage);
            return true;
        }

        SubCommand sub = this.getSubCommand(args[0]);

        if(sub == null) {
            KitData kit = Lazarus.getInstance().getKitsManager().getKit(args[0]);

            if(kit != null && sender instanceof Player) {
                Player player = (Player) sender;
                Lazarus.getInstance().getKitsManager().giveKit(player, kit);
                return true;
            }

            if(kit != null && sender instanceof ConsoleCommandSender) {
                sender.sendMessage(Language.KIT_PREFIX + Language.COMMANDS_FOR_PLAYERS_ONLY);
                return true;
            }

            sender.sendMessage(Language.KIT_PREFIX + Language.KITS_EXCEPTION_DOESNT_EXISTS.replace("<kit>", args[0]));
            return true;
        }

        if(sub.isForPlayersOnly() && sender instanceof ConsoleCommandSender) {
            sender.sendMessage(Language.KIT_PREFIX + Language.COMMANDS_FOR_PLAYERS_ONLY);
            return true;
        }

        if(sub.getPermission() != null && !sender.hasPermission(sub.getPermission())) {
            sender.sendMessage(Language.KIT_PREFIX + Language.COMMANDS_NO_PERMISSION);
            return true;
        }

        sub.execute(sender, Arrays.copyOfRange(args, 1, args.length));
        return true;
    }
}
