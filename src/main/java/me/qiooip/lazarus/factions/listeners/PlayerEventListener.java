package me.qiooip.lazarus.factions.listeners;

import me.qiooip.lazarus.abilities.event.AbilityActivatedEvent;
import me.qiooip.lazarus.abilities.event.ProjectileAbilityActivatedEvent;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionPlayer;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.claim.Claim;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import me.qiooip.lazarus.factions.enums.ChatType;
import me.qiooip.lazarus.factions.event.FactionChatEvent;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.factions.type.RoadFaction;
import me.qiooip.lazarus.factions.type.SystemFaction;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.scoreboard.HomeTimer;
import me.qiooip.lazarus.timer.scoreboard.StuckTimer;
import me.qiooip.lazarus.utils.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class PlayerEventListener implements Listener {

    private void checkMovement(Player player, Location from, Location to) {
        if(from.getBlockX() == to.getBlockX()
            && from.getBlockY() == to.getBlockY()
            && from.getBlockZ() == to.getBlockZ()) return;

        this.checkFactionHomeAndStuckOnMove(player);

        Faction fromFaction = ClaimManager.getInstance().getFactionAt(from);
        Faction toFaction = ClaimManager.getInstance().getFactionAt(to);

        if(fromFaction == toFaction) return;

        TimerManager.getInstance().getPvpProtTimer().pause(player, toFaction.isSafezone(), to);

        player.sendMessage(Language.FACTIONS_CLAIM_LEAVING
            .replace("<faction>", fromFaction.getDisplayName(player))
            .replace("<deathban>", fromFaction.getDeathbanString()));

        player.sendMessage(Language.FACTIONS_CLAIM_ENTERING
            .replace("<faction>", toFaction.getDisplayName(player))
            .replace("<deathban>", toFaction.getDeathbanString()));
    }

    private void checkFactionHomeAndStuckOnMove(Player player) {
        HomeTimer homeTimer = TimerManager.getInstance().getHomeTimer();

        if(homeTimer.isActive(player)) {
            homeTimer.cancel(player);
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_HOME_CANCELLED_MOVED);
        }

        StuckTimer stuckTimer = TimerManager.getInstance().getStuckTimer();

        if(stuckTimer.isActive(player) && FactionsManager.getInstance().hasExceededStuckLimit(player)) {
            stuckTimer.cancel(player);
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_STUCK_CANCELLED_MOVED);
        }
    }

    private void cancelHomeAndStuckTimersOnDamage(Player player) {
        HomeTimer homeTimer = TimerManager.getInstance().getHomeTimer();

        if(homeTimer.isActive(player)) {
            homeTimer.cancel(player);
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_HOME_CANCELLED_DAMAGED);
        }

        StuckTimer stuckTimer = TimerManager.getInstance().getStuckTimer();

        if(stuckTimer.isActive(player)) {
            stuckTimer.cancel(player);
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_STUCK_CANCELLED_DAMAGED);
        }
    }

    private void refundEnderpearl(Player player, String refundMessage) {
        TimerManager.getInstance().getEnderPearlTimer().cancel(player);
        player.setMetadata("enderpearlRefund", PlayerUtils.TRUE_METADATA_VALUE);
        player.sendMessage(refundMessage);
    }

    private boolean shouldDenyAbilityUsage(Player player, Location loc) {
        Environment environment = loc.getWorld().getEnvironment();

        if(Config.ABILITIES_DENY_USAGE_WORLD.get(environment)) {
            player.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_DENY_USAGE_WORLD);
            return true;
        }

        if(Config.ABILITIES_DENY_USAGE_DISTANCE_ENABLED.get(environment)) {
            int denyUsageDistance = Config.ABILITIES_DENY_USAGE_DISTANCE_BLOCKS.get(environment);

            if(Math.max(Math.abs(loc.getBlockX()), Math.abs(loc.getBlockZ())) <= denyUsageDistance) {
                player.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_DENY_USAGE_DISTANCE
                    .replace("<amount>", String.valueOf(denyUsageDistance)));

                return true;
            }
        }

        return false;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event) {
        if(TimerManager.getInstance().getCombatTagTimer().isActive(event.getPlayer())
                && ClaimManager.getInstance().getFactionAt(event.getTo()).isSafezone()) {
            event.setTo(event.getFrom());
            return;
        }

        this.checkMovement(event.getPlayer(), event.getFrom(), event.getTo());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        if(event.getCause() == TeleportCause.ENDER_PEARL) {
            Faction factionAt = ClaimManager.getInstance().getFactionAt(event.getTo());

            if(!factionAt.areEnderpearlEnabled()) {
                this.refundEnderpearl(player, Language.FACTION_PREFIX + Language.FACTIONS_ENDERPEARL_USAGE_DENIED
                    .replace("<faction>", factionAt.getDisplayName(player)));

                event.setCancelled(true);
                return;
            }

            if(factionAt.isSafezone() && !ClaimManager.getInstance().getFactionAt(event.getFrom()).isSafezone()) {
                this.refundEnderpearl(player, Language.PREFIX + Language.ENDERPEARL_DENY_REFUNDED);
                event.setCancelled(true);
                return;
            }
        }

        this.checkMovement(player, event.getFrom(), event.getTo());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerPortal(PlayerPortalEvent event) {
        if(event.getCause() != TeleportCause.NETHER_PORTAL) return;
        if(!event.useTravelAgent() || !event.getPortalTravelAgent().getCanCreatePortal()) return;

        Claim claimAt = ClaimManager.getInstance().getClaimAt(event.getTo());
        if(claimAt == null) return;

        Location newLocation;

        if(claimAt.getOwner() instanceof RoadFaction) {
            newLocation = event.getTo().clone();

            if(Math.abs(newLocation.getBlockX()) > Math.abs(newLocation.getBlockZ())) {
                newLocation.add(newLocation.getBlockX() < 0 ? -20 : 20, 0, 0);
            } else {
                newLocation.add(0, 0, newLocation.getBlockZ() < 0 ? -20 : 20);
            }

            if((claimAt = ClaimManager.getInstance().getClaimAt(newLocation)) != null) {
                newLocation = ClaimManager.getInstance().getSafeLocation(claimAt);
            }
        } else {
            newLocation = ClaimManager.getInstance().getSafeLocation(claimAt);
        }

        event.setTo(newLocation);
    }

    @EventHandler
    public void onAbilityActivated(AbilityActivatedEvent event) {
        if(event.isProjectileAbility()) return;

        Player player = event.getPlayer();

        if(this.shouldDenyAbilityUsage(player, event.getLocation())) {
            event.setCancelled(true);
            return;
        }

        Faction factionAt = ClaimManager.getInstance().getFactionAt(player);

        if(factionAt instanceof SystemFaction && !((SystemFaction) factionAt).isAbilities()) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_ABILITIES_USAGE_DENIED
                .replace("<faction>", factionAt.getDisplayName(player)));

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onProjectileAbilityActivated(ProjectileAbilityActivatedEvent event) {
        Player player = event.getPlayer();

        if(this.shouldDenyAbilityUsage(player, event.getLocation())) {
            event.setCancelled(true);
            return;
        }

        Faction factionAt = ClaimManager.getInstance().getFactionAt(event.getLocation());

        if(factionAt instanceof SystemFaction && !((SystemFaction) factionAt).isAbilities()) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_ABILITIES_USAGE_DENIED
                .replace("<faction>", factionAt.getDisplayName(player)));

            TimerManager.getInstance().getEnderPearlTimer().cancel(player);
            event.getAbilityItem().handleAbilityRefund(player, null);

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(event.getEntity());

        if(faction != null) {
            faction.onDeath(victim);

            TimerManager.getInstance().getHomeTimer().cancel(victim);
            TimerManager.getInstance().getStuckTimer().cancel(victim);
        }

        Player killer = victim.getKiller();

        if(killer != null && victim != killer) {
            PlayerFaction killerFaction = FactionsManager.getInstance().getPlayerFaction(killer);

            if(killerFaction != null) {
                killerFaction.addKill();
                killerFaction.incrementPoints(Config.FACTION_TOP_KILL);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(event.getPlayer());
        if(faction == null) return;

        FactionPlayer fplayer = faction.getMember(event.getPlayer());
        if(fplayer.getChatType() == ChatType.PUBLIC) return;

        event.setCancelled(true);

        String message = fplayer.getChatType().getFormat().replace("<player>",
        event.getPlayer().getName()).replace("<message>", event.getMessage());

        FactionChatEvent chatEvent = new FactionChatEvent(event.getPlayer(), faction, message);
        if(chatEvent.isCancelled()) return;

        faction.getOnlinePlayers().forEach(recipient -> recipient.sendMessage(message));

        if(fplayer.getChatType() == ChatType.ALLY) faction.getAlliesAsFactions().forEach(ally ->
        ally.getOnlinePlayers().forEach(recipient -> recipient.sendMessage(message)));
    }

    @EventHandler(ignoreCancelled = true)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        if(player.getFoodLevel() < event.getFoodLevel() || !ClaimManager.getInstance().getFactionAt(player.getLocation()).isSafezone()) return;

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPotionSplash(PotionSplashEvent event) {
        Faction factionAt = ClaimManager.getInstance().getFactionAt(event.getEntity().getLocation());

        if(factionAt.isSafezone()) {
            event.setCancelled(true);
            return;
        }

        ThrownPotion potion = event.getEntity();
        if(!(potion.getShooter() instanceof Player)) return;

        Player player = (Player) potion.getShooter();

        event.getAffectedEntities().stream().filter(entity -> entity instanceof Player && ClaimManager.getInstance()
        .getFactionAt(entity.getLocation()).isSafezone()).forEach(affected -> event.setIntensity(affected, 0));
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        Faction factionAt = ClaimManager.getInstance().getFactionAt(player);

        if(factionAt.isSafezone()) {
            event.setCancelled(true);
            return;
        }

        this.cancelHomeAndStuckTimersOnDamage(player);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        Player damager = PlayerUtils.getAttacker(event);
        if(damager == null) return;

        Player player = (Player) event.getEntity();

        if(player == damager) {
            if(!Config.FACTION_PLAYERS_TAKE_OWN_DAMAGE && event.getCause() != DamageCause.FALL) {
                event.setCancelled(true);
            }

            return;
        }

        Faction damagerFactionAt = ClaimManager.getInstance().getFactionAt(damager);

        if(damagerFactionAt.isSafezone()) {
            event.setCancelled(true);
            damager.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_SAFEZONE_DENY_DAMAGE_ATTACKER);
            return;
        }

        Faction playerFactionAt = ClaimManager.getInstance().getFactionAt(player);

        if(playerFactionAt.isSafezone()) {
            event.setCancelled(true);
            damager.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_SAFEZONE_DENY_DAMAGE_OTHERS);
            return;
        }

        if(event.getDamager() instanceof EnderPearl && !playerFactionAt.areEnderpearlEnabled()) {
            event.setCancelled(true);
            return;
        }

        PlayerFaction playerFaction = FactionsManager.getInstance().getPlayerFaction(player);
        PlayerFaction damagerFaction = FactionsManager.getInstance().getPlayerFaction(damager);

        if(playerFaction == null || damagerFaction == null) return;

        if(playerFaction == damagerFaction && !playerFaction.isFriendlyFire()) {
            event.setCancelled(true);
            return;
        }

        if(Config.FACTION_MAX_ALLIES > 0 && !Config.FACTION_ALLY_FRIENDLY_FIRE && playerFaction.isAlly(damagerFaction)) {
            event.setCancelled(true);

            damager.sendMessage(Language.FACTIONS_DENY_DAMAGE_ALLIES
                .replace("<player>", Config.ALLY_COLOR + player.getName()));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        TimerManager.getInstance().getHomeTimer().cancel(event.getPlayer());
        TimerManager.getInstance().getStuckTimer().cancel(event.getPlayer());
    }
}
