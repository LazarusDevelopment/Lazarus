package me.qiooip.lazarus.commands.manager;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.abilities.commands.AbilityCommand;
import me.qiooip.lazarus.abilities.commands.AbilityTimerCommand;
import me.qiooip.lazarus.commands.*;
import me.qiooip.lazarus.commands.base.*;
import me.qiooip.lazarus.commands.staff.*;
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
        this.commands.add(new AbilityTimerCommand());

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
        this.commands.add(new LastKillsCommand());
        this.commands.add(new LivesCommand());
        this.commands.add(new NotesCommand());
        this.commands.add(new PlaytimeCommand());
        this.commands.add(new PurgeCommand());
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
        this.commands.add(new LeaderboardCommand());
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
        this.commands.add(new SpawnCreditsCommand());
        this.commands.add(new StatsCommand());
        this.commands.add(new SubclaimCommand());
        this.commands.add(new TellLocationCommand());
        this.commands.add(new ToggleChatCommand());
        this.commands.add(new ToggleCobbleCommand());
        this.commands.add(new ToggleDeathMessagesCommand());
        this.commands.add(new ToggleFoundOreCommand());
        this.commands.add(new ToggleLightningCommand());
        this.commands.add(new ToggleScoreboardCommand());
        this.commands.add(new TopBalanceCommand());
        this.commands.add(new TopDeathsCommand());
        this.commands.add(new TopKillsCommand());
        this.commands.add(new TopKillstreaksCommand());
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
