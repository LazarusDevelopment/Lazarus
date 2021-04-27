package me.qiooip.lazarus.lunarclient.cooldown;

import com.lunarclient.bukkitapi.cooldown.LCCooldown;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

import java.util.concurrent.TimeUnit;

@Getter @Setter
public class LunarClientCooldown {

    private String name;
    private Material material;

    public LCCooldown createCooldown(int duration) {
        return new LCCooldown(this.name, duration, TimeUnit.SECONDS, this.material);
    }

    public LCCooldown clearCooldown() {
        return new LCCooldown(this.name, 1, TimeUnit.SECONDS, this.material);
    }
}
