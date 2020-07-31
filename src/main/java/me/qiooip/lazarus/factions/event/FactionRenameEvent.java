package me.qiooip.lazarus.factions.event;

import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.factions.Faction;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;

@Getter
public class FactionRenameEvent extends FactionEvent implements Cancellable {

    private final String newName;
    private final Faction faction;
    private final CommandSender sender;
    private final boolean forceRename;
    @Setter private boolean cancelled;

    public FactionRenameEvent(String newName, Faction faction, CommandSender sender, boolean forceRename) {
        this.newName = newName;
        this.faction = faction;
        this.sender = sender;
        this.forceRename = forceRename;

        Bukkit.getPluginManager().callEvent(this);
    }
}
