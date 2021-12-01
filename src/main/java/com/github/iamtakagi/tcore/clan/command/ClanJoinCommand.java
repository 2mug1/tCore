package com.github.iamtakagi.tcore.clan.command;

import com.github.iamtakagi.tcore.Core;
import com.github.iamtakagi.tcore.clan.packet.PacketClanJoin;
import com.github.iamtakagi.tcore.menu.Button;
import com.github.iamtakagi.tcore.menu.menus.ConfirmMenu;
import com.github.iamtakagi.tcore.profile.Profile;
import com.github.iamtakagi.tcore.util.ItemBuilder;
import com.github.iamtakagi.tcore.util.Style;
import com.github.iamtakagi.tcore.util.callback.TypeCallback;
import com.qrakn.honcho.command.CommandMeta;
import com.github.iamtakagi.tcore.clan.Clan;
import com.github.iamtakagi.tcore.clan.ClanPlayerProcedureStage;
import com.github.iamtakagi.tcore.clan.packet.PacketClanInviteDenied;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@CommandMeta(label = { "clan join" }, async = true)
public class ClanJoinCommand {

    public void execute(Player player, String clanName){
        Profile profile = Profile.getByUuid(player.getUniqueId());

        if(profile.isInClan()){
            player.sendMessage(Style.RED + "You've already in the clan.");
            return;
        }

        Clan clan = Clan.getByName(clanName);

        if(clan == null){
            player.sendMessage(Style.RED + "That name's not found.");
            return;
        }

        if(!clan.isLoaded()){
            player.sendMessage(Style.RED + "Clan data hasn't loaded yet.");
            return;
        }

        if(!clan.isInviting(player.getUniqueId())){
            player.sendMessage(Style.RED + "You're not invited from " + clan.getName());
            return;
        }

        new ConfirmMenu(Style.BLUE + "Join " + clan.getName(), (TypeCallback<Boolean>) data -> {
            if (data) {
                clan.getClanPlayerByUuid(player.getUniqueId()).setProcedureStage(ClanPlayerProcedureStage.ACCEPTED);
                clan.save();
                profile.setClanName(clanName);
                profile.save();
                player.sendMessage(Style.GREEN + "You've joined " + clan.getName());
                Core.get().getPidgin().sendPacket(new PacketClanJoin(clan, clan.getClanPlayerByUuid(player.getUniqueId())));
            }else{
                clan.getPlayers().remove(clan.getClanPlayerByUuid(player.getUniqueId()));
                clan.save();
                Core.get().getPidgin().sendPacket(new PacketClanInviteDenied(clan, player.getName()));
            }
        }, true, "Join", "Deny", new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.WOOL).durability(4).name(Style.YELLOW + "Ignore").build();
            }
            @Override
            public void clicked(Player player, ClickType clickType) {
                player.closeInventory();
            }
        }).openMenu(player);

    }
}
