package com.github.iamtakagi.tcore.nametag.command;

import com.github.iamtakagi.tcore.Core;
import com.github.iamtakagi.tcore.nametag.packet.PacketSetPrefix;
import com.github.iamtakagi.tcore.profile.Profile;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "setprefix", async = true, permission = "tCore.staff.setprefix")
public class SetPrefixCommand {

    public void execute(CommandSender sender, @CPL("player") Profile profile, String prefix) {
        if(prefix.length() > 16){
            sender.sendMessage("Prefixは16文字以下まで");
            return;
        }

        sender.sendMessage(profile.getUsername() + " のPrefixを設定しました: " + prefix);

        Core.get().getPidgin().sendPacket(new PacketSetPrefix(profile.getUuid(), prefix));
    }
}
