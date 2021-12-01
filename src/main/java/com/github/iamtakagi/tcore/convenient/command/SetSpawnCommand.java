package com.github.iamtakagi.tcore.convenient.command;

import com.github.iamtakagi.tcore.Core;
import com.github.iamtakagi.tcore.util.Style;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = "setspawn", permission = "tCore.admin.setspawn")
public class SetSpawnCommand {

	public void execute(Player player) {
		Core.get().getConvenient().setSpawn(player.getLocation());
		player.sendMessage(Style.GREEN + "You updated this world's spawn.");
	}

}
