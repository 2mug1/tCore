package com.github.iamtakagi.tcore.nametag.command;

import com.github.iamtakagi.tcore.Core;
import com.github.iamtakagi.tcore.profile.Profile;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import com.github.iamtakagi.tcore.nametag.packet.PacketResetSuffix;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "resetsuffix", async = true, permission = "tCore.staff.resetsuffix")
public class ResetSuffixCommand {

    public void execute(CommandSender sender, @CPL("player") Profile profile) {
        sender.sendMessage(profile.getUsername() + " のSuffixをリセットしました");

        Core.get().getPidgin().sendPacket(new PacketResetSuffix(profile.getUuid()));
    }
}
