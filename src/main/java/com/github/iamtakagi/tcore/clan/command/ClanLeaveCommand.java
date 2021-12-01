package com.github.iamtakagi.tcore.clan.command;

import com.github.iamtakagi.tcore.Core;
import com.github.iamtakagi.tcore.clan.packet.PacketClanLeave;
import com.github.iamtakagi.tcore.menu.menus.ConfirmMenu;
import com.github.iamtakagi.tcore.profile.Profile;
import com.github.iamtakagi.tcore.util.Style;
import com.github.iamtakagi.tcore.util.TimeUtil;
import com.github.iamtakagi.tcore.util.callback.TypeCallback;
import com.qrakn.honcho.command.CommandMeta;
import com.github.iamtakagi.tcore.clan.Clan;
import org.bukkit.entity.Player;

import java.util.Date;

@CommandMeta(label = { "clan leave" }, async = true)
public class ClanLeaveCommand {

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
            player.sendMessage(Style.RED + "Your clan did disband at " + TimeUtil.dateToString(new Date(clan.getDisbandedAt()), null));
            return;
        }

        if(clan.getClanPlayerByUuid(player.getUniqueId()).isOwner()){
            player.sendMessage(Style.RED + "You are owner. Can't it.");
            return;
        }

        new ConfirmMenu(Style.BLUE + "Leave Clan", (TypeCallback<Boolean>) data -> {
            if(data){
                clan.getPlayers().remove(clan.getClanPlayerByUuid(player.getUniqueId()));
                clan.save();

                player.sendMessage(Style.GREEN + "You have left from clan.");

                Core.get().getPidgin().sendPacket(new PacketClanLeave(clan, player.getName()));
            }
        }, true, "Confirm", "Cancel").openMenu(player);
    }
}
