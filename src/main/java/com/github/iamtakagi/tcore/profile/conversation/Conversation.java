package com.github.iamtakagi.tcore.profile.conversation;

import com.github.iamtakagi.tcore.Locale;
import com.github.iamtakagi.tcore.Api;
import com.github.iamtakagi.tcore.profile.Profile;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Conversation {

	@Getter private final UUID initiatedBy;
	@Getter private final UUID target;
	@Getter private long lastMessageSentAt;
	@Getter private UUID lastMessageSentBy;

	public Conversation(UUID initiatedBy, UUID target) {
		this.initiatedBy = initiatedBy;
		this.target = target;
		this.lastMessageSentAt = System.currentTimeMillis();

		Profile initiatorProfile = Profile.getByUuid(initiatedBy);
		initiatorProfile.getConversations().getConversations().put(target, this);

		Profile targetProfile = Profile.getByUuid(target);
		targetProfile.getConversations().getConversations().put(initiatedBy, this);
	}

	public void sendMessage(Player sender, Player target, String message) {
		sender.sendMessage(Locale.CONVERSATION_SEND_MESSAGE.format(
				sender.getName(), sender.getDisplayName(), Api.getColorOfPlayer(sender),
				target.getName(), target.getDisplayName(), Api.getColorOfPlayer(target))
				.replace("%MSG%", message));

		Profile targetProfile = Profile.getByUuid(target.getUniqueId());

		if (targetProfile.getOptions().playingMessageSounds()) {
			target.playSound(target.getLocation(), Sound.SUCCESSFUL_HIT, 1.0F, 1.0F);
		}

		target.sendMessage(Locale.CONVERSATION_RECEIVE_MESSAGE.format(
				target.getName(), target.getDisplayName(), Api.getColorOfPlayer(target),
				sender.getName(), sender.getDisplayName(), Api.getColorOfPlayer(sender))
				.replace("%MSG%", message));

		lastMessageSentAt = System.currentTimeMillis();
		lastMessageSentBy = sender.getUniqueId();
	}

	public boolean validate() {
		Player initiator = Bukkit.getPlayer(initiatedBy);

		if (initiator == null || !initiator.isOnline()) {
			destroy();
			return false;
		}

		Player target = Bukkit.getPlayer(this.target);

		if (target == null || !target.isOnline()) {
			destroy();
			return false;
		}

		return true;
	}

	public void destroy() {
		for (Player player : new Player[] { Bukkit.getPlayer(initiatedBy), Bukkit.getPlayer(target) }) {
			if (player != null && player.isOnline()) {
				Profile profile = Profile.getByUuid(player.getUniqueId());
				profile.getConversations().getConversations().remove(getPartner(player.getUniqueId()));
			}
		}
	}

	public UUID getPartner(UUID compareWith) {
		if (initiatedBy.equals(compareWith)) {
			return target;
		} else if (target.equals(compareWith)) {
			return initiatedBy;
		}

		return null;
	}

}
