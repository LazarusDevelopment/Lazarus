package me.qiooip.lazarus.factions.claim;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.glass.GlassManager.GlassType;
import me.qiooip.lazarus.utils.item.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class FactionMap {

    private final Material[] mapMaterials;
    private final Set<UUID> mapUsers;

    FactionMap() {
        this.mapMaterials = new Material[] {
            Material.IRON_ORE, Material.GOLD_ORE, Material.COAL_ORE, Material.DIAMOND_ORE,
            Material.EMERALD_ORE, Material.LAPIS_ORE, Material.QUARTZ_ORE, Material.REDSTONE_ORE,
            Material.NETHERRACK, Material.ENDER_STONE, Material.NETHER_BRICK, Material.STONE,
            Material.HARD_CLAY, Material.SMOOTH_BRICK, Material.IRON_BLOCK, Material.GOLD_BLOCK,
            Material.EMERALD_BLOCK, Material.DIAMOND_BLOCK, Material.COBBLESTONE, Material.BRICK
        };

        this.mapUsers = new HashSet<>();
    }

    public void disable() {
        this.mapUsers.clear();
    }

    public void removeFromMapUsers(Player player) {
        this.mapUsers.remove(player.getUniqueId());
    }

    public void showFactionMap(Player player) {
        if(this.mapUsers.remove(player.getUniqueId())) {
            Lazarus.getInstance().getGlassManager().clearGlassVisuals(player, GlassType.CLAIM_MAP);
            player.sendMessage(Language.FACTIONS_MAP_DISABLED);
            return;
        }

        int x1 = player.getLocation().getBlockX() - Config.FACTION_MAP_RADIUS;
        int x2 = player.getLocation().getBlockX() + Config.FACTION_MAP_RADIUS;
        int z1 = player.getLocation().getBlockZ() - Config.FACTION_MAP_RADIUS;
        int z2 = player.getLocation().getBlockZ() + Config.FACTION_MAP_RADIUS;

        Set<Claim> claims = ClaimManager.getInstance().getClaimsInSelection(player.getWorld(), x1, x2, z1, z2);

        if(claims.isEmpty()) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_MAP_NO_NEARBY_CLAIMS
                .replace("<radius>", String.valueOf(Config.FACTION_MAP_RADIUS)));
            return;
        }

        this.mapUsers.add(player.getUniqueId());

        Map<UUID, Material> materialMap = new HashMap<>();
        AtomicInteger count = new AtomicInteger(0);

        claims.forEach(claim -> {
            Material material = materialMap.get(claim.getOwner().getId());

            if(material == null) {
                material = this.mapMaterials[count.getAndIncrement()];
                materialMap.put(claim.getOwner().getId(), material);

                player.sendMessage(Language.FACTIONS_MAP_OWNER_MESSAGE
                    .replace("<faction>", claim.getOwner().getDisplayName(player))
                    .replace("<material>", ItemUtils.getMaterialName(material)));
            }

            claim.showPillars(player, material);
        });
    }
}
