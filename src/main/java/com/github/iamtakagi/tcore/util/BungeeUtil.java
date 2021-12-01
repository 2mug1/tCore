package com.github.iamtakagi.tcore.util;

import com.github.iamtakagi.tcore.Core;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;

public class BungeeUtil {

    public static void connect(Player player, String serverName) {
        player.sendMessage(Style.AQUA + "Connecting to " + serverName + "...");
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Connect");
        output.writeUTF(serverName);
        player.sendPluginMessage(Core.get(), "BungeeCord", output.toByteArray());
    }
}
