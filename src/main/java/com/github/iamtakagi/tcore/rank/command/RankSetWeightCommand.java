package com.github.iamtakagi.tcore.rank.command;

import com.github.iamtakagi.tcore.util.Style;
import com.github.iamtakagi.tcore.rank.Rank;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "rank setweight", permission = "tCore.admin.rank", async = true)
public class RankSetWeightCommand {

	public void execute(CommandSender sender, Rank rank, String weight) {
		if (rank == null) {
			sender.sendMessage(Style.RED + "A packet with that name does not exist.");
			return;
		}

		try {
			Integer.parseInt(weight);
		} catch (Exception e) {
			sender.sendMessage(Style.RED + "Invalid number.");
			return;
		}

		rank.setWeight(Integer.parseInt(weight));
		rank.save();

		sender.sendMessage(Style.GREEN + "You updated the packet's weight.");
	}

}
