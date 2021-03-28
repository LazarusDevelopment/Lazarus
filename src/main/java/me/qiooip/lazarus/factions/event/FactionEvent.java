package me.qiooip.lazarus.factions.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

class FactionEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    protected FactionEvent() {
        super();
    }

    protected FactionEvent(boolean isAsync) {
        super(isAsync);
    }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}
