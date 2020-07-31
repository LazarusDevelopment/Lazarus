package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class SpawnerCommand extends BaseCommand {

    public SpawnerCommand() {
        super("spawner", "lazarus.spawner", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if(args.length == 0) {
            player.sendMessage(Language.PREFIX + Language.SPAWNER_USAGE);
            return;
        }

        EntityType entityType;
        try {
            entityType = EntityType.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage(Language.PREFIX + Language.ENTITIES_INVALID_ENTITY.replace("<type>", args[0]));
            return;
        }

        Block block = player.getTargetBlock((HashSet<Byte>) null, 5);

        if(block == null || block.getType() != Material.MOB_SPAWNER) {
            player.sendMessage(Language.PREFIX + Language.SPAWNER_MUST_LOOK_AT_SPAWNER);
            return;
        }

        CreatureSpawner spawner = (CreatureSpawner) block.getState();
        spawner.setSpawnedType(entityType);
        spawner.update();

        player.sendMessage(Language.PREFIX + Language.SPAWNER_UPDATED_SPAWNER.replace("<type>", entityType.name()));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String label, String[] args) {
        if(args.length != 1 || !sender.hasPermission("lazarus.spawner")) return null;

        List<String> completions = new ArrayList<>();

        for(EntityType type : EntityType.values()) {
            if(!type.isAlive() || !type.name().startsWith(args[0].toUpperCase())) continue;

            completions.add(type.name());
        }

        Collections.sort(completions);
        return completions;
    }
}
