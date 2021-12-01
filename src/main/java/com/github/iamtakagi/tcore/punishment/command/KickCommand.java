package com.github.iamtakagi.tcore.punishment.command;

import com.github.iamtakagi.tcore.Core;
import com.github.iamtakagi.tcore.Locale;
import com.github.iamtakagi.tcore.punishment.packet.PacketBroadcastPunishment;
import com.github.iamtakagi.tcore.profile.Profile;
import com.github.iamtakagi.tcore.punishment.Punishment;
import com.github.iamtakagi.tcore.punishment.PunishmentType;
import com.github.iamtakagi.tcore.util.Style;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@CommandMeta(label = "kick", permission = "tCore.staff.kick", async = true)
public class KickCommand {

	public void execute(CommandSender sender, Player player, String reason) {
		if (player == null) {
			sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		Profile profile = Profile.getProfiles().get(player.getUniqueId());

		if (profile == null || !profile.isLoaded()) {
			sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		String staffName = sender instanceof Player ? Profile.getProfiles().get(((Player) sender)
				.getUniqueId()).getColoredUsername() : Style.DARK_RED + "Console";

		Punishment punishment = new Punishment(Punishment.getCurrentPunishmentSize() + 1, PunishmentType.KICK, System.currentTimeMillis(),
				reason, -1);

		if (sender instanceof Player) {
			punishment.setAddedBy(((Player) sender).getUniqueId());
		}

		profile.getPunishments().add(punishment);
		profile.save();

		Core.get().getPidgin().sendPacket(new PacketBroadcastPunishment(punishment, staffName,
				profile.getColoredUsername(), profile.getUuid(), false, false));

		new BukkitRunnable() {
			@Override
			public void run() {
				player.kickPlayer(punishment.getKickMessage());
			}
		}.runTask(Core.get());
	}

}
