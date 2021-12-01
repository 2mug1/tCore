package com.github.iamtakagi.tcore.profile.option.command;

import com.qrakn.honcho.command.CommandMeta;
import com.github.iamtakagi.tcore.profile.option.menu.ProfileOptionsMenu;
import org.bukkit.entity.Player;

@CommandMeta(label = { "options", "settings" })
public class OptionsCommand {

	public void execute(Player player) {
		new ProfileOptionsMenu().openMenu(player);
	}

}
