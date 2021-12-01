package com.github.iamtakagi.tcore.chat.command;

import com.github.iamtakagi.tcore.Core;
import com.github.iamtakagi.tcore.Locale;
import com.github.iamtakagi.tcore.profile.Profile;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandMeta(label = { "clearchat", "cc" }, permission = "tCore.staff.clearchat")
public class ClearChatCommand {

	public void execute(CommandSender sender) {
		String[] strings = new String[101];

		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.hasPermission("tCore.staff")) {
				if (Core.get().getMainConfig().getBoolean("CHAT.CLEAR_CHAT_FOR_STAFF")) {
					player.sendMessage(strings);
				}
			} else {
				player.sendMessage(strings);
			}
		}

		String senderName;

		if (sender instanceof Player) {
			Profile profile = Profile.getProfiles().get(((Player) sender).getUniqueId());
			senderName = profile.getActiveRank().getColor() + sender.getName();
		} else {
			senderName = ChatColor.DARK_RED + "Console";
		}

		Bukkit.broadcastMessage(Locale.CLEAR_CHAT_BROADCAST.format(senderName));
	}

}
