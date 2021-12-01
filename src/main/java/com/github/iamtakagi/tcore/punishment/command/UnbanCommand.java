package com.github.iamtakagi.tcore.punishment.command;

import com.github.iamtakagi.tcore.Core;
import com.github.iamtakagi.tcore.Locale;
import com.qrakn.honcho.command.CPL;
import com.github.iamtakagi.tcore.punishment.packet.PacketBroadcastPunishment;
import com.github.iamtakagi.tcore.profile.Profile;
import com.github.iamtakagi.tcore.punishment.Punishment;
import com.github.iamtakagi.tcore.punishment.PunishmentType;
import com.github.iamtakagi.tcore.util.Style;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandMeta(label = "unban", permission = "tCore.staff.unban", async = true)
public class UnbanCommand {

	public void execute(CommandSender sender, @CPL("player") Profile profile, String reason) {
		if (profile == null || !profile.isLoaded()) {
			sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		if (profile.getActivePunishmentByType(PunishmentType.BAN) == null) {
			sender.sendMessage(Style.RED + "That player is not banned.");
			return;
		}

		String staffName = sender instanceof Player ? Profile.getProfiles().get(((Player) sender)
				.getUniqueId()).getColoredUsername() : Style.DARK_RED + "Console";

		Punishment punishment = profile.getActivePunishmentByType(PunishmentType.BAN);
		punishment.setRemovedAt(System.currentTimeMillis());
		punishment.setRemovedReason(reason);
		punishment.setRemoved(true);

		if (sender instanceof Player) {
			punishment.setRemovedBy(((Player) sender).getUniqueId());
		}

		profile.save();

		Core.get().getPidgin().sendPacket(new PacketBroadcastPunishment(punishment, staffName,
				profile.getColoredUsername(), profile.getUuid(), false, true));
	}

}
