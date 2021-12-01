package com.github.iamtakagi.tcore.profile.option.menu.button;

import com.github.iamtakagi.tcore.profile.Profile;
import com.github.iamtakagi.tcore.profile.option.menu.ProfileOptionButton;
import com.github.iamtakagi.tcore.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class PublicChatOptionButton extends ProfileOptionButton {

	@Override
	public String getOptionName() {
		return "&a&lPublic Chat";
	}

	@Override
	public ItemStack getEnabledItem(Player player) {
		return new ItemBuilder(Material.BOOK_AND_QUILL).build();
	}

	@Override
	public ItemStack getDisabledItem(Player player) {
		return new ItemBuilder(Material.BOOK_AND_QUILL).build();
	}

	@Override
	public String getDescription() {
		return "If enabled, you will receive public chat messages.";
	}

	@Override
	public String getEnabledOption() {
		return "Receive public chat messages";
	}

	@Override
	public String getDisabledOption() {
		return "Do not receive public chat messages";
	}

	@Override
	public boolean isEnabled(Player player) {
		return Profile.getProfiles().get(player.getUniqueId()).getOptions().publicChatEnabled();
	}

	@Override
	public void clicked(Player player, ClickType clickType) {
		Profile profile = Profile.getProfiles().get(player.getUniqueId());
		profile.getOptions().publicChatEnabled(!profile.getOptions().publicChatEnabled());
	}

}
