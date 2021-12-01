package com.github.iamtakagi.tcore.convenient.command;

import com.qrakn.honcho.command.CommandMeta;
import com.github.iamtakagi.tcore.util.Style;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

@CommandMeta(label = "tpworld", permission = "tCore.admin.tpworld")
public class TeleportWorldCommand {

	public void execute(Player player, String worldName) {
		World world = Bukkit.getWorld(worldName);

		if (world == null) {
			world = Bukkit.createWorld(new WorldCreator(worldName));
			player.sendMessage(Style.GOLD + "Generating new world \"" + worldName + "\"");
		}

		if (world == null) {
			player.sendMessage(Style.RED + "A world with that name does not exist.");
		} else {
			player.teleport(world.getSpawnLocation());
			player.sendMessage(Style.GOLD + "Teleported you to " + world.getName());
		}
	}

}
