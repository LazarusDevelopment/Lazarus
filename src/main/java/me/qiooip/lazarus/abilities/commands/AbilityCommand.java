package me.qiooip.lazarus.abilities.commands;

import me.qiooip.lazarus.abilities.AbilitiesManager;
import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class AbilityCommand extends BaseCommand {

    public AbilityCommand() {
        super("ability", Collections.singletonList("abilities"), "lazarus.ability");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        String abilityName = args[0];

        AbilityType type = AbilityType.getByName(abilityName);
        AbilityItem abilityItem = AbilitiesManager.getInstance().getEnabledAbilities().get(type);

        player.getInventory().addItem(abilityItem.getItem());
    }
}