package me.qiooip.lazarus.factions.commands.admin;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import org.bukkit.command.CommandSender;

public class SaveCommand extends SubCommand {

    public SaveCommand() {
        super("save", "lazarus.factions.save");

        this.setExecuteAsync(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Lazarus.getInstance().log("&3===&b=============================================&3===");

        FactionsManager.getInstance().saveFactions(true, false);
        FactionsManager.getInstance().savePlayers(true);
        ClaimManager.getInstance().saveClaims(true);

        Lazarus.getInstance().log("&3===&b=============================================&3===");

        sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_SAVED);
    }
}
