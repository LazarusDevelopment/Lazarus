package me.qiooip.lazarus.commands.manager;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.abilities.commands.AbilityCommand;
import me.qiooip.lazarus.commands.BottleCommand;
import me.qiooip.lazarus.commands.CoordsCommand;
import me.qiooip.lazarus.commands.FillBottleCommand;
import me.qiooip.lazarus.commands.FilterCommand;
import me.qiooip.lazarus.commands.FocusCommand;
import me.qiooip.lazarus.commands.GoldenAppleCommand;
import me.qiooip.lazarus.commands.HelpCommand;
import me.qiooip.lazarus.commands.LazarusCommand;
import me.qiooip.lazarus.commands.LffCommand;
import me.qiooip.lazarus.commands.ListCommand;
import me.qiooip.lazarus.commands.LogoutCommand;
import me.qiooip.lazarus.commands.MapkitCommand;
import me.qiooip.lazarus.commands.MinerEffectsCommand;
import me.qiooip.lazarus.commands.PvpCommand;
import me.qiooip.lazarus.commands.RankReviveCommand;
import me.qiooip.lazarus.commands.ReclaimCommand;
import me.qiooip.lazarus.commands.ReportCommand;
import me.qiooip.lazarus.commands.RequestCommand;
import me.qiooip.lazarus.commands.SalvageCommand;
import me.qiooip.lazarus.commands.SettingsCommand;
import me.qiooip.lazarus.commands.SotwCommand;
import me.qiooip.lazarus.commands.SpawnCommand;
import me.qiooip.lazarus.commands.StatsCommand;
import me.qiooip.lazarus.commands.SubclaimCommand;
import me.qiooip.lazarus.commands.TellLocationCommand;
import me.qiooip.lazarus.commands.ToggleChatCommand;
import me.qiooip.lazarus.commands.ToggleCobbleCommand;
import me.qiooip.lazarus.commands.ToggleDeathMessagesCommand;
import me.qiooip.lazarus.commands.ToggleFoundOreCommand;
import me.qiooip.lazarus.commands.ToggleLightningCommand;
import me.qiooip.lazarus.commands.ToggleScoreboardCommand;
import me.qiooip.lazarus.commands.UnfocusCommand;
import me.qiooip.lazarus.commands.base.AdventureCommand;
import me.qiooip.lazarus.commands.base.BackCommand;
import me.qiooip.lazarus.commands.base.BroadcastCommand;
import me.qiooip.lazarus.commands.base.BroadcastRawCommand;
import me.qiooip.lazarus.commands.base.ClearInventoryCommand;
import me.qiooip.lazarus.commands.base.CraftCommand;
import me.qiooip.lazarus.commands.base.CreativeCommand;
import me.qiooip.lazarus.commands.base.DayCommand;
import me.qiooip.lazarus.commands.base.DeleteWarpCommand;
import me.qiooip.lazarus.commands.base.EnchantCommand;
import me.qiooip.lazarus.commands.base.EnderchestCommand;
import me.qiooip.lazarus.commands.base.ExperienceCommand;
import me.qiooip.lazarus.commands.base.FeedCommand;
import me.qiooip.lazarus.commands.base.FlyCommand;
import me.qiooip.lazarus.commands.base.GamemodeCommand;
import me.qiooip.lazarus.commands.base.GiveCommand;
import me.qiooip.lazarus.commands.base.GodCommand;
import me.qiooip.lazarus.commands.base.HealCommand;
import me.qiooip.lazarus.commands.base.IgnoreCommand;
import me.qiooip.lazarus.commands.base.InvseeCommand;
import me.qiooip.lazarus.commands.base.ItemCommand;
import me.qiooip.lazarus.commands.base.KickallCommand;
import me.qiooip.lazarus.commands.base.KillCommand;
import me.qiooip.lazarus.commands.base.KillallCommand;
import me.qiooip.lazarus.commands.base.MessageCommand;
import me.qiooip.lazarus.commands.base.MoreCommand;
import me.qiooip.lazarus.commands.base.NightCommand;
import me.qiooip.lazarus.commands.base.PingCommand;
import me.qiooip.lazarus.commands.base.RenameCommand;
import me.qiooip.lazarus.commands.base.RepairCommand;
import me.qiooip.lazarus.commands.base.ReplyCommand;
import me.qiooip.lazarus.commands.base.SeenCommand;
import me.qiooip.lazarus.commands.base.SetWarpCommand;
import me.qiooip.lazarus.commands.base.SocialSpyCommand;
import me.qiooip.lazarus.commands.base.SpawnerCommand;
import me.qiooip.lazarus.commands.base.SpeedCommand;
import me.qiooip.lazarus.commands.base.SurvivalCommand;
import me.qiooip.lazarus.commands.base.TeleportAllCommand;
import me.qiooip.lazarus.commands.base.TeleportCommand;
import me.qiooip.lazarus.commands.base.TeleportHereCommand;
import me.qiooip.lazarus.commands.base.TeleportPositionCommand;
import me.qiooip.lazarus.commands.base.ToggleMsgCommand;
import me.qiooip.lazarus.commands.base.ToggleSoundsCommand;
import me.qiooip.lazarus.commands.base.TopCommand;
import me.qiooip.lazarus.commands.base.WarpCommand;
import me.qiooip.lazarus.commands.base.WorldCommand;
import me.qiooip.lazarus.commands.staff.ChatControlCommand;
import me.qiooip.lazarus.commands.staff.CopyInventoryCommand;
import me.qiooip.lazarus.commands.staff.CrowbarCommand;
import me.qiooip.lazarus.commands.staff.CustomTimerCommand;
import me.qiooip.lazarus.commands.staff.DeathbanCommand;
import me.qiooip.lazarus.commands.staff.EndPortalCommand;
import me.qiooip.lazarus.commands.staff.EotwCommand;
import me.qiooip.lazarus.commands.staff.ExitCommand;
import me.qiooip.lazarus.commands.staff.FreezeAllCommand;
import me.qiooip.lazarus.commands.staff.FreezeCommand;
import me.qiooip.lazarus.commands.staff.HardResetCommand;
import me.qiooip.lazarus.commands.staff.HideStaffCommand;
import me.qiooip.lazarus.commands.staff.InventoryInspectCommand;
import me.qiooip.lazarus.commands.staff.InventoryRestoreCommand;
import me.qiooip.lazarus.commands.staff.LagCommand;
import me.qiooip.lazarus.commands.staff.LastDeathsCommand;
import me.qiooip.lazarus.commands.staff.LivesCommand;
import me.qiooip.lazarus.commands.staff.NotesCommand;
import me.qiooip.lazarus.commands.staff.PlaytimeCommand;
import me.qiooip.lazarus.commands.staff.RandomTeleportCommand;
import me.qiooip.lazarus.commands.staff.RebootCommand;
import me.qiooip.lazarus.commands.staff.SaleCommand;
import me.qiooip.lazarus.commands.staff.SetExitCommand;
import me.qiooip.lazarus.commands.staff.SetReclaimCommand;
import me.qiooip.lazarus.commands.staff.SetSlotsCommand;
import me.qiooip.lazarus.commands.staff.SetSpawnCommand;
import me.qiooip.lazarus.commands.staff.StaffChatCommand;
import me.qiooip.lazarus.commands.staff.StaffModeCommand;
import me.qiooip.lazarus.commands.staff.StaffScoreboardCommand;
import me.qiooip.lazarus.commands.staff.TimerCommand;
import me.qiooip.lazarus.commands.staff.VanishCommand;
import me.qiooip.lazarus.commands.staff.ViewDistanceCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.economy.commands.BalanceCommand;
import me.qiooip.lazarus.economy.commands.EconomyCommand;
import me.qiooip.lazarus.economy.commands.PayCommand;
import me.qiooip.lazarus.utils.ManagerEnabler;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import org.bukkit.command.CommandMap;
import org.bukkit.command.defaults.BukkitCommand;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements ManagerEnabler {

    private final CommandMap commandMap;
    private final List<BaseCommand> commands;

    public CommandManager() {
        this.commandMap = NmsUtils.getInstance().getCommandMap();
        this.commands = new ArrayList<>();

        this.commands.add(new AbilityCommand());
        this.commands.add(new ChatControlCommand());
        this.commands.add(new CopyInventoryCommand());
        this.commands.add(new CrowbarCommand());
        this.commands.add(new CustomTimerCommand());
        this.commands.add(new DeathbanCommand());
        this.commands.add(new EndPortalCommand());
        this.commands.add(new EotwCommand());
        this.commands.add(new ExitCommand());
        this.commands.add(new FreezeAllCommand());
        this.commands.add(new FreezeCommand());
        this.commands.add(new HardResetCommand());
        this.commands.add(new HideStaffCommand());
        this.commands.add(new InventoryInspectCommand());
        this.commands.add(new InventoryRestoreCommand());
        this.commands.add(new LagCommand());
        this.commands.add(new LastDeathsCommand());
        this.commands.add(new LivesCommand());
        this.commands.add(new NotesCommand());
        this.commands.add(new PlaytimeCommand());
        this.commands.add(new RandomTeleportCommand());
        this.commands.add(new RebootCommand());
        this.commands.add(new SaleCommand());
        this.commands.add(new SetExitCommand());
        this.commands.add(new SetReclaimCommand());
        this.commands.add(new SetSlotsCommand());
        this.commands.add(new SetSpawnCommand());
        this.commands.add(new StaffChatCommand());
        this.commands.add(new StaffModeCommand());
        this.commands.add(new StaffScoreboardCommand());
        this.commands.add(new TimerCommand());
        this.commands.add(new VanishCommand());
        this.commands.add(new ViewDistanceCommand());

        this.commands.add(new BalanceCommand());
        this.commands.add(new EconomyCommand());
        this.commands.add(new PayCommand());

        this.commands.add(new BottleCommand());
        this.commands.add(new CoordsCommand());
        this.commands.add(new FillBottleCommand());
        this.commands.add(new FilterCommand());
        this.commands.add(new FocusCommand());
        this.commands.add(new GoldenAppleCommand());
        this.commands.add(new HelpCommand());
        this.commands.add(new LazarusCommand());
        this.commands.add(new LffCommand());
        this.commands.add(new ListCommand());
        this.commands.add(new LogoutCommand());
        this.commands.add(new MapkitCommand());
        this.commands.add(new MinerEffectsCommand());
        this.commands.add(new PvpCommand());
        this.commands.add(new RankReviveCommand());
        this.commands.add(new ReclaimCommand());
        this.commands.add(new ReportCommand());
        this.commands.add(new RequestCommand());
        this.commands.add(new SalvageCommand());
        this.commands.add(new SettingsCommand());
        this.commands.add(new SotwCommand());
        this.commands.add(new SpawnCommand());
        this.commands.add(new StatsCommand());
        this.commands.add(new SubclaimCommand());
        this.commands.add(new TellLocationCommand());
        this.commands.add(new ToggleChatCommand());
        this.commands.add(new ToggleCobbleCommand());
        this.commands.add(new ToggleDeathMessagesCommand());
        this.commands.add(new ToggleFoundOreCommand());
        this.commands.add(new ToggleLightningCommand());
        this.commands.add(new ToggleScoreboardCommand());
        this.commands.add(new UnfocusCommand());

        this.commands.add(new AdventureCommand());
        this.commands.add(new BackCommand());
        this.commands.add(new BroadcastCommand());
        this.commands.add(new BroadcastRawCommand());
        this.commands.add(new ClearInventoryCommand());
        this.commands.add(new CraftCommand());
        this.commands.add(new CreativeCommand());
        this.commands.add(new DayCommand());
        this.commands.add(new DeleteWarpCommand());
        this.commands.add(new EnchantCommand());
        this.commands.add(new EnderchestCommand());
        this.commands.add(new ExperienceCommand());
        this.commands.add(new FeedCommand());
        this.commands.add(new FlyCommand());
        this.commands.add(new GamemodeCommand());
        this.commands.add(new GiveCommand());
        this.commands.add(new GodCommand());
        this.commands.add(new HealCommand());
        this.commands.add(new IgnoreCommand());
        this.commands.add(new InvseeCommand());
        this.commands.add(new ItemCommand());
        this.commands.add(new KickallCommand());
        this.commands.add(new KillallCommand());
        this.commands.add(new KillCommand());
        this.commands.add(new MessageCommand());
        this.commands.add(new MoreCommand());
        this.commands.add(new NightCommand());
        this.commands.add(new PingCommand());
        this.commands.add(new RenameCommand());
        this.commands.add(new RepairCommand());
        this.commands.add(new ReplyCommand());
        this.commands.add(new SeenCommand());
        this.commands.add(new SetWarpCommand());
        this.commands.add(new SocialSpyCommand());
        this.commands.add(new SpawnerCommand());
        this.commands.add(new SpeedCommand());
        this.commands.add(new SurvivalCommand());
        this.commands.add(new TeleportAllCommand());
        this.commands.add(new TeleportCommand());
        this.commands.add(new TeleportHereCommand());
        this.commands.add(new TeleportPositionCommand());
        this.commands.add(new ToggleMsgCommand());
        this.commands.add(new ToggleSoundsCommand());
        this.commands.add(new TopCommand());
        this.commands.add(new WarpCommand());
        this.commands.add(new WorldCommand());

        this.commands.forEach(this::registerCommand);

        Lazarus.getInstance().log("- &7Enabled &a" + this.commands.size() + " &7commands.");
    }

    public void disable() {
        this.commands.clear();
    }

    void registerCommand(BukkitCommand command) {
        if(!Config.DISABLED_LAZARUS_COMMANDS.contains(command.getName())) {
            this.commandMap.register("lazarus", command);
        }
    }
}
