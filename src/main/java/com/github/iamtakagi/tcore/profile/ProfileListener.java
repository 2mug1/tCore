package com.github.iamtakagi.tcore.profile;

import com.github.iamtakagi.tcore.Core;
import com.github.iamtakagi.tcore.Locale;
import com.github.iamtakagi.tcore.punishment.Punishment;
import com.github.iamtakagi.tcore.punishment.PunishmentType;
import com.github.iamtakagi.tcore.board.Board;
import com.github.iamtakagi.tcore.strap.StrappedListener;
import com.github.iamtakagi.tcore.cache.RedisPlayerData;
import com.github.iamtakagi.tcore.staff.packet.PacketStaffChat;
import com.github.iamtakagi.tcore.util.Style;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ProfileListener extends StrappedListener {

	public ProfileListener(Core instance) {
		super(instance);
	}

	@EventHandler
	public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
		Player player = Bukkit.getPlayer(event.getUniqueId());

		// Need to check if player is still logged in when receiving another login attempt
		// This happens when a player using a custom client can access the server list while in-game (and reconnecting)
		if (player != null && player.isOnline()) {
			event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
			event.setKickMessage(Style.RED + "You tried to login too quickly after disconnecting.\nTry again in a few seconds.");
			instance.getServer().getScheduler().runTask(instance, () -> player.kickPlayer(Style.RED + "Duplicate login kick"));
			return;
		}

		Profile profile = null;

		try {
			profile = new Profile(event.getName(), event.getUniqueId());

			if (!profile.isLoaded()) {
				event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
				event.setKickMessage(Locale.FAILED_TO_LOAD_PROFILE.format());
				return;
			}

			if (profile.getActivePunishmentByType(PunishmentType.BAN) != null) {
				handleBan(event, profile.getActivePunishmentByType(PunishmentType.BAN));
				return;
			}

			profile.setUsername(event.getName());

			if (profile.getFirstSeen() == null) {
				profile.setFirstSeen(System.currentTimeMillis());
			}

			profile.setLastSeen(System.currentTimeMillis());

			if (profile.getCurrentAddress() == null) {
				profile.setCurrentAddress(event.getAddress().getHostAddress());
			}

			if (!profile.getIpAddresses().contains(event.getAddress().getHostAddress())) {
				profile.getIpAddresses().add(event.getAddress().getHostAddress());
			}

			if (!profile.getCurrentAddress().equals(event.getAddress().getHostAddress())) {
				List<Profile> alts = Profile.getByIpAddress(event.getAddress().getHostAddress());

				for (Profile alt : alts) {
					if (alt.getActivePunishmentByType(PunishmentType.BAN) != null) {
						handleBan(event, alt.getActivePunishmentByType(PunishmentType.BAN));
						return;
					}
				}
			}

			profile.save();
		} catch (Exception e) {
			e.printStackTrace();
			instance.debug(Level.SEVERE, "Failed to load profile for " + event.getName(), e);
		}

		if (profile == null || !profile.isLoaded()) {
			event.setKickMessage(Locale.FAILED_TO_LOAD_PROFILE.format());
			event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
			return;
		}

		Profile.getProfiles().put(profile.getUuid(), profile);

		RedisPlayerData playerData = new RedisPlayerData(event.getUniqueId(), event.getName());
		playerData.setLastAction(RedisPlayerData.LastAction.JOINING_SERVER);
		playerData.setLastSeenServer(instance.getMainConfig().getString("SERVER_NAME"));
		playerData.setLastSeenAt(System.currentTimeMillis());

		instance.getRedisCache().updatePlayerData(playerData);
		instance.getRedisCache().updateNameAndUUID(event.getName(), event.getUniqueId());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		Profile profile = Profile.getProfiles().get(player.getUniqueId());
		profile.setupBukkitPlayer(player);

		for (String perm : profile.getActiveGrant().getRank().getAllPermissions()) {
			instance.debug(player, perm);
		}

		if (player.hasPermission("tCore.staff")) {
			player.sendMessage(Style.GOLD + "Your staff mode is currently: " +
					(profile.getStaffOptions().staffModeEnabled() ? Style.GREEN + "Enabled" : Style.RED + "Disabled"));

			if (profile.getStaffOptions().staffModeEnabled()) {
				event.setJoinMessage(null);
			}
		}

		if (instance.getBoardManager() != null) {
			instance.getBoardManager().getPlayerBoards().put(
					player.getUniqueId(),
					new Board(instance, player, instance.getBoardManager().getAdapter())
			);
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();

		if (Profile.getProfiles().get(event.getPlayer().getUniqueId()) != null) {

			Profile profile = Profile.getProfiles().remove(event.getPlayer().getUniqueId());
			profile.setLastSeen(System.currentTimeMillis());

			if (profile.isLoaded()) {
				new BukkitRunnable() {
					@Override
					public void run() {
						try {
							profile.save();
						} catch (Exception e) {
							instance.debug(Level.SEVERE, "Failed to save profile " + event.getPlayer().getName(), e);
						}
					}
				}.runTaskAsynchronously(Core.get());
			}

			RedisPlayerData playerData = new RedisPlayerData(event.getPlayer().getUniqueId(), event.getPlayer().getName());
			playerData.setLastAction(RedisPlayerData.LastAction.LEAVING_SERVER);
			playerData.setLastSeenServer(instance.getMainConfig().getString("SERVER_NAME"));
			playerData.setLastSeenAt(System.currentTimeMillis());

			instance.getRedisCache().updatePlayerData(playerData);

			if (instance.getBoardManager() != null) {
				instance.getBoardManager().getPlayerBoards().remove(event.getPlayer().getUniqueId());
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
		Profile profile = Profile.getByUuid(event.getPlayer().getUniqueId());

		if (profile.getStaffOptions().staffChatModeEnabled()) {
			if (profile.getStaffOptions().staffModeEnabled()) {
				Core.get().getPidgin().sendPacket(new PacketStaffChat(event.getPlayer().getDisplayName(),
						Core.get().getMainConfig().getString("SERVER_NAME"), event.getMessage()));
			} else {
				event.getPlayer().sendMessage(Style.RED + "You must enable staff mode or disable staff chat mode.");
			}

			event.setCancelled(true);
		}
	}

	private void handleBan(AsyncPlayerPreLoginEvent event, Punishment punishment) {
		event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
		event.setKickMessage(punishment.getKickMessage());
	}

}
