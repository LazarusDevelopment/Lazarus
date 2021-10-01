package me.qiooip.lazarus.factions.claim;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.event.FactionClaimChangeEvent;
import me.qiooip.lazarus.factions.event.FactionClaimChangeEvent.ClaimChangeReason;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.factions.type.RoadFaction;
import me.qiooip.lazarus.factions.type.SystemFaction;
import me.qiooip.lazarus.factions.type.WarzoneFaction;
import me.qiooip.lazarus.factions.type.WildernessFaction;
import me.qiooip.lazarus.glass.GlassInfo;
import me.qiooip.lazarus.glass.GlassManager.GlassType;
import me.qiooip.lazarus.selection.Selection;
import me.qiooip.lazarus.selection.SelectionType;
import me.qiooip.lazarus.utils.FileUtils;
import me.qiooip.lazarus.utils.GsonUtils;
import me.qiooip.lazarus.utils.StringUtils;
import me.qiooip.lazarus.utils.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Getter
public class ClaimManager implements Listener {

    @Getter private static ClaimManager instance;

    private final FactionMap factionMap;
    private final File claimsFile;

    protected List<Claim> claims;
    private final Table<String, Long, Claim> claimCache;

    private final WarzoneFaction warzone;
    private final WildernessFaction wilderness;

