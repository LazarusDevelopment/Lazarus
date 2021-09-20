package me.qiooip.lazarus.factions.commands.player;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.enums.Role;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.utils.Messages;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenCommand extends SubCommand {

    public OpenCommand() {
        super("open", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(player);

        if(faction == null) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NOT_IN_FACTION_SELF);
            return;
        }

        if(faction.getMember(player).getRole() != Role.LEADER) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_MUST_BE_LEADER);
            return;
        }

        long diff = faction.getOpenChangeCooldown() - System.currentTimeMillis();

        if(diff >= 0) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_OPEN_COOLDOWN
                .replace("<time>", StringUtils.formatDurationWords(diff)));
            return;
        }

        faction.setOpenStatus(!faction.isOpen());

        Messages.sendMessage(faction.isOpen() ? Language.FACTIONS_OPEN_OPENED.replace("<name>",
        faction.getName()) : Language.FACTIONS_OPEN_CLOSED.replace("<name>", faction.getName()));
    }
}
