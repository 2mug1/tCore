package com.github.iamtakagi.tcore.convenient.command;

import com.github.iamtakagi.tcore.Api;
import com.github.iamtakagi.tcore.util.BukkitReflection;
import com.github.iamtakagi.tcore.util.Style;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = "ping")
public class PingCommand {

    public void execute(Player player) {
        player.sendMessage(Style.YELLOW + "Your Ping: " + colorPing(BukkitReflection.getPing(player)));
    }

    public void execute(Player player, Player target) {
        if (target == null) {
            player.sendMessage(Style.RED + "A player with that name could not be found.");
        } else {
            player.sendMessage(Api.getColoredName(target) + Style.YELLOW + "'s Ping: " +
                    colorPing(BukkitReflection.getPing(target)));
        }
    }
    private String colorPing(int ping) {


        if (ping <= 40) {
            return Style.GREEN + ping;
        } else if (ping <= 70) {
            return Style.YELLOW + ping;
        } else if (ping <= 100) {
            return Style.GOLD + ping;
        } else {
            return Style.RED + ping;
        }
    }

}
