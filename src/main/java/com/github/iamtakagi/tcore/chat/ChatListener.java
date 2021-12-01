package com.github.iamtakagi.tcore.chat;

import com.github.iamtakagi.tcore.Core;
import com.github.iamtakagi.tcore.Locale;
import com.github.iamtakagi.tcore.profile.Profile;
import com.github.iamtakagi.tcore.strap.StrappedListener;
import com.github.iamtakagi.tcore.util.Style;
import com.github.iamtakagi.tcore.util.TimeUtil;
import com.github.iamtakagi.tcore.chat.event.ChatAttemptEvent;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener extends StrappedListener {

	public ChatListener(Core instance) {
		super(instance);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
		ChatAttempt chatAttempt = instance.getChat().attemptChatMessage(event.getPlayer(), event.getMessage());
		ChatAttemptEvent chatAttemptEvent = new ChatAttemptEvent(event.getPlayer(), chatAttempt, event.getMessage());

		instance.getServer().getPluginManager().callEvent(chatAttemptEvent);

		if (!chatAttemptEvent.isCancelled()) {
			switch (chatAttempt.getResponse()) {
				case ALLOWED: {
					Profile profile = Profile.getByUuid(event.getPlayer().getUniqueId());
					event.setFormat(
							(profile.getPrefix() == null ? "" : ChatColor.translateAlternateColorCodes('&', profile.getPrefix())) +
									Style.RESET +
									profile.getActiveRank().getColor() + "%1$s" + Style.RESET +
							(profile.getSuffix() == null ? "" : ChatColor.translateAlternateColorCodes('&', profile.getSuffix()))
							+ Style.RESET + ": " + "%2$s");
				}
				break;
				case MESSAGE_FILTERED: {
					event.setCancelled(true);
					chatAttempt.getFilterFlagged().punish(event.getPlayer());
				}
				break;
				case PLAYER_MUTED: {
					event.setCancelled(true);

					if (chatAttempt.getPunishment().isPermanent()) {
						event.getPlayer().sendMessage(Style.RED + "You are muted for forever." + Style.GRAY + " (Reason: " + chatAttempt.getPunishment().getAddedReason() +  ")" + Style.YELLOW + " https://xxx.com/punishments/" + chatAttempt.getPunishment().getId());
					} else {
						event.getPlayer().sendMessage(Style.RED + "You are muted for another " + chatAttempt.getPunishment().getTimeRemaining() + "." +  Style.GRAY + " (Reason: " + chatAttempt.getPunishment().getAddedReason() +  ")" + Style.YELLOW + " https://xxx.com/punishments/" + chatAttempt.getPunishment().getId() );
					}
				}
				break;
				case CHAT_MUTED: {
					event.setCancelled(true);
					event.getPlayer().sendMessage(Style.RED + "The public chat is currently muted.");
				}
				break;
				case CHAT_DELAYED: {
					event.setCancelled(true);
					event.getPlayer().sendMessage(Locale.CHAT_DELAYED.format(
							TimeUtil.millisToSeconds((int) chatAttempt.getValue())) + " seconds");
				}
				break;
			}
		}
	}

}
