package com.github.iamtakagi.tcore;

import java.text.MessageFormat;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;

@AllArgsConstructor
public enum Locale {

	FAILED_TO_LOAD_PROFILE("COMMON_ERRORS.FAILED_TO_LOAD_PROFILE"),
	COULD_NOT_RESOLVE_PLAYER("COMMON_ERRORS.COULD_NOT_RESOLVE_PLAYER"),
	PLAYER_NOT_FOUND("COMMON_ERRORS.PLAYER_NOT_FOUND"),
	RANK_NOT_FOUND("COMMON_ERRORS.RANK_NOT_FOUND"),
	STAFF_CHAT("STAFF.CHAT"),
	STAFF_BROADCAST_PREFIX("STAFF.BROADCAST_PREFIX"),
	STAFF_JOIN_NETWORK("STAFF.JOIN_NETWORK"),
	STAFF_SWITCH_SERVER("STAFF.SWITCH_SERVER"),
	STAFF_LEAVE_NETWORK("STAFF.LEAVE_NETWORK"),
	CLEAR_CHAT_BROADCAST("CHAT.CLEAR_CHAT_BROADCAST"),
	MUTE_CHAT_BROADCAST("CHAT.MUTE_CHAT_BROADCAST"),
	DELAY_CHAT_ENABLED_BROADCAST("CHAT.DELAY_CHAT_ENABLED_BROADCAST"),
	DELAY_CHAT_DISABLED_BROADCAST("CHAT.DELAY_CHAT_DISABLED_BROADCAST"),
	CHAT_DELAYED("CHAT.CHAT_DELAYED"),
	NETWORK_BROADCAST_PREFIX("NETWORK.BROADCAST_PREFIX"),
	NETWORK_RANK_REFRESHED("NETWORK.RANK_REFRESH"),
	NETWORK_RANK_DELETED("NETWORK.RANK_DELETE"),
	CONVERSATION_SEND_MESSAGE("CONVERSATION.SEND_MESSAGE"),
	CONVERSATION_RECEIVE_MESSAGE("CONVERSATION.RECEIVE_MESSAGE"),
	OPTIONS_PRIVATE_MESSAGES_ENABLED("OPTIONS.PRIVATE_MESSAGES_ENABLED"),
	OPTIONS_PRIVATE_MESSAGES_DISABLED("OPTIONS.PRIVATE_MESSAGES_DISABLED"),
	OPTIONS_PRIVATE_MESSAGE_SOUND_ENABLED("OPTIONS.PRIVATE_MESSAGE_SOUNDS_ENABLED"),
	OPTIONS_PRIVATE_MESSAGE_SOUND_DISABLED("OPTIONS.PRIVATE_MESSAGE_SOUNDS_DISABLED"),
	OPTIONS_GLOBAL_CHAT_ENABLED("OPTIONS.GLOBAL_CHAT_ENABLED"),
	OPTIONS_GLOBAL_CHAT_DISABLED("OPTIONS.GLOBAL_CHAT_DISABLED");

	private String path;

	public String format(Object... objects) {
		return new MessageFormat(ChatColor.translateAlternateColorCodes('&',
				Core.get().getMainConfig().getString(path))).format(objects);
	}

//	public static final String PUBLIC_CHAT_MUTE_APPLIED = "&bThe public chat has been muted by {actor}";
//	public static final String PUBLIC_CHAT_DELAY_APPLIED = "&bThe public chat has been delayed by {actor}";
//	public static final String CHAT_ATTEMPT_FILTERED = "&cYour message was filtered.";
//	public static final String CHAT_ATTEMPT_PLAYER_MUTED = "&cYou are currently muted for another {time-remaining}.";
//	public static final String CHAT_ATTEMPT_PUBLIC_CHAT_MUTED = "&cThe public chat is currently muted.";
//	public static final String CHAT_ATTEMPT_PUBLIC_CHAT_DELAYED = "&cYou may chat again in {time-remaining}.";
//	public static final String OPTIONS_GLOBAL_CHAT_DISABLED = "&cYou have your public chat disabled.";
//	public static final String OPTIONS_PRIVATE_CHAT_DISABLED = "&cYou have your private messages disabled.";
//	public static final String PTIONS_PRIVATE_CHAT_DISABLED_OTHER = "&cThat player has their private messages disabled.";

}
