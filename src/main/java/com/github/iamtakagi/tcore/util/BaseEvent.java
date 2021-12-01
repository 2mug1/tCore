package com.github.iamtakagi.tcore.util;

import com.github.iamtakagi.tcore.Core;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BaseEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public void call() {
		Core.get().getServer().getPluginManager().callEvent(this);
	}

}
