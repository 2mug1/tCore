package com.github.iamtakagi.tcore.convenient.command;

import com.github.iamtakagi.tcore.util.LocationUtil;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = "loc", permission = "tCore.admin.loc")
public class LocationCommand {

	public void execute(Player player) {
		player.sendMessage(LocationUtil.serialize(player.getLocation()));
		System.out.println(LocationUtil.serialize(player.getLocation()));
	}

}
