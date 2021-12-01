package com.github.iamtakagi.tcore.profile.option.menu.button;

import com.github.iamtakagi.tcore.profile.Profile;
import com.github.iamtakagi.tcore.profile.option.menu.ProfileOptionButton;
import com.github.iamtakagi.tcore.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class PrivateChatOptionButton extends ProfileOptionButton {

	@Override
	public String getOptionName() {
		return "&c&lPrivate Chat";
	}

	@Override
	public ItemStack getEnabledItem(Player player) {
		return new ItemBuilder(Material.NAME_TAG).build();
	}

	@Override
	public ItemStack getDisabledItem(Player player) {
		return new ItemBuilder(Material.NAME_TAG).build();
	}

	@Override
	public String getDescription() {
		return "If enabled, you will receive private chat messages.";
	}

	@Override
	public String getEnabledOption() {
		return "Receive private chat messages";
	}

	@Override
	public String getDisabledOption() {
		return "Do not receive private chat messages";
	}

	@Override
	public boolean isEnabled(Player player) {
		return Profile.getProfiles().get(player.getUniqueId()).getOptions().receivingNewConversations();
	}

	@Override
	public void clicked(Player player, ClickType clickType) {
		Profile profile = Profile.getProfiles().get(player.getUniqueId());
		profile.getOptions().receivingNewConversations(!profile.getOptions().receivingNewConversations());
	}

}
