package me.qiooip.lazarus.userdata.event;

import lombok.Getter;
import me.qiooip.lazarus.userdata.Userdata;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class PlayerUsernameChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Userdata userdata;
    private final String newName;

    public PlayerUsernameChangeEvent(Userdata userdata, String newName) {
        this.userdata = userdata;
        this.newName = newName;
        Bukkit.getPluginManager().callEvent(this);
    }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}
