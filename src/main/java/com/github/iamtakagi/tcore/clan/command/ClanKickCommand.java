package com.github.iamtakagi.tcore.clan.command;


import com.github.iamtakagi.tcore.Core;
import com.github.iamtakagi.tcore.clan.packet.PacketClanKick;
import com.github.iamtakagi.tcore.menu.menus.ConfirmMenu;
import com.github.iamtakagi.tcore.profile.Profile;
import com.github.iamtakagi.tcore.util.Style;
import com.github.iamtakagi.tcore.util.TimeUtil;
import com.github.iamtakagi.tcore.util.callback.TypeCallback;
import com.qrakn.honcho.command.CommandMeta;
import com.github.iamtakagi.tcore.clan.Clan;
import org.bukkit.entity.Player;

import java.util.Date;

@CommandMeta(label = { "clan kick" }, async = true)
public class ClanKickCommand {

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
            player.sendMessage(Style.RED + "You are not leader.");
            return;
        }

        Profile other = Profile.getByUsername(username);

        if(other == null){
            player.sendMessage(Style.GRAY + "Has " + username + " joined network once?");
            return;
        }

        if(!clan.isMember(other.getUuid())){
            player.sendMessage(Style.RED + other.getUsername() + " isn't member.");
            return;
        }

        if(player.getName().equalsIgnoreCase(username)){
            player.sendMessage(Style.RED + "You can't it yourself.");
            return;
        }

        new ConfirmMenu(Style.BLUE + "Kick " + other.getUsername(), (TypeCallback<Boolean>) data -> {
            if(data){
                clan.getPlayers().remove(clan.getClanPlayerByUuid(other.getUuid()));
                clan.save();
                player.sendMessage(Style.GREEN + "You have kicked " + username + " from your clan.");
                Core.get().getPidgin().sendPacket(new PacketClanKick(clan, other.getUuid(), other.getUsername()));
            }
        }, true, "Kick", "Cancel").openMenu(player);
    }
}
