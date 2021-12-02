package com.github.iamtakagi.tcore;

import com.github.iamtakagi.tcore.clan.command.*;
import com.github.iamtakagi.tcore.clan.packet.*;
import com.github.iamtakagi.tcore.convenient.command.*;
import com.github.iamtakagi.tcore.nametag.command.*;
import com.github.iamtakagi.tcore.nametag.packet.*;
import com.github.iamtakagi.tcore.punishment.command.*;
import com.github.iamtakagi.tcore.staff.packet.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qrakn.honcho.Honcho;
import com.github.iamtakagi.tcore.board.BoardManager;
import com.github.iamtakagi.tcore.chat.Chat;
import com.github.iamtakagi.tcore.chat.command.ClearChatCommand;
import com.github.iamtakagi.tcore.chat.command.MuteChatCommand;
import com.github.iamtakagi.tcore.chat.ChatListener;
import com.github.iamtakagi.tcore.chat.command.SlowChatCommand;
import com.github.iamtakagi.tcore.clan.ClanPlayerRole;
import com.github.iamtakagi.tcore.clan.ClanPlayerRoleTypeAdapter;
import com.github.iamtakagi.tcore.convenient.packet.PacketClickableBroadcast;
import com.github.iamtakagi.tcore.fix.EnderpearlFixListener;
import com.github.iamtakagi.tcore.io.file.ConfigValidation;
import com.github.iamtakagi.tcore.convenient.Convenient;
import com.github.iamtakagi.tcore.convenient.ConvenientListener;
import com.github.iamtakagi.tcore.io.file.type.BasicConfigurationFile;
import com.github.iamtakagi.tcore.network.NetworkPacketListener;
import com.github.iamtakagi.tcore.friend.packet.PacketFriendAccepted;
import com.github.iamtakagi.tcore.friend.packet.PacketFriendDelete;
import com.github.iamtakagi.tcore.friend.packet.PacketFriendJoinNetwork;
import com.github.iamtakagi.tcore.friend.packet.PacketFriendSendRequest;
import com.github.iamtakagi.tcore.grant.packet.PacketAddGrant;
import com.github.iamtakagi.tcore.grant.packet.PacketDeleteGrant;
import com.github.iamtakagi.tcore.profile.experience.command.ExpBoosterCommand;
import com.github.iamtakagi.tcore.profile.experience.packet.PacketExpBoosterApply;
import com.github.iamtakagi.tcore.profile.experience.packet.PacketExpBoosterRemove;
import com.github.iamtakagi.tcore.punishment.packet.PacketBroadcastPunishment;
import com.github.iamtakagi.tcore.punishment.packet.PacketClearPunishments;
import com.github.iamtakagi.tcore.rank.packet.PacketDeleteRank;
import com.github.iamtakagi.tcore.rank.packet.PacketRefreshRank;
import com.github.iamtakagi.tcore.pidgin.Pidgin;
import com.github.iamtakagi.tcore.profile.Profile;
import com.github.iamtakagi.tcore.profile.ProfileTypeAdapter;
import com.github.iamtakagi.tcore.profile.option.command.OptionsCommand;
import com.github.iamtakagi.tcore.profile.conversation.command.MessageCommand;
import com.github.iamtakagi.tcore.profile.conversation.command.ReplyCommand;
import com.github.iamtakagi.tcore.grant.command.GrantCommand;
import com.github.iamtakagi.tcore.grant.command.GrantsCommand;
import com.github.iamtakagi.tcore.grant.GrantListener;
import com.github.iamtakagi.tcore.profile.ProfileListener;
import com.github.iamtakagi.tcore.profile.option.command.ToggleGlobalChatCommand;
import com.github.iamtakagi.tcore.profile.option.command.TogglePrivateMessagesCommand;
import com.github.iamtakagi.tcore.profile.option.command.ToggleSoundsCommand;
import com.github.iamtakagi.tcore.profile.staff.command.AltsCommand;
import com.github.iamtakagi.tcore.punishment.listener.PunishmentListener;
import com.github.iamtakagi.tcore.profile.staff.command.StaffModeCommand;
import com.github.iamtakagi.tcore.rank.Rank;
import com.github.iamtakagi.tcore.rank.RankTypeAdapter;
import com.github.iamtakagi.tcore.profile.staff.command.StaffChatCommand;
import com.github.iamtakagi.tcore.rank.command.RankAddPermissionCommand;
import com.github.iamtakagi.tcore.rank.command.RankCreateCommand;
import com.github.iamtakagi.tcore.rank.command.RankDeleteCommand;
import com.github.iamtakagi.tcore.rank.command.RankHelpCommand;
import com.github.iamtakagi.tcore.rank.command.RankInfoCommand;
import com.github.iamtakagi.tcore.rank.command.RankInheritCommand;
import com.github.iamtakagi.tcore.rank.command.RankRemovePermissionCommand;
import com.github.iamtakagi.tcore.rank.command.RankSetColorCommand;
import com.github.iamtakagi.tcore.rank.command.RankSetPrefixCommand;
import com.github.iamtakagi.tcore.rank.command.RankSetSuffixCommand;
import com.github.iamtakagi.tcore.rank.command.RankSetWeightCommand;
import com.github.iamtakagi.tcore.rank.command.RankUninheritCommand;
import com.github.iamtakagi.tcore.rank.command.RanksCommand;
import com.github.iamtakagi.tcore.server.ServerStatusAPI;
import com.github.iamtakagi.tcore.task.MenuUpdateTask;
import com.github.iamtakagi.tcore.util.Style;
import com.github.iamtakagi.tcore.util.adapter.ChatColorTypeAdapter;
import com.github.iamtakagi.tcore.util.duration.Duration;
import com.github.iamtakagi.tcore.util.duration.DurationTypeAdapter;
import com.github.iamtakagi.tcore.menu.MenuListener;
import com.github.iamtakagi.tcore.cache.RedisCache;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import lombok.Getter;
import lombok.Setter;
import com.github.iamtakagi.tcore.world.WorldListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Getter
public class Core extends JavaPlugin {

