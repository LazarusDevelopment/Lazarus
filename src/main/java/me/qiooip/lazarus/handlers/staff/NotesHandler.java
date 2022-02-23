package me.qiooip.lazarus.handlers.staff;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.userdata.Userdata;
import me.qiooip.lazarus.utils.Messages;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.stream.IntStream;

public class NotesHandler extends Handler implements Listener {

    public void addNote(CommandSender sender, Player target, String[] args) {
        String note = StringUtils.joinArray(args, " ", 3);

        Userdata data = Lazarus.getInstance().getUserdataManager().getUserdata(target);
        data.getNotes().add(note);

        sender.sendMessage(Language.PREFIX + Language.NOTES_COMMAND_NOTE_ADDED
            .replace("<player>", target.getName()));
    }

    public void removeNote(CommandSender sender, Player target, String id) {
        if(!StringUtils.isInteger(id)) {
            sender.sendMessage(Language.PREFIX + Language.COMMANDS_INVALID_NUMBER);
            return;
        }

        Userdata data = Lazarus.getInstance().getUserdataManager().getUserdata(target);

        if(data.getNotes().isEmpty()) {
            sender.sendMessage(Language.PREFIX + Language.NOTES_COMMAND_NO_NOTES
                .replace("<player>", target.getName()));
            return;
        }

        int noteId = Math.abs(Integer.parseInt(id));

        if((data.getNotes().size() < noteId) || (noteId == 0)) {
            sender.sendMessage(Language.PREFIX + Language.NOTES_COMMAND_NOTE_DOESNT_EXIST
                .replace("<id>", String.valueOf(noteId)));
            return;
        }

        data.getNotes().remove(noteId - 1);

        sender.sendMessage(Language.PREFIX + Language.NOTES_COMMAND_NOTE_REMOVED
            .replace("<id>", String.valueOf(noteId)));
    }

    public void listNotes(CommandSender sender, Player target) {
        Userdata data = Lazarus.getInstance().getUserdataManager().getUserdata(target);

        if(data.getNotes().isEmpty()) {
            sender.sendMessage(Language.PREFIX + Language.NOTES_COMMAND_NO_NOTES
                .replace("<player>", target.getName()));
            return;
        }

        sender.sendMessage("");
        sender.sendMessage(Language.PREFIX + Language.NOTES_COMMAND_MESSAGE.replace("<player>", target.getName()));

        IntStream.rangeClosed(1, data.getNotes().size()).forEach(i ->
            sender.sendMessage(Language.NOTES_COMMAND_FORMAT
                .replace("<id>", String.valueOf(i))
                .replace("<note>", data.getNotes().get(i - 1))));

        sender.sendMessage("");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Userdata data = Lazarus.getInstance().getUserdataManager().getUserdata(event.getPlayer());
        if(data.getNotes().isEmpty()) return;

        Messages.sendMessage("", "lazarus.staff");

        Messages.sendMessage(Language.PREFIX + Language.NOTES_COMMAND_MESSAGE
            .replace("<player>", event.getPlayer().getName()), "lazarus.staff");

        IntStream.rangeClosed(1, data.getNotes().size()).forEach(i ->
            Messages.sendMessage(Language.NOTES_COMMAND_FORMAT
                .replace("<id>", String.valueOf(i))
                .replace("<note>", data.getNotes().get(i - 1)), "lazarus.staff"));

        Messages.sendMessage("", "lazarus.staff");
    }
}
