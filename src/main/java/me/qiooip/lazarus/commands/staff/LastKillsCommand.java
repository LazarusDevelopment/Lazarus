package me.qiooip.lazarus.commands.staff;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.userdata.Userdata;
import me.qiooip.lazarus.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LastKillsCommand extends BaseCommand {

	public LastKillsCommand() {
		super("lastkills", "lazarus.lastkills");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length == 0) {
            sender.sendMessage(Language.PREFIX + Language.LAST_KILLS_COMMAND_USAGE);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if(!this.checkPlayer(sender, target, args[0])) return;

        Userdata data = Lazarus.getInstance().getUserdataManager().getUserdata(target);
				
        if(data.getLastKills().isEmpty()) {
            sender.sendMessage(Language.PREFIX + Language.LAST_KILLS_COMMAND_NO_KILLS_YET
                .replace("<player>", target.getName()));
            return;
        }
				
        sender.sendMessage("");

        sender.sendMessage(Language.LAST_KILLS_COMMAND_HEADER_FORMAT.replace("<player>", target.getName()));

        data.getLastKills().forEach(death -> sender.sendMessage(Language.LAST_KILLS_COMMAND_KILL_MESSAGE_FORMAT
            .replace("<killMessage>", Color.translate(death))));

        sender.sendMessage("");
	}
}