	public static final Gson GSON = new Gson();
	public static final Type LIST_STRING_TYPE = new TypeToken<List<String>>() {}.getType();

	private static Core core;

	private BasicConfigurationFile mainConfig;

	private String serverName;

	private Pidgin pidgin;

	private Database db;
	private JedisPool jedisPool;
	private RedisCache redisCache;

	private Convenient convenient;
	private Chat chat;

	private BoardManager boardManager;

	@Setter private boolean debug;

	private Honcho honcho;

	@Override
	public void onEnable() {
		core = this;

		honcho = new Honcho(this);

		mainConfig = new BasicConfigurationFile(this, "config");

		serverName = mainConfig.getString("SERVER_NAME");

		new ConfigValidation(mainConfig.getFile(), mainConfig.getConfiguration(), 3).check();

		loadMongo();
		loadRedis();

		redisCache = new RedisCache(this);
		convenient = new Convenient(this);
		chat = new Chat(this);

		Arrays.asList(
				new BroadcastCommand(),
				new ClearCommand(),
				new DayCommand(),
				new HealCommand(),
				new HidePlayerCommand(),
				new LocationCommand(),
				new MoreCommand(),
				new NightCommand(),
				new RenameCommand(),
				new SetSlotsCommand(),
				new SetSpawnCommand(),
				new ShowPlayerCommand(),
				new SpawnCommand(),
				new SunsetCommand(),
				new ClearChatCommand(),
				new SlowChatCommand(),
				new AltsCommand(),
				new BanCommand(),
				new CheckCommand(),
				new KickCommand(),
				new MuteCommand(),
				new UnbanCommand(),
				new UnmuteCommand(),
				new WarnCommand(),
				new GrantCommand(),
				new GrantsCommand(),
				new StaffChatCommand(),
				new StaffModeCommand(),
				new MuteChatCommand(),
				new OptionsCommand(),
				new RankAddPermissionCommand(),
				new RankCreateCommand(),
				new RankDeleteCommand(),
				new RankHelpCommand(),
				new RankInfoCommand(),
				new RankInheritCommand(),
				new RankRemovePermissionCommand(),
				new RanksCommand(),
				new RankSetColorCommand(),
				new RankSetPrefixCommand(),
				new RankSetSuffixCommand(),
				new RankSetWeightCommand(),
				new RankUninheritCommand(),
				new SpicaDebugCommand(),
				new TeleportWorldCommand(),
				new MessageCommand(),
				new ReplyCommand(),
				new ToggleGlobalChatCommand(),
				new TogglePrivateMessagesCommand(),
				new ToggleSoundsCommand(),
				new PingCommand(),
				new ListCommand(),
				new HistoryCommand(),
				new ClanCommand(),
				new ClanChatCommand(),
				new ClanCreateCommand(),
				new ClanDisbandCommand(),
				new ClanInfoCommand(),
				new ClanInviteCommand(),
				new ClanJoinCommand(),
				new ClanKickCommand(),
				new ClanLeaveCommand(),
				new ClanRoleCommand(),
				new ClanTagCommand(),
				new ReportCommand(),
				new AddPrefixCommand(),
				new AddSuffixCommand(),
				new SetPrefixCommand(),
				new SetSuffixCommand(),
				new ResetPrefixCommand(),
				new ResetSuffixCommand(),
				new ClearPunishmentsCommand(),
				new ExpBoosterCommand()
		).forEach(honcho::registerCommand);

		honcho.registerTypeAdapter(Rank.class, new RankTypeAdapter());
		honcho.registerTypeAdapter(Profile.class, new ProfileTypeAdapter());
		honcho.registerTypeAdapter(Duration.class, new DurationTypeAdapter());
		honcho.registerTypeAdapter(ChatColor.class, new ChatColorTypeAdapter());
		honcho.registerTypeAdapter(ClanPlayerRole.class, new ClanPlayerRoleTypeAdapter());

		pidgin = new Pidgin("tCore",
				mainConfig.getString("REDIS.HOST"),
				mainConfig.getInteger("REDIS.PORT"),
				mainConfig.getBoolean("REDIS.AUTHENTICATION.ENABLED") ?
						mainConfig.getString("REDIS.AUTHENTICATION.PASSWORD") : null
		);

		Arrays.asList(
				PacketAddGrant.class,
				PacketBroadcastPunishment.class,
				PacketDeleteGrant.class,
				PacketDeleteRank.class,
				PacketRefreshRank.class,
				PacketStaffChat.class,
				PacketStaffJoinNetwork.class,
				PacketStaffLeaveNetwork.class,
				PacketStaffSwitchServer.class,
				PacketClickableBroadcast.class,
				PacketFriendAccepted.class,
				PacketFriendDelete.class,
				PacketFriendSendRequest.class,
				PacketFriendJoinNetwork.class,
				PacketClanBroadcast.class,
				PacketClanChat.class,
				PacketClanDisband.class,
				PacketClanInvite.class,
				PacketClanInviteDenied.class,
				PacketClanJoin.class,
				PacketClanLeave.class,
				PacketClanPlayerRoleChange.class,
				PacketReportPlayer.class,
				PacketAddPrefix.class,
				PacketAddSuffix.class,
				PacketResetPrefix.class,
				PacketResetSuffix.class,
				PacketSetPrefix.class,
				PacketSetSuffix.class,
				PacketClearPunishments.class,
				PacketExpBoosterApply.class,
				PacketExpBoosterRemove.class
		).forEach(pidgin::registerPacket);

		pidgin.registerListener(new NetworkPacketListener(this));

		loadServerStatus();

		Arrays.asList(
				new ProfileListener(this),
				new MenuListener(this),
				new ConvenientListener(this),
				new ChatListener(this),
				new GrantListener(this),
				new PunishmentListener(this),
				new WorldListener(),
				new EnderpearlFixListener()
		).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

		Rank.init();

		new BukkitRunnable() {
			@Override
			public void run() {
				for (Profile profile : Profile.getProfiles().values()) {
					profile.checkGrants();
				}
			}
		}.runTaskTimerAsynchronously(this, 20L, 20L);

		this.getServer().getScheduler().runTaskTimer(this, new MenuUpdateTask(), 20L, 20L);

		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
	}

