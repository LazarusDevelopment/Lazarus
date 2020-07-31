package me.qiooip.lazarus.games.conquest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.ChatColor;

@AllArgsConstructor
public enum ZoneType {

    RED(ChatColor.RED),
    BLUE(ChatColor.AQUA),
    YELLOW(ChatColor.YELLOW),
    GREEN(ChatColor.GREEN);

    @Getter private final ChatColor color;

    public String getName() {
        return this.color + StringUtils.capitalize(this.name().toLowerCase());
    }
}
