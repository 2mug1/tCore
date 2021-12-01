package com.github.iamtakagi.tcore.convenient.command;

import com.github.iamtakagi.tcore.Core;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "tCore debug", permission = "tCore.admin")
public class SpicaDebugCommand {

	public void execute(CommandSender sender) {
		Core.get().setDebug(!Core.get().isDebug());
		sender.sendMessage("Debug: " + Core.get().isDebug());
	}

}
