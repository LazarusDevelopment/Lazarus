package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Monster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public class KillallCommand extends BaseCommand {

    private static final String[] KILL_TYPES = new String[] { "ALL", "MOBS", "ANIMALS", "ITEMS" };

    public KillallCommand() {
        super("killall", "lazarus.killall");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.PREFIX + Language.KILLALL_USAGE);
            return;
        }

        switch(args[0].toLowerCase()) {
            case "all": {
                this.removeEntities(sender, "entities", null);
                return;
            }
            case "mobs": {
                this.removeEntities(sender, "mobs", null);
                return;
            }
            case "animals": {
                this.removeEntities(sender, "animals", null);
                return;
            }
            case "items": {
                this.removeEntities(sender, "items", null);
                return;
            }
            default: {
                try {
                    EntityType.valueOf(args[0].toUpperCase());
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(Language.PREFIX + Language.ENTITIES_INVALID_ENTITY.replace("<type>", args[0]));
                    return;
                }

                EntityType type = EntityType.valueOf(args[0].toUpperCase());
                this.removeEntities(sender, StringUtils.getEntityName(type.name()), type);
            }
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String label, String[] args) {
        if(args.length != 1 || !sender.hasPermission("lazarus.killall")) return null;

        List<String> completions = new ArrayList<>();

        for(String killType : KILL_TYPES) {
            if(!killType.startsWith(args[0].toUpperCase())) continue;

            completions.add(killType);
        }

        for(EntityType type : EntityType.values()) {
            if(!type.isSpawnable() || !type.name().startsWith(args[0].toUpperCase())) continue;

            completions.add(type.name());
        }

        Collections.sort(completions);
        return completions;
    }

    private void removeEntities(CommandSender sender, String type, EntityType entityType) {
        AtomicInteger total = new AtomicInteger();
        AtomicReference<Predicate<Entity>> predicate = new AtomicReference<>();

        switch(type) {
            case "entities": predicate.set(entity -> entity instanceof Creature || entity instanceof Item); break;
            case "mobs": predicate.set(entity -> entity instanceof Monster); break;
            case "animals": predicate.set(entity -> entity instanceof Animals); break;
            case "items": predicate.set(entity -> entity instanceof Item); break;
            default: predicate.set(entity -> entity.getType() == entityType);
        }

        Bukkit.getWorlds().forEach(world -> world.getEntities().stream().filter(predicate.get()).forEach(entity -> {
            entity.remove();
            total.getAndIncrement();
        }));

        sender.sendMessage(Language.PREFIX + Language.KILLALL_KILLED.replace("<type>", type));
        sender.sendMessage(Language.PREFIX + Language.KILLALL_COUNT.replace("<amount>", String.valueOf(total.get())));
    }
}
