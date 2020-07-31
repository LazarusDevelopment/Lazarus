package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeleteWarpCommand extends BaseCommand {

    public DeleteWarpCommand() {
        super("deletewarp", Collections.singletonList("delwarp"), "lazarus.delwarp");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.PREFIX + Language.DEL_WARP_USAGE);
            return;
        }

        if(!Lazarus.getInstance().getWarpsHandler().deleteWarp(args[0])) {
            sender.sendMessage(Language.PREFIX + Language.DEL_WARP_DOESNT_EXIST.replace("<name>", args[0]));
            return;
        }

        sender.sendMessage(Language.PREFIX + Language.DEL_WARP_DELETED.replace("<name>", args[0]));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String label, String[] args) {
        if(args.length != 1 || !sender.hasPermission("lazarus.delwarp")) return null;

        List<String> completions = new ArrayList<>();

        for(String warpName : Lazarus.getInstance().getWarpsHandler().getWarpNames()) {
            if(!warpName.toLowerCase().startsWith(args[0].toLowerCase())) continue;

            completions.add(warpName);
        }

        Collections.sort(completions);
        return completions;
    }
}
