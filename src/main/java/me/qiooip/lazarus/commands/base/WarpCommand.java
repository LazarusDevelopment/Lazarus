package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WarpCommand extends BaseCommand {

    public WarpCommand() {
        super("warp", Collections.singletonList("warps"), "lazarus.warp", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            Lazarus.getInstance().getWarpsHandler().listWarps(sender);
            return;
        }

        Location warp = Lazarus.getInstance().getWarpsHandler().getWarp(args[0]);

        if(warp == null) {
            sender.sendMessage(Language.PREFIX + Language.WARP_DOESNT_EXIST.replace("<name>", args[0]));
            return;
        }

        Player player = (Player) sender;
        if(!player.teleport(warp)) return;

        player.sendMessage(Language.PREFIX + Language.WARP_TELEPORTED.replace("<name>", StringUtils.capitalize(args[0])));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String label, String[] args) {
        if(args.length != 1 || !sender.hasPermission("lazarus.warp")) return null;

        List<String> completions = new ArrayList<>();

        for(String warpName : Lazarus.getInstance().getWarpsHandler().getWarpNames()) {
            if(!warpName.toLowerCase().startsWith(args[0].toLowerCase())) continue;

            completions.add(warpName);
        }

        Collections.sort(completions);
        return completions;
    }
}
