package com.github.iamtakagi.tcore.menu.button;

import com.github.iamtakagi.tcore.menu.Button;
import com.github.iamtakagi.tcore.menu.Menu;
import com.github.iamtakagi.tcore.util.ItemBuilder;
import com.github.iamtakagi.tcore.util.Style;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class BackButton extends Button {

	private Menu back;
	private int size = 0;

	public BackButton(Menu back){
		this.back = back;
	}

	public BackButton(Menu back, int size){
		this.back = back;
		this.size = size;
	}


	@Override
	public ItemStack getButtonItem(Player player) {
		return new ItemBuilder(Material.WOOD_DOOR)
				.name(Style.RED + Style.BOLD + "Back")
				.lore(Arrays.asList(
						Style.RED + "Click here to return to",
						Style.RED + "the previous menu.")
				)
				.build();
	}

	@Override
	public void clicked(Player player, ClickType clickType) {
		Button.playNeutral(player);
		if(size != 0) {
			back.openMenu(player, size);
		}else{
			back.openMenu(player);
		}
	}

}
