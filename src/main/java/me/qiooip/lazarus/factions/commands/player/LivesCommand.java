package me.qiooip.lazarus.factions.commands.player;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LivesCommand extends SubCommand {

    public LivesCommand() {
        super("lives");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            if(!this.checkConsoleSender(sender)) return;

            Player player = (Player) sender;
            PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(player);

            if(faction == null) {
                player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NOT_IN_FACTION_SELF);
                return;
            }

            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_LIVES_SELF
            .replace("<amount>", String.valueOf(faction.getLives())));
            return;
        }

        PlayerFaction faction = FactionsManager.getInstance().searchForFaction(args[0]);

        if(faction == null) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FACTION_DOESNT_EXIST.replace("<argument>", args[0]));
            return;
        }

        sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_LIVES_OTHERS.replace("<faction>",
        faction.getName(sender)).replace("<amount>", String.valueOf(faction.getLives())));
    }
}
