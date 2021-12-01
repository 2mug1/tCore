package com.github.iamtakagi.tcore.grant.command;

import com.github.iamtakagi.tcore.Core;
import com.github.iamtakagi.tcore.Locale;
import com.github.iamtakagi.tcore.profile.Profile;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import com.github.iamtakagi.tcore.grant.packet.PacketAddGrant;
import com.github.iamtakagi.tcore.grant.event.GrantAppliedEvent;
import com.github.iamtakagi.tcore.rank.Rank;
import com.github.iamtakagi.tcore.util.Style;
import com.github.iamtakagi.tcore.util.TimeUtil;
import com.github.iamtakagi.tcore.util.duration.Duration;
import java.util.UUID;

import com.github.iamtakagi.tcore.grant.Grant;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandMeta(label = "grant", async = true, permission = "tCore.staff.grant")
public class GrantCommand {

	public void execute(CommandSender sender, @CPL("player") Profile profile, Rank rank, Duration duration, String reason) {
		if (rank == null) {
			sender.sendMessage(Locale.RANK_NOT_FOUND.format());
			return;
		}

		if (profile == null || !profile.isLoaded()) {
			sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		if (duration.getValue() == -1) {
			sender.sendMessage(Style.RED + "That duration is not valid.");
			sender.sendMessage(Style.RED + "Example: [perm/1y1m1w1d]");
			return;
		}

		UUID addedBy = sender instanceof Player ? ((Player) sender).getUniqueId() : null;
		Grant grant = new Grant(UUID.randomUUID(), rank, addedBy, System.currentTimeMillis(), reason,
				duration.getValue());

		profile.getGrants().add(grant);
		profile.save();
		profile.activateNextGrant();

		Core.get().getPidgin().sendPacket(new PacketAddGrant(profile.getUuid(), grant));

		sender.sendMessage(Style.GREEN + "You applied a `{packet}` packet to `{player}` for {time-remaining}."
				.replace("{packet}", rank.getDisplayName())
				.replace("{player}", profile.getUsername())
				.replace("{time-remaining}", duration.getValue() == Integer.MAX_VALUE ? "forever"
						: TimeUtil.millisToRoundedTime(duration.getValue())));

		Player player = profile.getPlayer();

		if (player != null) {
			new GrantAppliedEvent(player, grant).call();
		}
	}

}
