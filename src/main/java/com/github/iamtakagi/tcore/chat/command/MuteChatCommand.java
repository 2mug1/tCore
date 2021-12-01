package com.github.iamtakagi.tcore.chat.command;

import com.github.iamtakagi.tcore.Core;
import com.github.iamtakagi.tcore.Locale;
import com.github.iamtakagi.tcore.profile.Profile;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandMeta(label = "mutechat", permission = "tCore.staff.mutechat")
public class MuteChatCommand {

	public void execute(CommandSender sender) {
		Core.get().getChat().togglePublicChatMute();

		String senderName;

		if (sender instanceof Player) {
			Profile profile = Profile.getProfiles().get(((Player) sender).getUniqueId());
			senderName = profile.getActiveRank().getColor() + sender.getName();
		} else {
			senderName = ChatColor.DARK_RED + "Console";
		}

		String context = Core.get().getChat().isPublicChatMuted() ? "muted" : "unmuted";

		Bukkit.broadcastMessage(Locale.MUTE_CHAT_BROADCAST.format(context, senderName));
	}

}
