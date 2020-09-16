package me.qiooip.lazarus.abilities.commands;

import me.qiooip.lazarus.abilities.AbilitiesManager;
import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AbilityCommand extends BaseCommand {

    // TODO: temp command

    public AbilityCommand() {
        super("ability", "lazarus.ability", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        String ability = args[0];
        AbilityItem abilityItem = AbilitiesManager.getInstance().getEnabledAbilities().get(ability);

        player.getInventory().addItem(abilityItem.getItem());
    }
}