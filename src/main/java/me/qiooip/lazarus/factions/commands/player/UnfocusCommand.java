package me.qiooip.lazarus.factions.commands.player;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.enums.Role;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnfocusCommand extends SubCommand {

    public UnfocusCommand() {
        super("unfocus", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(player);

        if(faction == null) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NOT_IN_FACTION_SELF);
            return;
        }

        if(!faction.getMember(player).getRole().isAtLeast(Role.CAPTAIN)) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NO_PERMISSION
                .replace("<role>", Role.CAPTAIN.getName()));
            return;
        }

        if(faction.getFocusedFaction() == null) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_UNFOCUS_NOT_FOCUSING);
            return;
        }

        PlayerFaction target = faction.getFocusedAsFaction();

        if(target == null) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_UNFOCUS_FACTION_DOESNT_EXIST);
            faction.setFocusedFaction(null);
            return;
        }

        faction.unfocusFaction(target);
        player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_UNFOCUS_UNFOCUSED.replace("<faction>", target.getName()));
    }
}
