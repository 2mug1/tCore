package com.github.iamtakagi.tcore.clan.command;

import com.github.iamtakagi.tcore.Core;
import com.github.iamtakagi.tcore.clan.packet.PacketClanInvite;
import com.github.iamtakagi.tcore.menu.menus.ConfirmMenu;
import com.github.iamtakagi.tcore.profile.Profile;
import com.github.iamtakagi.tcore.util.Style;
import com.github.iamtakagi.tcore.util.TimeUtil;
import com.github.iamtakagi.tcore.util.callback.TypeCallback;
import com.qrakn.honcho.command.CommandMeta;
import com.github.iamtakagi.tcore.clan.Clan;
import com.github.iamtakagi.tcore.clan.ClanPlayer;
import com.github.iamtakagi.tcore.clan.ClanPlayerProcedureStage;
import com.github.iamtakagi.tcore.clan.ClanPlayerRole;
import org.bukkit.entity.Player;

import java.util.Date;

@CommandMeta(label = { "clan invite" }, async = true)
public class ClanInviteCommand {

    public void execute(Player player, String username){
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

        if(!clan.getClanPlayerByUuid(player.getUniqueId()).isMoreThanLeader()){
            player.sendMessage(Style.RED + "You are not owner or leader.");
            return;
        }

        Profile other = Profile.getByUsername(username);

        if(other == null){
            player.sendMessage(Style.GRAY + "Has " + username + " joined network once?");
            return;
        }

        if(clan.isInviting(other.getUuid())){
            player.sendMessage(Style.RED + other.getUsername() + " has already been invited.");
            return;
        }

        if(clan.isMember(other.getUuid())){
            player.sendMessage(Style.RED + other.getUsername() + " has already in your clan.");
            return;
        }

        new ConfirmMenu(Style.BLUE + "Invite " + other.getUsername(), (TypeCallback<Boolean>) data -> {
            if(data){
                clan.getPlayers().add(new ClanPlayer(other.getUuid(), other.getUsername(), ClanPlayerRole.Regular, ClanPlayerProcedureStage.INVITING));
                clan.save();
                player.sendMessage(Style.GREEN + "You have been invited " + username + " to your clan.");

                Core.get().getPidgin().sendPacket(new PacketClanInvite(clan, clan.getClanPlayerByUuid(other.getUuid())));
            }
        }, true, "Invite", "Cancel").openMenu(player);
    }
}
