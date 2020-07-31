package me.qiooip.lazarus.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ListCommand extends BaseCommand {

    public ListCommand() {
        super("list", "lazarus.list");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        List<String> staff = new ArrayList<>();

        Bukkit.getOnlinePlayers().stream().filter(online -> online.hasPermission("lazarus.staff")).forEach(online -> {
            if(!Config.LIST_SHOW_VANISHED_STAFF && Lazarus.getInstance().getVanishManager().isVanished(online)) return;

            staff.add(online.getName());
        });

        String onlineStaff = staff.isEmpty() ? Config.LIST_NO_STAFF_ONLINE : String.join(", ", staff);

        int online = Config.LIST_SHOW_VANISHED_STAFF ? Bukkit.getOnlinePlayers().size()
        : Bukkit.getOnlinePlayers().size() - Lazarus.getInstance().getVanishManager().vanishedAmount();

        Language.LIST_COMMAND.forEach(message ->
            sender.sendMessage(message.replace("<max>", String.valueOf(Bukkit.getMaxPlayers()))
            .replace("<online>", String.valueOf(online)).replace("<staffonline>", onlineStaff))
        );
    }
}
