package com.github.iamtakagi.tcore.nametag.command;

import com.github.iamtakagi.tcore.Core;
import com.github.iamtakagi.tcore.nametag.packet.PacketResetPrefix;
import com.github.iamtakagi.tcore.profile.Profile;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "resetprefix", async = true, permission = "tCore.staff.resetprefix")
public class ResetPrefixCommand {

    public void execute(CommandSender sender, @CPL("player") Profile profile) {
        sender.sendMessage(profile.getUsername() + " のPrefixをリセットしました");

        Core.get().getPidgin().sendPacket(new PacketResetPrefix(profile.getUuid()));
    }
}
