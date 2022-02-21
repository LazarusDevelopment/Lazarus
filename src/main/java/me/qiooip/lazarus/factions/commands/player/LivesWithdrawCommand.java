package me.qiooip.lazarus.factions.commands.player;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.enums.Role;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.userdata.Userdata;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class LivesWithdrawCommand extends SubCommand {

    public LivesWithdrawCommand() {
        super("liveswithdraw", Collections.singletonList("withdrawlives"), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_LIVES_WITHDRAW_USAGE);
            return;
        }

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

        if(!this.checkNumber(sender, args[0])) return;

        int amount = Math.abs(Integer.parseInt(args[0]));

        if(faction.getLives() < amount) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_LIVES_WITHDRAW_NOT_ENOUGH_LIVES);
            return;
        }

        Userdata data = Lazarus.getInstance().getUserdataManager().getUserdata(player);

        faction.setLives(faction.getLives() - amount);
        data.setLives(data.getLives() + amount);

        faction.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_LIVES_WITHDRAWN
            .replace("<player>", player.getName())
            .replace("<amount>", String.valueOf(amount)));
    }
}
