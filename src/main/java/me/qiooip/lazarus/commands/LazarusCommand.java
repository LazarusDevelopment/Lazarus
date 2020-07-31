package me.qiooip.lazarus.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.Color;
import org.bukkit.command.CommandSender;

public class LazarusCommand extends BaseCommand {

    public LazarusCommand() {
        super("lazarus");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length != 1 || !args[0].equalsIgnoreCase("reload")) {
            this.sendPluginInfo(sender);
            return;
        }

        if(!sender.hasPermission("lazarus.reload")) {
            this.sendPluginInfo(sender);
            return;
        }

        this.reloadConfigs(sender);
    }

    private void sendPluginInfo(CommandSender sender) {
        sender.sendMessage(Color.translate("&4&m---&8&m------------------------------------&4&m---"));
        sender.sendMessage(Color.translate(" &7Lazarus core made by: &cqIooIp"));
        sender.sendMessage(Color.translate(" &7Plugin version: &c" + Lazarus.getInstance().getDescription().getVersion()));
        sender.sendMessage(Color.translate("&4&m---&8&m------------------------------------&4&m---"));
    }

    private void reloadConfigs(CommandSender sender) {
        long startTime = System.currentTimeMillis();

        Lazarus.getInstance().setConfig(new ConfigFile("config.yml"));
        Lazarus.getInstance().setLanguage(new ConfigFile("language.yml"));
        Lazarus.getInstance().setScoreboardFile(new ConfigFile("scoreboard.yml"));
        Lazarus.getInstance().setTabFile(new ConfigFile("tab.yml"));
        Lazarus.getInstance().setClassesFile(new ConfigFile("classes.yml"));
        Lazarus.getInstance().setLimitersFile(new ConfigFile("limiters.yml"));
        Lazarus.getInstance().setItemsFile(new ConfigFile("items.yml"));

        new Config();
        new Language();

        sender.sendMessage(Language.PREFIX + Language.PLUGIN_RELOAD_MESSAGE.replace("<time>",
        String.valueOf(System.currentTimeMillis() - startTime)));
    }
}
