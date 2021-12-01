package com.github.iamtakagi.tcore.rank.command;

import com.github.iamtakagi.tcore.rank.Rank;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "rank uninherit", permission = "tCore.admin.rank", async = true)
public class RankUninheritCommand {

	public void execute(CommandSender sender, Rank parent, Rank child) {
		if (parent == null) {
			sender.sendMessage(ChatColor.RED + "A packet with that name does not exist (parent).");
			return;
		}

		if (child == null) {
			sender.sendMessage(ChatColor.RED + "A packet with that name does not exist (child).");
			return;
		}

		if (parent.getInherited().remove(child)) {
			parent.save();
			sender.sendMessage(ChatColor.GREEN + "You made the parent packet " + parent.getDisplayName() +
			                   " uninherit the child packet " + child.getDisplayName() + ".");
		} else {
			sender.sendMessage(ChatColor.RED + "That parent packet does not inherit that child packet.");
		}
	}

}