    public ClaimManager() {
        instance = this;

        this.factionMap = new FactionMap();

        this.claimsFile = FileUtils.getOrCreateFile(Config.FACTIONS_DIR, "claims.json");
        this.claimCache = HashBasedTable.create();

        this.warzone = new WarzoneFaction();
        this.wilderness = new WildernessFaction();

        this.loadClaims();

        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    public void disable() {
        this.saveClaims(true);

        this.claims.clear();
        this.claimCache.clear();

        this.factionMap.disable();
    }

    protected void loadClaims() {
        String content = FileUtils.readWholeFile(this.claimsFile);

        if(content == null) {
            this.claims = new ArrayList<>();
            return;
        }

        this.claims = Lazarus.getInstance().getGson().fromJson(content, GsonUtils.CLAIM_TYPE);

        this.claims.forEach(claim -> {
            Faction claimOwner = claim.getOwner();

            if(claimOwner != null) {
                this.cacheClaim(claim);
                claimOwner.addClaim(claim);
            }
        });

        Lazarus.getInstance().log("- &7Loaded &a" + this.claims.size() + " &7claims.");
    }

    public void saveClaims(boolean log) {
        if(this.claims == null) return;

        FileUtils.writeString(this.claimsFile, Lazarus.getInstance().getGson()
            .toJson(this.claims, GsonUtils.CLAIM_TYPE));

        if(log) {
            Lazarus.getInstance().log("- &7Saved &a" + this.claims.size() + " &7claims.");
        }
    }

    public Faction getFactionAt(Block block) {
        return this.getFactionAt(block.getLocation());
    }

    public Faction getFactionAt(Player player) {
        return this.getFactionAt(player.getLocation());
    }

    public Faction getFactionAt(Location location) {
        return this.getFactionAt(location.getWorld(), location.getBlockX(), location.getBlockZ());
    }

    private Faction getFactionAt(World world, int x, int z) {
        Claim claim = this.getClaimAt(world, x, z);
        if(claim != null) return claim.getOwner();

        int warzone = Config.WARZONE_RADIUS.get(world.getEnvironment());
        return Math.abs(x) > warzone || Math.abs(z) > warzone ? this.wilderness : this.warzone;
    }

    private long hash(int x, int z) {
        return (long) x << 32 | z & 0xFFFFFFFFL;
    }

    public Claim getClaimAt(Block block) {
        return this.getClaimAt(block.getLocation());
    }

    public Claim getClaimAt(Player player) {
        return this.getClaimAt(player.getLocation());
    }

    public Claim getClaimAt(Location location) {
        return this.getClaimAt(location.getWorld(), location.getBlockX(), location.getBlockZ());
    }

    private Claim getClaimAt(World world, int x, int z) {
        return this.claimCache.get(world.getName(), this.hash(x, z));
    }

    public boolean addClaim(Claim claim) {
        FactionClaimChangeEvent event = new FactionClaimChangeEvent(claim.getOwner(),
            Collections.singletonList(claim), ClaimChangeReason.CLAIM);

        if(event.isCancelled()) return false;

        this.claims.add(claim);
        this.cacheClaim(claim);
        claim.getOwner().addClaim(claim);

        return true;
    }

    public boolean removeClaim(Claim claim) {
        FactionClaimChangeEvent event = new FactionClaimChangeEvent(claim.getOwner(),
            Collections.singletonList(claim), ClaimChangeReason.UNCLAIM);

        if(event.isCancelled()) return false;

        this.claims.remove(claim);
        this.uncacheClaim(claim);
        claim.getOwner().removeClaim(claim);

        return true;
    }

    public boolean removeAllClaims(Faction faction, ClaimChangeReason reason) {
        List<Claim> claims = faction.getClaims();
        if(claims.isEmpty()) return true;

        FactionClaimChangeEvent event = new FactionClaimChangeEvent(faction, claims, reason);
        if(event.isCancelled() && reason != ClaimChangeReason.DISBAND) return false;

        Iterator<Claim> iterator = claims.iterator();

        while(iterator.hasNext()) {
            Claim claim = iterator.next();

            this.claims.remove(claim);
            this.uncacheClaim(claim);

            iterator.remove();
        }

        return true;
    }

    public void deleteAllPlayerFactionClaims() {
        int claimsSize = this.claims.size();
        Iterator<Claim> iterator = this.claims.iterator();

        while(iterator.hasNext()) {
            Claim claim = iterator.next();
            if(claim.getOwner() instanceof SystemFaction) continue;

            this.uncacheClaim(claim);
            iterator.remove();
        }

        this.saveClaims(false);
        Lazarus.getInstance().log("- &cDeleted &e" + (claimsSize - this.claims.size()) + " &cplayer faction claims.");
    }

    protected void cacheClaim(Claim claim) {
        IntStream.rangeClosed(claim.getMinX(), claim.getMaxX()).forEach(x
            -> IntStream.rangeClosed(claim.getMinZ(), claim.getMaxZ()).forEach(z
                -> this.claimCache.put(claim.getWorldName(), this.hash(x, z), claim)));
    }

    protected void uncacheClaim(Claim claim) {
        IntStream.rangeClosed(claim.getMinX(), claim.getMaxX()).forEach(x
            -> IntStream.rangeClosed(claim.getMinZ(), claim.getMaxZ()).forEach(z
                -> this.claimCache.remove(claim.getWorldName(), this.hash(x, z))));
    }

    public Location getSafeLocation(Player player) {
        Location location = player.getLocation().clone();
        Claim claim;

        while((claim = this.getClaimAt(location)) != null) {
            location = claim.getFirstSafePosition();
        }

        if(location.equals(player.getLocation())) {
            location.add(location.getBlockX() > 0 ? 1 : -1, 0, location.getBlockZ() > 0 ? 1 : -1);
        }

        location.setY(player.getWorld().getHighestBlockYAt(location.getBlockX(), location.getBlockZ()));

        return location;
    }

    public Location getSafeLocation(Claim claimAt) {
        Location location = claimAt.getFirstSafePosition().clone();
        Claim claim;

        while((claim = this.getClaimAt(location)) != null) {
            location = claim.getFirstSafePosition();
        }

        location.setY(location.getWorld().getHighestBlockYAt(location.getBlockX(), location.getBlockZ()));

        return location;
    }

    public Set<Claim> getClaimsInSelection(Location posOne, Location posTwo) {
        return this.getClaimsInSelection(posOne.getWorld(), posOne.getBlockX(),
            posTwo.getBlockX(), posOne.getBlockZ(), posTwo.getBlockZ());
    }

    public Set<Claim> getClaimsInSelection(World world, int x1, int x2, int z1, int z2) {
        Set<Claim> claims = new HashSet<>();

        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        int minZ = Math.min(z1, z2);
        int maxZ = Math.max(z1, z2);

        Claim claimAt;

        for(int x = minX; x <= maxX; x += Config.FACTION_MIN_CLAIM_SIZE) {
            for(int z = minZ; z <= maxZ; z += Config.FACTION_MIN_CLAIM_SIZE) {
                claimAt = this.getClaimAt(world, x, z);
                if(claimAt != null) claims.add(claimAt);

                claimAt = this.getClaimAt(world, maxX, z);
                if(claimAt != null) claims.add(claimAt);
            }

            claimAt = this.getClaimAt(world, x, maxZ);
            if(claimAt != null) claims.add(claimAt);
        }

        claimAt = this.getClaimAt(world, maxX, maxZ);
        if(claimAt != null) claims.add(claimAt);

        return claims;
    }

    private boolean isWarzone(World world, int x1, int x2, int z1, int z2) {
        int warzone = Config.WARZONE_RADIUS.get(world.getEnvironment());

        return (Math.abs(x1) <= warzone && Math.abs(z1) <= warzone)
            || (Math.abs(x2) <= warzone && Math.abs(z2) <= warzone);
    }

    public boolean isSelectionConnected(Faction faction, Claim claim) {
        if(!Config.FACTION_CLAIMS_MUST_BE_CONNECTED) return true;

        List<Claim> claims = faction.getClaims();
        if(claims.isEmpty()) return true;

        int halfX = (claim.getMinX() + claim.getMaxX()) / 2;
        int halfZ = (claim.getMinZ() + claim.getMaxZ()) / 2;

        for(Claim existingClaim : claims) {
            Claim existingClaimCopy = existingClaim.clone().expand(1);

            for(Location corner : claim.getCorners()) {
                if(existingClaimCopy.contains(corner)) return true;
            }

            if(existingClaimCopy.contains(claim.getWorld(), halfX, claim.getMinZ())
                || existingClaimCopy.contains(claim.getWorld(), halfX, claim.getMaxZ())
                || existingClaimCopy.contains(claim.getWorld(), claim.getMinX(), halfZ)
                || existingClaimCopy.contains(claim.getWorld(), claim.getMaxX(), halfZ)) return true;
        }

        return false;
    }

    public boolean canClaimSelection(Faction faction, Claim claim, Player player, boolean claimChunk) {
        if(this.isWarzone(claim.getWorld(), claim.getMinX(), claim.getMaxX(), claim.getMinZ(), claim.getMaxZ())) {
            if(claimChunk) {
                player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CLAIM_CANNOT_CLAIM_WARZONE);
            }

            return false;
        }

        Set<Claim> claims = this.getClaimsInSelection(claim.getMinimumPoint(), claim.getMaximumPoint());

        if(!claims.isEmpty()) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CLAIM_CLAIM_OVERLAPPING);
            return false;
        }

        int minX = claim.getMinX() - Config.FACTION_CLAIM_BUFFER;
        int maxX = claim.getMaxX() + Config.FACTION_CLAIM_BUFFER;
        int minZ = claim.getMinZ() - Config.FACTION_CLAIM_BUFFER;
        int maxZ = claim.getMaxZ() + Config.FACTION_CLAIM_BUFFER;

        claims = this.getClaimsInSelection(claim.getWorld(), minX, maxX, minZ, maxZ);
        if(claims.isEmpty()) return true;

        boolean canClaimWithBuffer = claims.stream().allMatch(c -> c.getOwnerId().equals(faction.getId())
            || (Config.FACTION_CLAIM_BUFFER_IGNORE_ROADS && c.getOwner() instanceof RoadFaction));

        if(!canClaimWithBuffer) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CLAIM_CLAIM_BUFFER
                .replace("<buffer>", String.valueOf(Config.FACTION_CLAIM_BUFFER)));
        }

        return canClaimWithBuffer;
    }

    private void sendSelectionPillars(Player player, Location clicked) {
        for(int i = 0; i < player.getLocation().getBlockY() + 60; i++) {
            Location location = clicked.clone();
            location.setY(i);

            Lazarus.getInstance().getGlassManager().generateGlassVisual(player,
                new GlassInfo(GlassType.CLAIM_SELECTION, location, i % 4 == 0
                    ? Material.DIAMOND_BLOCK
                    : Material.STAINED_GLASS, (byte) 5));
        }
    }

    public int getClaimPrice(PlayerFaction faction, int claimSize) {
        return (int) (claimSize * Config.FACTION_CLAIM_PRICE_PER_BLOCK);
    }

    private boolean checkMinMaxSizeRestriction(Player player, int[] minMaxSize) {
        if(minMaxSize[0] != -1 && minMaxSize[0] < Config.FACTION_MIN_CLAIM_SIZE) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CLAIM_CLAIM_MIN_SIZE
            .replace("<size>", Config.FACTION_MIN_CLAIM_SIZE + "x" + Config.FACTION_MIN_CLAIM_SIZE));
            return false;
        }

        if(minMaxSize[1] != -1 && minMaxSize[1] > Config.FACTION_MAX_CLAIM_SIZE) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CLAIM_CLAIM_MAX_SIZE
            .replace("<size>", Config.FACTION_MAX_CLAIM_SIZE + "x" + Config.FACTION_MAX_CLAIM_SIZE));
            return false;
        }

        return true;
    }

    private void checkSelectionUpdate(Player player, PlayerFaction faction, Selection selection, Location location, boolean leftClick) {
        if(location.getWorld().getEnvironment() != Environment.NORMAL) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CLAIM_CAN_CLAIM_ONLY_IN_OVERWORLD);
            return;
        }

        Faction factionAt = this.getFactionAt(location);

        if(!(factionAt instanceof WildernessFaction)) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CLAIM_LOCATION_ALREADY_CLAIMED);
            return;
        }

        Location oldClicked;

        if(leftClick) {
            if(!this.checkMinMaxSizeRestriction(player, selection.getSize(true, location))) return;

            oldClicked = selection.getPosOne();
            selection.setPosOne(location);
        } else {
            if(!this.checkMinMaxSizeRestriction(player, selection.getSize(false, location))) return;

            oldClicked = selection.getPosTwo();
            selection.setPosTwo(location);
        }

        int price = selection.areBothPositionsSet() ? this.getClaimPrice(faction, selection.sizeX() * selection.sizeZ()) : 0;

        player.sendMessage(Language.FACTION_PREFIX +
            (leftClick ? Language.FACTIONS_CLAIM_POSITION_ONE_SET : Language.FACTIONS_CLAIM_POSITION_TWO_SET)
            .replace("<location>", StringUtils.getLocationNameWithoutY(location)));

        if(selection.areBothPositionsSet()) {
            String priceString = faction.getBalance() < price ? ChatColor.RED + "$" + price : ChatColor.GREEN + "$" + price;

            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CLAIM_PRICE_MESSAGE
                .replace("<price>", priceString)
                .replace("<sizeX>", String.valueOf(selection.sizeX()))
                .replace("<sizeZ>", String.valueOf(selection.sizeZ()))
                .replace("<size>", String.valueOf(selection.sizeX() * selection.sizeZ())));
        }

        Tasks.async(() -> {
            Lazarus.getInstance().getGlassManager().clearGlassVisuals(player, GlassType.CLAIM_SELECTION, glassInfo -> {
                Location loc = glassInfo.getLocation();

                return oldClicked != null
                    && loc.getBlockX() == oldClicked.getBlockX()
                    && loc.getBlockZ() == oldClicked.getBlockZ();
            });

            this.sendSelectionPillars(player, location);
        });
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.useInteractedBlock() == Result.DENY && event.useItemInHand() == Result.DENY) return;
        if(!event.hasItem()) return;

        Player player = event.getPlayer();

        Selection selection = Lazarus.getInstance().getSelectionManager().getSelection(player);
        if(selection == null || selection.getType() != SelectionType.CLAIM) return;

        if(!event.getItem().equals(selection.getWand())) return;

        event.setCancelled(true);

        Action action = event.getAction();
        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(player);

        if((action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) && player.isSneaking()) {
            if(!selection.areBothPositionsSet()) {
                player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CLAIM_SELECTION_NOT_SET);
                return;
            }

            Claim claim = selection.toClaim(faction);

            if(!this.isSelectionConnected(faction, claim)) {
                player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CLAIM_CLAIM_NOT_CONNECTED);
                return;
            }

            if(!this.canClaimSelection(faction, claim, player, false)) return;
            int price = this.getClaimPrice(faction, selection.sizeX() * selection.sizeZ());

            if(faction.getBalance() < price) {
                player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CLAIM_NOT_ENOUGH_MONEY);
                return;
            }

            if(!this.addClaim(claim)) return;
            faction.removeBalance(price);

            Tasks.async(() -> Lazarus.getInstance().getGlassManager().clearGlassVisuals(player, GlassType.CLAIM_SELECTION));
            Lazarus.getInstance().getSelectionManager().removeSelectionProcess(player);

            faction.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CLAIM_CLAIM_CLAIMED
                .replace("<location1>", StringUtils.getLocationNameWithoutY(claim.getMinimumPoint()))
                .replace("<location2>", StringUtils.getLocationNameWithoutY(claim.getMaximumPoint()))
                .replace("<player>", player.getName()));

            return;
        }

        if(action == Action.LEFT_CLICK_BLOCK) {
            this.checkSelectionUpdate(player, faction, selection, event.getClickedBlock().getLocation(), true);
            return;
        }

        if(action == Action.RIGHT_CLICK_BLOCK) {
            this.checkSelectionUpdate(player, faction, selection, event.getClickedBlock().getLocation(), false);
            return;
        }

        if(action == Action.RIGHT_CLICK_AIR) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CLAIM_SELECTION_CLEARED);
            selection.clearSelection();

            Tasks.async(() -> Lazarus.getInstance().getGlassManager().clearGlassVisuals(player, GlassType.CLAIM_SELECTION));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onFactionClaimChange(FactionClaimChangeEvent event) {
        if(!(event.getFaction() instanceof PlayerFaction)) return;
        if(event.getClaimChangeReason() == ClaimChangeReason.CLAIM) return;

        PlayerFaction faction = (PlayerFaction) event.getFaction();
        AtomicInteger toReturn = new AtomicInteger(0);

        event.getClaims().forEach(claim -> toReturn.addAndGet(this.getClaimPrice(faction, claim.sizeX() * claim.sizeZ())));
        int returnBalance = (int) (toReturn.get() * Config.FACTION_UNCLAIM_PRICE_MULTIPLIER);

        Tasks.async(() -> {
            String message;

            if(event.getClaimChangeReason() == ClaimChangeReason.DISBAND) {
                Lazarus.getInstance().getEconomyManager().addBalance(Bukkit.getOfflinePlayer(faction.getLeader().getUuid()), returnBalance);
                message = Language.FACTIONS_DISBAND_CLAIM_MONEY_REFUNDED;
            } else {
                faction.addBalance(returnBalance);
                message = Language.FACTIONS_UNCLAIM_CLAIM_MONEY_REFUNDED;
            }

            Player player = faction.getLeader().getPlayer();
            if(player == null) return;

            player.sendMessage(Language.FACTION_PREFIX + message.replace("<amount>", String.valueOf(returnBalance)));
        });
    }
}
