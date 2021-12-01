package com.github.iamtakagi.tcore.rank.command;

import com.github.iamtakagi.tcore.util.Style;
import com.github.iamtakagi.tcore.rank.Rank;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = { "rank removepermission", "rank removeperm", "rank deleteperm", "rank delperm" },
             permission = "tCore.admin.rank",
             async = true)
public class RankRemovePermissionCommand {

	public void execute(CommandSender sender, Rank rank, String permission) {
		if (!rank.removePermission(permission)) {
			sender.sendMessage(Style.RED + "That packet does not have that permission.");
		} else {
			rank.save();
			sender.sendMessage(Style.GREEN + "Successfully removed permission from packet.");
		}
	}

}
