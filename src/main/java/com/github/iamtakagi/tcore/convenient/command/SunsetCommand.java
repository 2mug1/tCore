package com.github.iamtakagi.tcore.convenient.command;

import com.github.iamtakagi.tcore.util.Style;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = "sunset")
public class SunsetCommand {

	public void execute(Player player) {
		player.setPlayerTime(12000, false);
		player.sendMessage(Style.GREEN + "It's now sunset.");
	}

}
