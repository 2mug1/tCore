package com.github.iamtakagi.tcore.rank.command;

import com.github.iamtakagi.tcore.util.Style;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = { "rank", "rank help" }, permission = "tCore.admin.rank")
public class RankHelpCommand {

	private static final String[][] HELP;

	static {
		HELP = new String[][]{
				new String[]{ "ranks", "List all existing ranks" },
				new String[]{ "rank create <name>", "Create a new rank" },
				new String[]{ "rank delete <rank>", "Delete an existing rank" },
				new String[]{ "rank setcolor <rank> <color>", "Set a ranks's color" },
				new String[]{ "rank setprefix <rank> <prefix>", "Set a ranks's prefix" },
				new String[]{ "rank setsuffix <rank> <suffix>", "Set a ranks's suffix" },
				new String[]{ "rank setweight <rank> <weight>", "Set a ranks's weight" },
				new String[]{ "rank addperm <rank> <permission>", "Add a permission to a rank" },
				new String[]{ "rank delperm <rank> <permission>", "Remove a permission from a rank" },
				new String[]{ "rank inherit <parent> <child>", "Make a parent rank inherit a child rank" },
				new String[]{ "rank uninherit <parent> <child>", "Make a parent rank uninherit a child rank" }
		};
	}

	public void execute(CommandSender sender) {
		sender.sendMessage(Style.CHAT_BAR);
		sender.sendMessage(Style.GOLD + "Rank Help");

		for (String[] help : HELP) {
			sender.sendMessage(Style.BLUE + help[0] + Style.GRAY + " - " + Style.RESET + help[1]);
		}

		sender.sendMessage(Style.CHAT_BAR);
	}

}
