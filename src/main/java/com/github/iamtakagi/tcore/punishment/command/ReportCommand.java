package com.github.iamtakagi.tcore.punishment.command;

import com.github.iamtakagi.tcore.Core;
import com.qrakn.honcho.command.CommandMeta;
import com.github.iamtakagi.tcore.staff.packet.PacketReportPlayer;
import com.github.iamtakagi.tcore.profile.Profile;
import com.github.iamtakagi.tcore.util.Style;
import org.bukkit.entity.Player;

@CommandMeta(label = "report", async = true)
public class ReportCommand {

    public void execute(Player player, String target, String reason){
        Profile profile = Profile.getByUuid(player.getUniqueId());
        if(profile.canReport()){
            profile.setLastReported(System.currentTimeMillis());
            profile.save();
            Core.get().getPidgin().sendPacket(
                    new PacketReportPlayer(target, player.getName(), Core.get().getMainConfig().getString("SERVER_NAME"), reason, System.currentTimeMillis())
            );
            player.sendMessage(Style.GREEN + "Report has been sent to Staff / レポートはスタッフに送信されました");
        }else{
            player.sendMessage(Style.RED + "You must wait for 180 seconds to sent report / レポートは180秒に一度送信可能です");
        }
    }
}
