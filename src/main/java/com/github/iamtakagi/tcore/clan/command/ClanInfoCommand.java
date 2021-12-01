package com.github.iamtakagi.tcore.clan.command;

import com.github.iamtakagi.tcore.profile.Profile;
import com.github.iamtakagi.tcore.util.Style;
import com.github.iamtakagi.tcore.util.TimeUtil;
import com.qrakn.honcho.command.CommandMeta;
import com.github.iamtakagi.tcore.clan.Clan;
import com.github.iamtakagi.tcore.clan.ClanPlayer;
import com.github.iamtakagi.tcore.clan.ClanPlayerProcedureStage;
import org.bukkit.entity.Player;

import java.util.Date;

@CommandMeta(label = { "clan info" }, async = true)
public class ClanInfoCommand {

    public void execute(Player player){
        Profile profile = Profile.getByUuid(player.getUniqueId());

        if(!profile.isInClan()){
            player.sendMessage(Style.RED + "You're not in a clan.");
            return;
        }

        Clan clan = profile.getClan();

        if(!clan.isLoaded()){
            player.sendMessage(Style.RED + "Clan data hasn't loaded yet.");
            return;
        }

        if(clan.isDisbanded()){
            player.sendMessage(Style.RED + "Your clan has already disbanded at " + TimeUtil.dateToString(new Date(clan.getDisbandedAt()), null));
            return;
        }

        clan.sortPlayersByRole();

        player.sendMessage(Style.HORIZONTAL_SEPARATOR);
        player.sendMessage("");
        player.sendMessage(Style.YELLOW + Style.BOLD + " Clan Information");
        player.sendMessage(Style.GOLD + " Name: " + Style.WHITE + clan.getName());
        player.sendMessage(Style.GOLD + " Tag: " + Style.WHITE + clan.getTag());
        player.sendMessage("");
        player.sendMessage(Style.GOLD + " Members");
        for(ClanPlayer clanPlayer : clan.getPlayers()){
            if(clanPlayer.getProcedureStage() == ClanPlayerProcedureStage.ACCEPTED) {
                player.sendMessage(Style.RESET + " - " + clanPlayer.getName() + " (" + clanPlayer.getRole().getColor() + clanPlayer.getRole().name() + Style.RESET + ")");
            }
        }

        if(clan.getInvitingPlayers().size() >= 1) {
            player.sendMessage("");
            player.sendMessage(Style.GOLD + " Inviting");
            for (ClanPlayer clanPlayer : clan.getInvitingPlayers()) {
                player.sendMessage(Style.RESET + " - " + clanPlayer.getName() + " (" + clanPlayer.getRole().getColor() + clanPlayer.getRole().name() + Style.RESET + ")");
            }
        }
        player.sendMessage("");
        player.sendMessage(Style.GRAY + " Created at: " + Style.WHITE + TimeUtil.dateToString(new Date(clan.getCreatedAt()), null));
        player.sendMessage("");
        player.sendMessage(Style.HORIZONTAL_SEPARATOR);
    }
}
