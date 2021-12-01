package com.github.iamtakagi.tcore.profile.option;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class ProfileOptions {

	@Getter @Setter private boolean publicChatEnabled = true;
	@Getter @Setter private boolean receivingNewConversations = true;
	@Getter @Setter private boolean playingMessageSounds = true;

}
