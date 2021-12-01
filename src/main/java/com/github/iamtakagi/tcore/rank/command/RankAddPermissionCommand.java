package com.github.iamtakagi.tcore.rank.command;

import com.github.iamtakagi.tcore.util.Style;
import com.github.iamtakagi.tcore.rank.Rank;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = { "rank addpermission", "rank addperm" }, permission = "tCore.admin.rank", async = true)
public class RankAddPermissionCommand {

	public void execute(CommandSender sender, Rank rank, String permission) {
		if (!rank.addPermission(permission)) {
			sender.sendMessage(Style.RED + "That packet already has that permission.");
		} else {
			rank.save();
			sender.sendMessage(Style.GREEN + "Successfully added permission to rank.");
		}
	}

}
