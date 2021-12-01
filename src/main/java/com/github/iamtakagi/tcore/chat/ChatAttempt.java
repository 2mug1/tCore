package com.github.iamtakagi.tcore.chat;

import com.github.iamtakagi.tcore.punishment.Punishment;
import com.github.iamtakagi.tcore.chat.filter.ChatFilter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatAttempt {

	private Response response;
	private ChatFilter filterFlagged;
	private Punishment punishment;
	private Object value;

	public ChatAttempt(Response response) {
		this.response = response;
	}

	public ChatAttempt(Response response, ChatFilter filterFlagged) {
		this.response = response;
		this.filterFlagged = filterFlagged;
	}

	public ChatAttempt(Response response, Punishment punishment) {
		this.response = response;
		this.punishment = punishment;
	}

	public enum Response {
		ALLOWED,
		MESSAGE_FILTERED,
		PLAYER_MUTED,
		CHAT_MUTED,
		CHAT_DELAYED
	}

}
