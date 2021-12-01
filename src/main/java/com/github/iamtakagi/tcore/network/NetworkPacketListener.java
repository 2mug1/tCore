package com.github.iamtakagi.tcore.network;

import com.github.iamtakagi.tcore.Core;
import com.github.iamtakagi.tcore.Locale;
import com.github.iamtakagi.tcore.clan.packet.*;
import com.github.iamtakagi.tcore.convenient.packet.PacketClickableBroadcast;
import com.github.iamtakagi.tcore.nametag.packet.*;
import com.github.iamtakagi.tcore.profile.Profile;
import com.github.iamtakagi.tcore.profile.experience.ExpBooster;
import com.github.iamtakagi.tcore.profile.experience.packet.PacketExpBoosterApply;
import com.github.iamtakagi.tcore.profile.experience.packet.PacketExpBoosterRemove;
import com.github.iamtakagi.tcore.punishment.Punishment;
import com.github.iamtakagi.tcore.punishment.packet.PacketBroadcastPunishment;
import com.github.iamtakagi.tcore.punishment.packet.PacketClearPunishments;
import com.github.iamtakagi.tcore.staff.packet.*;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import com.github.iamtakagi.tcore.clan.Clan;
import com.github.iamtakagi.tcore.clan.ClanPlayer;
import com.github.iamtakagi.tcore.friend.packet.PacketFriendAccepted;
import com.github.iamtakagi.tcore.friend.packet.PacketFriendDelete;
import com.github.iamtakagi.tcore.friend.packet.PacketFriendJoinNetwork;
import com.github.iamtakagi.tcore.friend.packet.PacketFriendSendRequest;
import com.github.iamtakagi.tcore.grant.packet.PacketAddGrant;
import com.github.iamtakagi.tcore.grant.packet.PacketDeleteGrant;
import com.github.iamtakagi.tcore.rank.packet.PacketDeleteRank;
import com.github.iamtakagi.tcore.rank.packet.PacketRefreshRank;
import com.github.iamtakagi.tcore.pidgin.packet.handler.IncomingPacketHandler;
import com.github.iamtakagi.tcore.pidgin.packet.listener.PacketListener;
import com.github.iamtakagi.tcore.staff.event.ReceiveStaffChatEvent;
import com.github.iamtakagi.tcore.grant.Grant;
import com.github.iamtakagi.tcore.grant.event.GrantAppliedEvent;
import com.github.iamtakagi.tcore.grant.event.GrantExpireEvent;
import com.github.iamtakagi.tcore.rank.Rank;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import com.github.iamtakagi.tcore.util.Style;
import com.github.iamtakagi.tcore.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class NetworkPacketListener implements PacketListener {

	private Core instance;

	public NetworkPacketListener(Core instance) {
		this.instance = instance;
	}

	@IncomingPacketHandler
	public void onAddGrant(PacketAddGrant packet) {
		Player player = Bukkit.getPlayer(packet.getPlayerUuid());
		Grant grant = packet.getGrant();

		if (player != null) {
			Profile profile = Profile.getProfiles().get(player.getUniqueId());
			profile.getGrants().removeIf(other -> Objects.equals(other, grant));
			profile.getGrants().add(grant);

			new GrantAppliedEvent(player, grant);
		}
	}

	@IncomingPacketHandler
	public void onDeleteGrant(PacketDeleteGrant packet) {
		Player player = Bukkit.getPlayer(packet.getPlayerUuid());
		Grant grant = packet.getGrant();

		if (player != null) {
			Profile profile = Profile.getProfiles().get(player.getUniqueId());
			profile.getGrants().removeIf(other -> Objects.equals(other, grant));
			profile.getGrants().add(grant);

			new GrantExpireEvent(player, grant);
		}
	}

	@IncomingPacketHandler
	public void onBroadcastPunishment(PacketBroadcastPunishment packet) {
		Punishment punishment = packet.getPunishment();
		punishment.broadcast(packet.getStaff(), packet.getTarget(), packet.isSilent(), packet.isRemoved());

		Player player = Bukkit.getPlayer(packet.getTargetUuid());

		if (player != null) {
			Profile profile = Profile.getProfiles().get(player.getUniqueId());
			profile.getPunishments().removeIf(other -> Objects.equals(other, punishment));
			profile.getPunishments().add(punishment);

			if (punishment.getType().isBan()) {
				new BukkitRunnable() {
					@Override
					public void run() {
						player.kickPlayer(punishment.getKickMessage());
					}
				}.runTask(Core.get());
			}
		}
	}

	@IncomingPacketHandler
	public void onRankRefresh(PacketRefreshRank packet) {
		Rank rank = Rank.getRankByUuid(packet.getUuid());

		if (rank == null) {
			rank = new Rank(packet.getUuid(), packet.getName());
		}

		rank.load();

		Core.broadcastOps(Locale.NETWORK_RANK_REFRESHED.format(Locale.NETWORK_BROADCAST_PREFIX.format(),
				rank.getDisplayName()));
	}

	@IncomingPacketHandler
	public void onRankDelete(PacketDeleteRank packet) {
		Rank rank = Rank.getRanks().remove(packet.getUuid());

		if (rank != null) {
			Core.broadcastOps(Locale.NETWORK_RANK_DELETED.format(Locale.NETWORK_BROADCAST_PREFIX.format(),
					rank.getDisplayName()));
		}
	}

	@IncomingPacketHandler
	public void onStaffChat(PacketStaffChat packet) {
		instance.getServer().getOnlinePlayers().stream()
				.filter(onlinePlayer -> onlinePlayer.hasPermission("tCore.staff"))
				.forEach(onlinePlayer -> {
					ReceiveStaffChatEvent event = new ReceiveStaffChatEvent(onlinePlayer);

					instance.getServer().getPluginManager().callEvent(event);

					if (!event.isCancelled()) {
						Profile profile = Profile.getProfiles().get(event.getPlayer().getUniqueId());

						if (profile != null && profile.getStaffOptions().staffModeEnabled()) {
							onlinePlayer.sendMessage(Locale.STAFF_CHAT.format(Locale.STAFF_BROADCAST_PREFIX.format(),
									packet.getPlayerName(), packet.getServerName(), packet.getChatMessage()
							));
						}
					}
				});
	}

	@IncomingPacketHandler
	public void onStaffJoinNetwork(PacketStaffJoinNetwork packet) {
		instance.getServer().broadcast(Locale.STAFF_JOIN_NETWORK.format(Locale.STAFF_BROADCAST_PREFIX.format(),
				packet.getPlayerName(), packet.getServerName()), "tCore.staff");
	}

	@IncomingPacketHandler
	public void onStaffLeaveNetwork(PacketStaffLeaveNetwork packet) {
		instance.getServer().broadcast(Locale.STAFF_LEAVE_NETWORK.format(Locale.STAFF_BROADCAST_PREFIX.format(),
				packet.getPlayerName()), "tCore.staff");
	}

	@IncomingPacketHandler
	public void onStaffSwitchServer(PacketStaffSwitchServer packet) {
		instance.getServer().broadcast(Locale.STAFF_SWITCH_SERVER.format(Locale.STAFF_BROADCAST_PREFIX.format(),
				packet.getPlayerName(), packet.getToServerName(), packet.getFromServerName()), "tCore.staff");
	}

	@IncomingPacketHandler
	public void onPacketClickableBroadcast(PacketClickableBroadcast packet) {
		instance.getServer().spigot().broadcast(
				new ComponentBuilder(
						ChatColor.translateAlternateColorCodes('&', packet.getMessage())).
						append(" ").
						append(ChatColor.translateAlternateColorCodes('&', packet.getClickableMessage())).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, packet.getPerformCommand()))
						.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', packet.getHoverText())).create()))
						.create());
	}

	@IncomingPacketHandler
	public void onPacketSendFriendRequest(PacketFriendSendRequest packet) {
		Player player = Bukkit.getPlayer(UUID.fromString(packet.getTargetUUID()));
		if (player != null) {
			player.spigot().sendMessage(
					new ComponentBuilder(Profile.getByUuid(UUID.fromString(packet.getSenderUUID())).getColoredUsername() + Style.PINK + " has been sent friend request to you").
							append(" ").
							append(Style.GREEN + "(Click to Accept)")
							.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Style.WHITE + "Click to accept").create())).
							event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend accept " + packet.getSenderUUID())).create());
		}
	}

	@IncomingPacketHandler
	public void onPacketFriendAccept(PacketFriendAccepted packet) {
		Player player = Bukkit.getPlayer(UUID.fromString(packet.getTargetUUID()));
		if (player != null) {
			player.sendMessage(Profile.getByUuid(UUID.fromString(packet.getSenderUUID())).getColoredUsername() + Style.GREEN + " has been accepted your request.");
			player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
		}
	}

	@IncomingPacketHandler
	public void onPacketFriendDelete(PacketFriendDelete packet) {
		Player player = Bukkit.getPlayer(UUID.fromString(packet.getTargetUUID()));
		if (player != null) {
			player.sendMessage(Profile.getByUuid(UUID.fromString(packet.getSenderUUID())).getColoredUsername() + Style.RED + " has been unfriended you.");
			player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
		}
	}

	@IncomingPacketHandler
	public void onPacketFriendJoin(PacketFriendJoinNetwork packet){
		for(String uuid : packet.getFriendsUUID()){
			UUID playerUUID = UUID.fromString(uuid);
			Player player = Bukkit.getPlayer(playerUUID);
			if(player != null && player.isOnline()){
				player.sendMessage(Style.YELLOW + packet.getPlayerName() + " has joined the network.");
			}
		}
	}

	@IncomingPacketHandler
	public void onPacketClanBroadcast(PacketClanBroadcast packet){
		for(ClanPlayer clanPlayer : packet.getClan().getMembers()){
			Player player = clanPlayer.toPlayer();
			if(player != null){
				player.sendMessage(Style.GOLD + Style.BOLD + "Clan" + Style.GRAY + Style.GRAY + " > " + Style.RESET + ChatColor.translateAlternateColorCodes('&', packet.getMessage()));
				player.playSound(player.getLocation(), Sound.NOTE_PIANO, 0.5F, 0.5F);
			}
		}
	}

	@IncomingPacketHandler
	public void onPacketClanChat(PacketClanChat packet) {
		Clan clan = packet.getClan();
		Profile profile = Profile.getByUuid(packet.getSender().getUuid());
		clan.broadcast(Style.RESET + "(" + profile.getExperience().getLevel() + ") " +
				profile.getColoredUsername() + Style.GRAY + ": " +
				Style.RESET + ChatColor.translateAlternateColorCodes('&', packet.getMessage()));
	}

	@IncomingPacketHandler
	public void onPacketClanDisband(PacketClanDisband packet){
		packet.getClan().broadcast(Style.RED + "Your clan has been disbanded by Owner.");
	}

	@IncomingPacketHandler
	public void onPacketClanInvite(PacketClanInvite packet){
		Player player = packet.getTarget().toPlayer();
		if(player != null) {
			Clan clan = packet.getClan();
			player.spigot().sendMessage(
					new ComponentBuilder(
							Style.GOLD + Style.BOLD + "Clan" + Style.GRAY + Style.GRAY + " > " + Style.RESET + Style.GREEN + "You've been invited from "  + clan.getName()).
					append(" ").
					append(Style.GREEN + "(Click to Join)")
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Style.WHITE + "Click to Join").create())).
							event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan join " + packet.getClan().getName())).create());
		}
	}

	@IncomingPacketHandler
	public void onPacketClanJoin(PacketClanJoin packet){
		Clan clan = packet.getClan();
		clan.broadcast(Profile.getByUuid(packet.getJoinPlayer().getUuid()).getColoredUsername() + Style.YELLOW + " has joined.");
	}

	@IncomingPacketHandler
	public void onPacketRoleChange(PacketClanPlayerRoleChange packet){
		ClanPlayer clanPlayer = packet.getChangedPlayer();
		Player player = clanPlayer.toPlayer();
		if(player != null){
			player.sendMessage(Style.GOLD + Style.BOLD + "Clan" + Style.GRAY + Style.GRAY + " > " + Style.GREEN + "Your role has been changed to " + clanPlayer.getRole().getColor() + clanPlayer.getRole().name() + Style.GREEN + " by Clan Owner");
		}
	}

	@IncomingPacketHandler
	public void onPacketClanInviteDenied(PacketClanInviteDenied packet){
		for(ClanPlayer moreThanLeader : packet.getClan().getMoreThanLeaders()) {
			Player player = moreThanLeader.toPlayer();
			if (player != null) {
				player.sendMessage(Style.GOLD + Style.BOLD + "Clan" + Style.GRAY + Style.GRAY + " > " + Style.RED + packet.getDeniedPlayerName() + " has denied your invitation.");
			}
		}
	}

	@IncomingPacketHandler
	public void onPacketClanKick(PacketClanKick packet){
		Player player = Bukkit.getPlayer(packet.getKickedPlayerUUID());
		Clan clan = packet.getClan();
		clan.broadcast(Style.RED + packet.getKickedPlayerName() + " has been kicked.");
		if(player != null){
			player.sendMessage(Style.GOLD + Style.BOLD + "Clan" + Style.GRAY + Style.GRAY + " > " + Style.RED + "You've been kicked from " + packet.getClan().getName());
		}
	}

	@IncomingPacketHandler
	public void onPacketClanLeave(PacketClanLeave packet){
		Clan clan = packet.getClan();
		clan.broadcast(Style.RED + packet.getLeavePlayerName() + " has left clan.");
	}

	@IncomingPacketHandler
	public void onPacketPlayerReport(PacketReportPlayer packet) {
		instance.getServer().getOnlinePlayers().stream()
				.filter(onlinePlayer -> onlinePlayer.hasPermission("tCore.staff"))
				.forEach(onlinePlayer -> {

					Profile profile = Profile.getProfiles().get((onlinePlayer.getUniqueId()));

					if (profile != null && profile.getStaffOptions().staffModeEnabled()) {
						Arrays.asList(
								Style.HORIZONTAL_SEPARATOR,
								Style.RED + Style.BOLD + " Report",
								Style.YELLOW + " Target: " + packet.getSenderName(),
								Style.YELLOW + " Reason: " + packet.getReason(),
								Style.YELLOW + " Server: " + packet.getFromServer(),
								Style.YELLOW + " Sent by: " + packet.getSenderName(),
								Style.YELLOW + " Sent at: " + TimeUtil.dateToString(new Date(packet.getSentAt()), null),
								Style.HORIZONTAL_SEPARATOR)
								.forEach(s -> {
									onlinePlayer.sendMessage(s);
								});
					}
				});
	}

	@IncomingPacketHandler
	public void onPacketAddPrefix(PacketAddPrefix packet){
		UUID uuid = packet.getUuid();

		Profile profile = Profile.getByUuid(uuid);
		profile.setPrefix(profile.getPrefix() + packet.getPrefix());
		profile.save();


		Player player = Bukkit.getPlayer(uuid);

		if(player != null) {

			player.sendMessage("Added one to your prefix.");
		}
	}

	@IncomingPacketHandler
	public void onPacketAddSuffix(PacketAddSuffix packet){
		UUID uuid = packet.getUuid();

		Profile profile = Profile.getByUuid(uuid);
		profile.setSuffix(profile.getSuffix() + packet.getSuffix());
		profile.save();

		Player player = Bukkit.getPlayer(uuid);

		if(player != null) {

			player.sendMessage("Added one to your suffix.");
		}
	}

	@IncomingPacketHandler
	public void onPacketSetPrefix(PacketSetPrefix packet){
		UUID uuid = packet.getUuid();

		Profile profile = Profile.getByUuid(uuid);
		profile.setPrefix(packet.getPrefix());
		profile.save();

		Player player = Bukkit.getPlayer(uuid);

		if(player != null) {

			player.sendMessage("Your prefix has been updated.");
		}
	}

	@IncomingPacketHandler
	public void onPacketSetSuffix(PacketSetSuffix packet){
		UUID uuid = packet.getUuid();

		Profile profile = Profile.getByUuid(uuid);
		profile.setSuffix(packet.getSuffix());
		profile.save();

		Player player = Bukkit.getPlayer(uuid);

		if(player != null) {

			player.sendMessage("Your suffix has been updated.");
		}
	}

	@IncomingPacketHandler
	public void onPacketResetPrefix(PacketResetPrefix packet){
		UUID uuid = packet.getUuid();

		Profile profile = Profile.getByUuid(uuid);
		profile.setPrefix(null);
		profile.save();

		Player player = Bukkit.getPlayer(uuid);

		if(player != null) {

			player.sendMessage("Your prefix has been reset.");
		}
	}

	@IncomingPacketHandler
	public void onPacketResetSuffix(PacketResetSuffix packet){
		UUID uuid = packet.getUuid();

		Profile profile = Profile.getByUuid(uuid);
		profile.setSuffix(null);
		profile.save();

		Player player = Bukkit.getPlayer(uuid);

		if(player != null) {

			player.sendMessage("Your suffix has been reset.");
		}
	}

	@IncomingPacketHandler
	public void onPacketClearPunishments(PacketClearPunishments packet){
		UUID uuid = packet.getUuid();

		Profile profile = Profile.getByUuid(uuid);
		profile.getPunishments().clear();
		profile.save();

		Player player = Bukkit.getPlayer(uuid);

		if(player != null){

			player.sendMessage(Style.GREEN + "Your punishment history has been all removed.");
		}
	}

	@IncomingPacketHandler
	public void onPacketExpBoosterApply(PacketExpBoosterApply packet){
		ExpBooster expBooster = packet.getExpBooster();
		Profile profile = Profile.getByUuid(packet.getUuid());

		profile.setExpBooster(expBooster);
		profile.save();

		Player player = Bukkit.getPlayer(packet.getUuid());

		if(player != null){

			player.sendMessage(Style.GREEN + "A new exp booster" + Style.GRAY + "(" + expBooster.getIncreaseRate() + "x) "  + Style.GREEN + "has been applied to your profile.");
		}
	}

	@IncomingPacketHandler
	public void onPacketExpBoosterRemove(PacketExpBoosterRemove packet){
		Profile profile = Profile.getByUuid(packet.getTarget());

		ExpBooster expBooster = profile.getExpBooster();
		expBooster.setRemoved(true);
		expBooster.setRemovedBy(packet.getRemovedBy());
		expBooster.setRemovedAt(packet.getRemovedAt());
		expBooster.setRemovedReason(packet.getRemovedReason());

		profile.setExpBooster(expBooster);
		profile.save();

		Player player = Bukkit.getPlayer(packet.getTarget());

		if(player != null){

			player.sendMessage(Style.GREEN + "Your exp booster has been removed.");
		}
	}


}
