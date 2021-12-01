package com.github.iamtakagi.tcore.convenient.command;

import com.github.iamtakagi.tcore.Locale;
import com.github.iamtakagi.tcore.util.Style;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandMeta(label = "heal", permission = "tCore.admin.heal")
public class HealCommand {

	public void execute(Player player) {
		player.setHealth(20.0);
		player.setFoodLevel(20);
		player.setSaturation(5.0F);
		player.updateInventory();
		player.sendMessage(Style.GOLD + "You healed yourself.");
	}

	public void execute(CommandSender sender, Player player) {
		if (player == null) {
			sender.sendMessage(Locale.PLAYER_NOT_FOUND.format());
			return;
		}

		player.setHealth(20.0);
		player.setFoodLevel(20);
		player.setSaturation(5.0F);
		player.updateInventory();
		player.sendMessage(Style.GOLD + "You have been healed by " + sender.getName());
	}

}
