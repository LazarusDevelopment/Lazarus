package me.qiooip.lazarus.commands.staff;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.LocationUtils;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetExitCommand extends BaseCommand {

    public SetExitCommand() {
        super("setexit", "lazarus.setexit", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if(args.length == 0) {
            Language.SET_EXIT_USAGE.forEach(player::sendMessage);
            return;
        }

        switch(args[0].toLowerCase()) {
            case "nether":
                this.setExit(player, Environment.NETHER, "NETHER");
                return;
            case "end": {
                this.setExit(player, Environment.THE_END, "END");
                return;
            }
            default: Language.SET_EXIT_USAGE.forEach(player::sendMessage);
        }
    }

    private void setExit(Player player, Environment environment, String world) {
        Config.WORLD_EXITS.put(environment, player.getLocation());

        String worldName = StringUtils.getWorldName(player.getWorld());
        int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockY();
        int z = player.getLocation().getBlockZ();

        player.sendMessage(Language.PREFIX + Language.SET_EXIT_EXIT_SET
            .replace("<worldName>", StringUtils.capitalize(world.toLowerCase()))
            .replace("<world>", worldName)
            .replace("<x>", String.valueOf(x))
            .replace("<y>", String.valueOf(y))
            .replace("<z>", String.valueOf(z)));

        Lazarus.getInstance().getUtilitiesFile().set(world + "_EXIT", LocationUtils.locationToString(player.getLocation()));
        Lazarus.getInstance().getUtilitiesFile().save();
    }
}
