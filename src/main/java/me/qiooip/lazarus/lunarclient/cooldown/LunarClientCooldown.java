package me.qiooip.lazarus.lunarclient.cooldown;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.common.icon.Icon;
import com.lunarclient.apollo.common.icon.ItemStackIcon;
import com.lunarclient.apollo.module.cooldown.Cooldown;
import com.lunarclient.apollo.module.cooldown.CooldownModule;
import me.qiooip.lazarus.utils.ApolloUtils;
import org.bukkit.Material;

import java.time.Duration;
import java.util.UUID;

public class LunarClientCooldown {

    private final static CooldownModule MODULE = Apollo.getModuleManager().getModule(CooldownModule.class);

    private final String name;
    private final Icon icon;

    public LunarClientCooldown(String name, Material material) {
        this.name = name;

        this.icon = ItemStackIcon.builder()
            .itemId(material.getId())
            .itemName(material.name())
            .build();
    }

    public void createCooldown(UUID playerId, int duration) {
        Cooldown cooldown = Cooldown.builder()
            .name(this.name)
            .icon(this.icon)
            .duration(Duration.ofSeconds(duration))
            .build();

        ApolloUtils.runForPlayer(playerId, ap -> MODULE.displayCooldown(ap, cooldown));
    }

    public void clearCooldown(UUID playerId) {
        ApolloUtils.runForPlayer(playerId, ap -> MODULE.removeCooldown(ap, this.name));
    }
}
