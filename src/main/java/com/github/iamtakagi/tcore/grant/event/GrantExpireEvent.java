package com.github.iamtakagi.tcore.grant.event;

import com.github.iamtakagi.tcore.grant.Grant;
import com.github.iamtakagi.tcore.util.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

@AllArgsConstructor
@Getter
public class GrantExpireEvent extends BaseEvent {

	private Player player;
	private Grant grant;

}
