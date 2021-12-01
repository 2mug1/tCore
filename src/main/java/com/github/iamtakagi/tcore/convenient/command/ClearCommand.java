package com.github.iamtakagi.tcore.convenient.command;

import com.github.iamtakagi.tcore.util.Style;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandMeta(label = { "clearinv", "clear", "ci" }, permission = "t.admin.clearinv")
public class ClearCommand {

	public void execute(Player player) {
		player.getInventory().setContents(new ItemStack[36]);
		player.getInventory().setArmorContents(new ItemStack[4]);
		player.updateInventory();
		player.sendMessage(Style.GOLD + "You cleared your inventory.");
	}

	public void execute(CommandSender sender, Player player) {
		player.getInventory().setContents(new ItemStack[36]);
		player.getInventory().setArmorContents(new ItemStack[4]);
		player.updateInventory();
		player.sendMessage(Style.GOLD + "Your inventory has been cleared by " + sender.getName());
	}

}
