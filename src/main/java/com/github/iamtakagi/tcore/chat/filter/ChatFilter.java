package com.github.iamtakagi.tcore.chat.filter;

import com.github.iamtakagi.tcore.Core;
import com.github.iamtakagi.tcore.strap.Strapped;
import org.bukkit.entity.Player;

public abstract class ChatFilter extends Strapped {

	private String command;

	public ChatFilter(Core instance, String command) {
		super(instance);

		this.command = command;
	}

	public abstract boolean isFiltered(String message, String[] words);

	public void punish(Player player) {
		if (command != null) {
			instance.getServer().dispatchCommand(instance.getServer().getConsoleSender(), command
					.replace("{player}", player.getName())
					.replace("{player-uuid}", player.getUniqueId().toString()));
		}
	}

}
