package com.github.iamtakagi.tcore.nametag.command;

import com.github.iamtakagi.tcore.Core;
import com.github.iamtakagi.tcore.nametag.packet.PacketAddPrefix;
import com.github.iamtakagi.tcore.profile.Profile;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "addprefix", async = true, permission = "tCore.staff.addprefix")
public class AddPrefixCommand {

    public void execute(CommandSender sender, @CPL("player") Profile profile, String prefix) {
        if((profile.getPrefix() + prefix).length() > 16){
            sender.sendMessage("Prefixは16文字以下まで");
            return;
        }

        sender.sendMessage(profile.getUsername() + " のPrefixを追加しました: " + prefix);

        Core.get().getPidgin().sendPacket(new PacketAddPrefix(profile.getUuid(), prefix));
    }
}
