package com.github.iamtakagi.tcore.convenient.command;

import com.github.iamtakagi.tcore.Core;
import com.github.iamtakagi.tcore.util.Style;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = "spawn", permission = "tCore.staff.spawn")
public class SpawnCommand {

	public void execute(Player player) {
		Core.get().getConvenient().teleportToSpawn(player);
		player.sendMessage(Style.GREEN + "You teleported to this world's spawn.");
	}

}
