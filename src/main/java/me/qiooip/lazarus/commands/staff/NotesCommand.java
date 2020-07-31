package me.qiooip.lazarus.commands.staff;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NotesCommand extends BaseCommand {

    public NotesCommand() {
        super("notes", "lazarus.notes");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2) {
            Language.NOTES_COMMAND_USAGE.forEach(sender::sendMessage);
            return;
        }

        if(args.length == 2 && args[0].equalsIgnoreCase("check")) {

            Player target = Bukkit.getPlayer(args[1]);
            if(!this.checkPlayer(sender, target, args[1])) return;

            Lazarus.getInstance().getNotesHandler().listNotes(sender, target);

        } else if(args.length == 3 && args[0].equalsIgnoreCase("remove")) {

            Player target = Bukkit.getPlayer(args[1]);

            if(target == null) {
                sender.sendMessage(Language.PREFIX + Language.COMMANDS_PLAYER_NOT_ONLINE.replace("<player>", args[1]));
                return;
            }

            Lazarus.getInstance().getNotesHandler().removeNote(sender, target, args[2]);

        } else {
            if(!args[0].equalsIgnoreCase("add")) {
                Language.NOTES_COMMAND_USAGE.forEach(sender::sendMessage);
                return;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if(!this.checkPlayer(sender, target, args[1])) return;

            Lazarus.getInstance().getNotesHandler().addNote(sender, target, args);
        }
    }
}
