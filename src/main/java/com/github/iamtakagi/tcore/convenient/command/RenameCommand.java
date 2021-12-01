package com.github.iamtakagi.tcore.convenient.command;

import com.github.iamtakagi.tcore.util.Style;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@CommandMeta(label = "rename", permission = "tCore.staff.rename")
public class RenameCommand {

	public void execute(Player player, String name) {
		if (player.getItemInHand() != null) {
			ItemStack itemStack = player.getItemInHand();
			ItemMeta itemMeta = itemStack.getItemMeta();
			itemMeta.setDisplayName(Style.translate(name));
			itemStack.setItemMeta(itemMeta);

			player.updateInventory();
			player.sendMessage(Style.GREEN + "You renamed the item in your hand.");
		} else {
			player.sendMessage(Style.RED + "There is nothing in your hand.");
		}
	}

}
