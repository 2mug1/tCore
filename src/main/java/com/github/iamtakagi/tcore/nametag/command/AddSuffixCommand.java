package com.github.iamtakagi.tcore.nametag.command;

import com.github.iamtakagi.tcore.Core;
import com.github.iamtakagi.tcore.profile.Profile;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import com.github.iamtakagi.tcore.nametag.packet.PacketAddSuffix;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "addsuffix", async = true, permission = ".staff.addsuffix")
public class AddSuffixCommand {

    public void execute(CommandSender sender, @CPL("player") Profile profile, String suffix) {
        if((profile.getSuffix() + suffix).length() > 16){
            sender.sendMessage("Suffixは16文字以下まで");
            return;
        }

        sender.sendMessage(profile.getUsername() + " のSuffixを追加しました: " + suffix);

        Core.get().getPidgin().sendPacket(new PacketAddSuffix(profile.getUuid(), suffix));
    }
}
