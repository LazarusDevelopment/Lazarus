package me.qiooip.lazarus.commands.manager;

import lombok.Setter;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.StringUtils;
import me.qiooip.lazarus.utils.Tasks;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseCommand extends BukkitCommand {

    private final boolean forPlayersOnly;
    @Setter private boolean executeAsync;

    public BaseCommand(String name) {
        this(name, new ArrayList<>());
    }

    public BaseCommand(String name, List<String> aliases) {
        this(name, aliases, false);
    }

    public BaseCommand(String name, String permission) {
        this(name, new ArrayList<>(), permission);
    }

    public BaseCommand(String name, boolean forPlayersOnly) {
        this(name, new ArrayList<>(), null, forPlayersOnly);
    }

    public BaseCommand(String name, List<String> aliases, String permission) {
        this(name, aliases, permission, false);
    }

    public BaseCommand(String name, List<String> aliases, boolean forPlayersOnly) {
        this(name, aliases, null, forPlayersOnly);
    }

    public BaseCommand(String name, String permission, boolean forPlayersOnly) {
        this(name, new ArrayList<>(), permission, forPlayersOnly);
    }

    public BaseCommand(String name, List<String> aliases, String permission, boolean forPlayersOnly) {
        super(name);

        this.setAliases(aliases);
        this.setPermission(permission);
        this.forPlayersOnly = forPlayersOnly;
    }

    protected boolean checkConsoleSender(CommandSender sender) {
        if(sender instanceof ConsoleCommandSender) {
            sender.sendMessage(Language.PREFIX + Language.COMMANDS_FOR_PLAYERS_ONLY);
            return false;
        }
        return true;
    }

    protected boolean checkOfflinePlayer(CommandSender sender, OfflinePlayer offlinePlayer, String name) {
        if(!offlinePlayer.hasPlayedBefore() && !offlinePlayer.isOnline()) {
            sender.sendMessage(Language.PREFIX + Language.COMMANDS_PLAYER_NOT_FOUND.replace("<player>", name));
            return false;
        }
        return true;
    }

    protected boolean checkPlayer(CommandSender sender, Player player, String name) {
        if(player == null) {
            sender.sendMessage(Language.PREFIX + Language.COMMANDS_PLAYER_NOT_ONLINE.replace("<player>", name));
            return false;
        }
        return true;
    }

    protected boolean checkNumber(CommandSender sender, String number) {
        if(!StringUtils.isInteger(number)) {
            sender.sendMessage(Language.PREFIX + Language.COMMANDS_INVALID_NUMBER);
            return false;
        }
        return true;
    }

    protected boolean checkPermission(CommandSender sender, String permission) {
        if(!sender.hasPermission(permission)) {
            sender.sendMessage(Language.PREFIX + Language.COMMANDS_NO_PERMISSION);
            return false;
        }
        return true;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if(this.forPlayersOnly && sender instanceof ConsoleCommandSender) {
            sender.sendMessage(Language.PREFIX + Language.COMMANDS_FOR_PLAYERS_ONLY);
            return true;
        }

        if(this.getPermission() != null && !sender.hasPermission(this.getPermission())) {
            sender.sendMessage(Language.PREFIX + Language.COMMANDS_NO_PERMISSION);
            return true;
        }

        if(this.executeAsync) {
            Tasks.async(() -> this.execute(sender, args));
        } else {
            this.execute(sender, args);
        }

        return true;
    }

    public abstract void execute(CommandSender sender, String[] args);
}