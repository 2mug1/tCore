package com.github.iamtakagi.tcore.nametag.command;

import com.github.iamtakagi.tcore.Core;
import com.github.iamtakagi.tcore.profile.Profile;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import com.github.iamtakagi.tcore.nametag.packet.PacketSetSuffix;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "setsuffix", async = true, permission = "tCore.staff.setsuffix")
public class SetSuffixCommand {

    public void execute(CommandSender sender, @CPL("player") Profile profile, String suffix) {
        if(suffix.length() > 16){
            sender.sendMessage("Suffixは16文字以下まで");
            return;
        }

        sender.sendMessage(profile.getUsername() + " のSuffixを設定しました: " + suffix);

        Core.get().getPidgin().sendPacket(new PacketSetSuffix(profile.getUuid(), suffix));
    }
}
