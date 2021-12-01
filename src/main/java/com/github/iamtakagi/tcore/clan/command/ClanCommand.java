package com.github.iamtakagi.tcore.clan.command;

import com.github.iamtakagi.tcore.util.Style;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = { "clan" })
public class ClanCommand {

    private static final String[] HELP = new String[]{
            "/clan info",
            "/clan leave",
            "/clan invite <name>",
            "/clan join <name>",
            "/clan chat <message>",
            "/clan kick <username>",
            "/clan tag <tag>",
            "/clan disband <reason>",
            "/clan create <name> <tag>",
            "/clan role <username> <Regular/Leader>"
    };

    public void execute(Player player){
        player.sendMessage(Style.HORIZONTAL_SEPARATOR);
        player.sendMessage("");
        for (String help : HELP) {
            player.sendMessage(" " + Style.YELLOW + help);
        }
        player.sendMessage("");
        player.sendMessage(Style.HORIZONTAL_SEPARATOR);
    }
}