	public void setBoardManager(BoardManager manager) {
		this.boardManager = manager;
		this.boardManager.runTaskTimerAsynchronously(this, manager.getAdapter().getInterval(), manager.getAdapter().getInterval());
	}

	@Override
	public void onDisable() {
		try {
			jedisPool.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void debug(Level level, String message, Exception exception) {
		getLogger().log(level, message);
		exception.printStackTrace();
	}

	public void debug(String message) {
		if (debug) {
			broadcastOps(Style.translate("&e(Debug) &r" + message));
		}
	}


	public void debug(Player player, String message) {
		if (debug) {
			broadcastOps(Style.translate("&e(Debug) &r" + player.getDisplayName() + ": " + message));
		}
	}

	public static void broadcastOps(String message) {
		Bukkit.getOnlinePlayers().stream().filter(Player::isOp).forEach(op -> op.sendMessage(message));
	}

	public static void broadcast(String message) {
		Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(message));
	}

	public static void playsound(Sound sound, float volume, float pitch) {
		Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), sound, volume, pitch));
	}

	private void loadMongo() {
		db = new Database(mainConfig);
	}

	private void loadRedis() {
		jedisPool = new JedisPool(mainConfig.getString("REDIS.HOST"), mainConfig.getInteger("REDIS.PORT"));

		if (mainConfig.getBoolean("REDIS.AUTHENTICATION.ENABLED")) {
			try (Jedis jedis = jedisPool.getResource()) {
				jedis.auth(mainConfig.getString("REDIS.AUTHENTICATION.PASSWORD"));
			}
		}
	}

	public static Core get() {
		return core;
	}

	private void loadServerStatus(){
		List<String> list = mainConfig.getStringList("SERVER_STATUS.SERVERS");

		if(list == null) return;

		if(!list.isEmpty()){
			list.forEach(data -> {

				String[] split = data.split(":");

				if(split[0] == null || split[1] == null || split[2] == null) return;

				String name = split[0];
				String address = split[1];
				int port = Integer.parseInt(split[2]);

				ServerStatusAPI.register(name, address, port);

				this.getLogger().info(data + " has been added to cache for server status.");
			});
		}
	}
}
