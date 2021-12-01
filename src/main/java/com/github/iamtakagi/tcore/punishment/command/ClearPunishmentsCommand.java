package com.github.iamtakagi.tcore.punishment.command;

import com.github.iamtakagi.tcore.Core;
import com.github.iamtakagi.tcore.Locale;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import com.github.iamtakagi.tcore.profile.Profile;
import com.github.iamtakagi.tcore.punishment.packet.PacketClearPunishments;
import com.github.iamtakagi.tcore.util.Style;
import org.bukkit.command.CommandSender;

@CommandMeta(label = {"clearpunishments", "cp"}, permission = "tCore.staff.clearpunishments")
public class ClearPunishmentsCommand {

    public void execute(CommandSender sender, @CPL("player") Profile profile) {
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        profile.getPunishments().clear();
        profile.save();

        Core.get().getPidgin().sendPacket(new PacketClearPunishments(profile.getUuid()));

        sender.sendMessage(profile.getActiveRank().getColor() +  profile.getUsername() + Style.GREEN + "'s punishment history has been all removed.");
    }
}
