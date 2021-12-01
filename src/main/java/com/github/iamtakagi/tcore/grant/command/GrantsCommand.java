package com.github.iamtakagi.tcore.grant.command;

import com.github.iamtakagi.tcore.Locale;
import com.github.iamtakagi.tcore.profile.Profile;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import com.github.iamtakagi.tcore.grant.menu.GrantsListMenu;
import org.bukkit.entity.Player;

@CommandMeta(label = "grants", async = true, permission = "tCore.staff.grants")
public class GrantsCommand {

	public void execute(Player player, @CPL("player") Profile profile) {
		if (profile == null || !profile.isLoaded()) {
			player.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		new GrantsListMenu(profile).openMenu(player);
	}

}
