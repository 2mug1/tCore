package com.github.iamtakagi.tcore.punishment.command;

import com.github.iamtakagi.tcore.Core;
import com.github.iamtakagi.tcore.Locale;
import com.qrakn.honcho.command.CPL;
import com.github.iamtakagi.tcore.cache.RedisPlayerData;
import com.github.iamtakagi.tcore.profile.Profile;
import com.github.iamtakagi.tcore.punishment.menu.PunishmentsListMenu;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = { "check", "c" }, permission = "tCore.staff.check", async = true)
public class CheckCommand {

	public void execute(Player player, @CPL("player") Profile profile) {
		if (profile == null || !profile.isLoaded()) {
			player.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		RedisPlayerData redisPlayerData = Core.get().getRedisCache().getPlayerData(profile.getUuid());

		if (redisPlayerData == null) {
			player.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		new PunishmentsListMenu(profile, redisPlayerData).openMenu(player);
	}

}
