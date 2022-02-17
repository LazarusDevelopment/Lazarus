package me.qiooip.lazarus.handlers.manager;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.handlers.BorderHandler;
import me.qiooip.lazarus.handlers.BottleHandler;
import me.qiooip.lazarus.handlers.CustomRecipeHandler;
import me.qiooip.lazarus.handlers.DisenchantHandler;
import me.qiooip.lazarus.handlers.DynamicEventHandler;
import me.qiooip.lazarus.handlers.ExpAmplifierHandler;
import me.qiooip.lazarus.handlers.GlisteringMelonHandler;
import me.qiooip.lazarus.handlers.InventoryHandler;
import me.qiooip.lazarus.handlers.MapkitHandler;
import me.qiooip.lazarus.handlers.MobStackHandler;
import me.qiooip.lazarus.handlers.SpawnTeleportHandler;
import me.qiooip.lazarus.handlers.StatsHandler;
import me.qiooip.lazarus.handlers.block.AutoSmeltHandler;
import me.qiooip.lazarus.handlers.block.CobwebRemoveHandler;
import me.qiooip.lazarus.handlers.block.CrowbarHandler;
import me.qiooip.lazarus.handlers.block.DisabledBlocksHandler;
import me.qiooip.lazarus.handlers.block.FilterHandler;
import me.qiooip.lazarus.handlers.block.FoundOreHandler;
import me.qiooip.lazarus.handlers.block.SubclaimHandler;
import me.qiooip.lazarus.handlers.chat.BlockedCommandsHandler;
import me.qiooip.lazarus.handlers.chat.ChatControlHandler;
import me.qiooip.lazarus.handlers.chat.ChatHandler;
import me.qiooip.lazarus.handlers.chat.MessagingHandler;
import me.qiooip.lazarus.handlers.death.DeathMessageHandler;
import me.qiooip.lazarus.handlers.death.DeathSignHandler;
import me.qiooip.lazarus.handlers.death.HeadDropHandler;
import me.qiooip.lazarus.handlers.death.StattrakHandler;
import me.qiooip.lazarus.handlers.elevator.MinecartElevatorHandler;
import me.qiooip.lazarus.handlers.elevator.SignElevatorHandler;
import me.qiooip.lazarus.handlers.kitmap.KillstreakHandler;
import me.qiooip.lazarus.handlers.kitmap.KitmapHandler;
import me.qiooip.lazarus.handlers.leaderboard.LeaderboardHandler;
import me.qiooip.lazarus.handlers.limiter.EnchantLimiterHandler;
import me.qiooip.lazarus.handlers.limiter.EntityLimiterHandler;
import me.qiooip.lazarus.handlers.limiter.PotionLimiterHandler;
import me.qiooip.lazarus.handlers.logger.CombatLoggerHandler;
import me.qiooip.lazarus.handlers.portal.EndPortalHandler;
import me.qiooip.lazarus.handlers.portal.NetherPortalTrapHandler;
import me.qiooip.lazarus.handlers.portal.PortalHandler;
import me.qiooip.lazarus.handlers.rank.RankAnnouncerHandler;
import me.qiooip.lazarus.handlers.rank.RankReviveHandler;
import me.qiooip.lazarus.handlers.rank.ReclaimHandler;
import me.qiooip.lazarus.handlers.salvage.SalvageHandler;
import me.qiooip.lazarus.handlers.settings.SettingsHandler;
import me.qiooip.lazarus.handlers.staff.FreezeHandler;
import me.qiooip.lazarus.handlers.staff.NotesHandler;
import me.qiooip.lazarus.handlers.staff.RebootHandler;
import me.qiooip.lazarus.handlers.staff.StaffChatHandler;
import me.qiooip.lazarus.handlers.staff.WarpsHandler;
import me.qiooip.lazarus.handlers.timer.CombatTagHandler;
import me.qiooip.lazarus.handlers.timer.EnderPearlHandler;
import me.qiooip.lazarus.handlers.timer.EotwHandler;
import me.qiooip.lazarus.handlers.timer.GoldenAppleHandler;
import me.qiooip.lazarus.handlers.timer.LogoutHandler;
import me.qiooip.lazarus.handlers.timer.PurgeHandler;
import me.qiooip.lazarus.handlers.timer.PvpProtHandler;
import me.qiooip.lazarus.handlers.timer.SotwHandler;
import me.qiooip.lazarus.utils.ManagerEnabler;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class HandlerManager implements ManagerEnabler {

    private final List<Handler> handlers;

    public HandlerManager() {
        this.handlers = new ArrayList<>();

        this.handlers.add(new AutoSmeltHandler());
        this.handlers.add(new BlockedCommandsHandler());
        this.handlers.add(new BorderHandler());
        this.handlers.add(new BottleHandler());
        this.handlers.add(new ChatControlHandler());
        this.handlers.add(new ChatHandler());
        this.handlers.add(new CombatLoggerHandler());
        this.handlers.add(new CombatTagHandler());
        this.handlers.add(new CrowbarHandler());
        this.handlers.add(new CustomRecipeHandler());
        this.handlers.add(new DeathMessageHandler());
        this.handlers.add(new DeathSignHandler());
        this.handlers.add(new DisabledBlocksHandler());
        this.handlers.add(new DisenchantHandler());
        this.handlers.add(new DynamicEventHandler());
        this.handlers.add(new EnchantLimiterHandler());
        this.handlers.add(new EnderPearlHandler());
        this.handlers.add(new EndPortalHandler());
        this.handlers.add(new EntityLimiterHandler());
        this.handlers.add(new EotwHandler());
        this.handlers.add(new ExpAmplifierHandler());
        this.handlers.add(new FilterHandler());
        this.handlers.add(new FoundOreHandler());
        this.handlers.add(new FreezeHandler());
        this.handlers.add(new GlisteringMelonHandler());
        this.handlers.add(new GoldenAppleHandler());
        this.handlers.add(new HeadDropHandler());
        this.handlers.add(new InventoryHandler());
        this.handlers.add(new KillstreakHandler());
        this.handlers.add(new LeaderboardHandler());
        this.handlers.add(new LogoutHandler());
        this.handlers.add(new MapkitHandler());
        this.handlers.add(new MessagingHandler());
        this.handlers.add(new MinecartElevatorHandler());
        this.handlers.add(new NetherPortalTrapHandler());
        this.handlers.add(new NotesHandler());
        this.handlers.add(new PortalHandler());
        this.handlers.add(new PotionLimiterHandler());
        this.handlers.add(new PurgeHandler());
        this.handlers.add(new PvpProtHandler());
        this.handlers.add(new RankReviveHandler());
        this.handlers.add(new RebootHandler());
        this.handlers.add(new ReclaimHandler());
        this.handlers.add(new SalvageHandler());
        this.handlers.add(new SettingsHandler());
        this.handlers.add(new SignElevatorHandler());
        this.handlers.add(new SotwHandler());
        this.handlers.add(new SpawnTeleportHandler());
        this.handlers.add(new StaffChatHandler());
        this.handlers.add(new StatsHandler());
        this.handlers.add(new StattrakHandler());
        this.handlers.add(new SubclaimHandler());
        this.handlers.add(new WarpsHandler());

        if(Config.KITMAP_MODE_ENABLED) this.handlers.add(new KitmapHandler());
        if(Config.MOB_STACK_ENABLED) this.handlers.add(new MobStackHandler());
        if(Config.COBWEB_REMOVER_ENABLED) this.handlers.add(new CobwebRemoveHandler());
        if(Config.ONLINE_RANK_ANNOUNCER_ENABLED) this.handlers.add(new RankAnnouncerHandler());

        this.handlers.stream().filter(handler -> handler instanceof Listener).forEach(handler ->
            Bukkit.getPluginManager().registerEvents((Listener) handler, Lazarus.getInstance()));

        Lazarus.getInstance().log("- &7Enabled &a" + this.handlers.size() + " &7handlers.");
    }

    public void disable() {
        try {
            this.handlers.forEach(Handler::disable);
        } catch(Exception e) {
            e.printStackTrace();
        }

        this.handlers.clear();
    }

    public Handler getHandler(Class<?> clazz) {
        return this.handlers.stream().filter(handler -> handler.getClass() == clazz).findFirst().orElse(null);
    }
}
