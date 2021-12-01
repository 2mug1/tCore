package com.github.iamtakagi.tcore.friend.gui;

import com.github.iamtakagi.tcore.Core;
import com.github.iamtakagi.tcore.profile.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import com.github.iamtakagi.tcore.util.BungeeUtil;
import com.github.iamtakagi.tcore.util.Style;
import com.github.iamtakagi.tcore.cache.RedisPlayerData;
import com.github.iamtakagi.tcore.friend.packet.PacketFriendAccepted;
import com.github.iamtakagi.tcore.friend.packet.PacketFriendDelete;
import com.github.iamtakagi.tcore.friend.packet.PacketFriendSendRequest;
import com.github.iamtakagi.tcore.friend.Friend;
import com.github.iamtakagi.tcore.util.ItemBuilder;
import com.github.iamtakagi.tcore.util.callback.TypeCallback;
import com.github.iamtakagi.tcore.menu.Button;
import com.github.iamtakagi.tcore.menu.Menu;
import com.github.iamtakagi.tcore.menu.button.BackButton;
import com.github.iamtakagi.tcore.menu.menus.ConfirmMenu;
import com.github.iamtakagi.tcore.menu.pagination.PaginatedMenu;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

@AllArgsConstructor
public class FriendListMenu extends PaginatedMenu {

    private Friend friend;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return Style.GOLD + "Friends Online (" + friend.getOnlineFriendsUUID().size() + "/" + friend.getAcceptedPlayersUUID().size() + ")";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        for(int i = 0; i < friend.getAcceptedPlayersUUID().size(); i++){
            UUID uuid = UUID.fromString(friend.getAcceptedPlayersUUID().get(i));
            Profile profile = Profile.getByUuid(uuid);
            RedisPlayerData redisPlayerData = Core.get().getRedisCache().getPlayerData(uuid);
            buttons.put(i, new AcceptedUserButton(profile, redisPlayerData));
        }
        return buttons;
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        //Friend Requesting Button
        buttons.put(2, new RequestingListButton());

        //Send Request Button
        buttons.put(4, new SendRequestButton());

        //Friend Receiving Button
        buttons.put(6, new ReceivingListButton());
        return buttons;
    }

    @AllArgsConstructor
    @Getter
    private class UserButton extends Button {

        private Profile profile;
        private RedisPlayerData redisPlayerData;
        private UserButtonType type;

        @Override
        public ItemStack getButtonItem(Player player) {
            int durability = 1;
            if(redisPlayerData == null){
               durability = 14;
            } else if(redisPlayerData.getLastAction() == RedisPlayerData.LastAction.LEAVING_SERVER){
                durability = 14;
            } else if(redisPlayerData.getLastAction() == RedisPlayerData.LastAction.JOINING_SERVER){
                durability = 5;
            }

            List<String> lore = new ArrayList<>();
            lore.add(Style.MENU_BAR);
            if(redisPlayerData == null){
                lore.add(Style.BLUE + "Status: " + Style.RED + "Offline");
            } else if(redisPlayerData.getLastAction() == RedisPlayerData.LastAction.LEAVING_SERVER){
                lore.add(Style.BLUE + "Status: " + Style.RED + "Offline");
                lore.add(Style.BLUE + "Last Seen at: " + Style.WHITE + redisPlayerData.getLastSeenServer());
                lore.add(Style.BLUE + "Updated: " + Style.WHITE + redisPlayerData.getTimeAgo());
            } else if(redisPlayerData.getLastAction() == RedisPlayerData.LastAction.JOINING_SERVER){
                lore.add(Style.BLUE + "Status: " + Style.GREEN + "Online");
                lore.add(Style.BLUE + "Server: " + Style.WHITE + redisPlayerData.getLastSeenServer());
                lore.add(Style.BLUE + "Updated: " + Style.WHITE + redisPlayerData.getTimeAgo());
            }
            lore.add(Style.MENU_BAR);

            if(type == UserButtonType.ACCEPTED || type == UserButtonType.MUTUAL){
                lore.add(Style.PINK + "Click to view");
            } else if(type == UserButtonType.REQUESTING){
                lore.add(Style.RED + "Click to cancel");
            } else if(type == UserButtonType.RECEIVING) {
                lore.add(Style.GREEN + "Click to accept");
            }

            return new ItemBuilder(Material.STAINED_GLASS_PANE).name(profile.getColoredUsername()).lore(lore).durability((short)durability).build();
        }
    }

    private class AcceptedUserButton extends UserButton {

        private AcceptedUserButton(Profile profile, RedisPlayerData redisPlayerData){
            super(profile, redisPlayerData, UserButtonType.ACCEPTED);
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            new UserMenu(getProfile(), getRedisPlayerData()).openMenu(player);
        }
    }

    private class RequestingUserButton extends UserButton {

        private RequestingUserButton(Profile profile, RedisPlayerData redisPlayerData){
            super(profile, redisPlayerData, UserButtonType.REQUESTING);
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            new ConfirmMenu(Style.GOLD + "Cancel Request?", (TypeCallback<Boolean>) data -> {
                if (data) {
                    getProfile().getFriend().removeFromReceivingPlayers(player.getUniqueId().toString());
                    friend.removeFromRequestingPlayers(getProfile().getUuid().toString());
                    player.sendMessage(Style.GREEN + "You have been confirmed to cancel request of " + getProfile().getColoredUsername());
                    new RequestingListMenu().openMenu(player, 54);
                } else {
                    new RequestingListMenu().openMenu(player, 54);
                }
            },true).openMenu(player);
        }
    }

    private class ReceivingUserButton extends UserButton {
        private ReceivingUserButton(Profile profile, RedisPlayerData redisPlayerData){
            super(profile, redisPlayerData, UserButtonType.RECEIVING);
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            new ConfirmMenu(Style.GOLD + "Accept or Cancel or Reject?", (TypeCallback<Boolean>) data -> {
                if (data) {
                    getProfile().getFriend().removeFromRequestingPlayers(player.getUniqueId().toString());
                    friend.removeFromReceivingPlayers(getProfile().getUuid().toString());
                    getProfile().getFriend().addToAcceptedPlayers(player.getUniqueId().toString());
                    friend.addToAcceptedPlayers(getProfile().getUuid().toString());
                    player.sendMessage(Style.GREEN + "You have been accepted " + getProfile().getColoredUsername() + Style.GREEN + "'s friend request.");
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                    new ReceivingListMenu().openMenu(player, 54);
                    Core.get().getPidgin().sendPacket(new PacketFriendAccepted(getProfile().getUuid().toString(), player.getUniqueId().toString()));
                } else {
                    getProfile().getFriend().removeFromRequestingPlayers(player.getUniqueId().toString());
                    friend.removeFromReceivingPlayers(getProfile().getUuid().toString());
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                    player.sendMessage(Style.RED + "You have been rejected request of " + getProfile().getColoredUsername());
                    new ReceivingListMenu().openMenu(player, 54);
                }
            }, true, "Accept", "Reject", new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return new ItemBuilder(Material.WOOL).durability(4).name(Style.YELLOW + "Cancel").build();
                }

                @Override
                public void clicked(Player player, ClickType clickType) {
                    new ReceivingListMenu().openMenu(player, 54);
                }
            }).openMenu(player);
        }
    }

    private class MutualUserButton extends UserButton {

        private MutualUserButton(Profile profile, RedisPlayerData redisPlayerData){
            super(profile, redisPlayerData, UserButtonType.MUTUAL);
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            new UserMenu(getProfile(), getRedisPlayerData()).openMenu(player);
        }
    }

    @AllArgsConstructor
    private class UserMenu extends Menu {

        private Profile profile;
        private RedisPlayerData redisPlayerData;

        @Override
        public String getTitle(Player player) {
            return profile.getColoredUsername() + Style.BLUE + "'s Info";
        }

        @Override
        public Map<Integer, Button> getButtons(Player player) {
            Map<Integer, Button> buttons = new HashMap<>();
            buttons.put(3, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    ItemBuilder itemBuilder = new ItemBuilder(skull(profile.getUsername()));

                    List<String> lore = new ArrayList<>();
                    lore.add(Style.MENU_BAR);
                    if(redisPlayerData == null){
                        lore.add(Style.BLUE + "Status: " + Style.RED + "Offline");
                    } else if(redisPlayerData.getLastAction() == RedisPlayerData.LastAction.LEAVING_SERVER){
                        lore.add(Style.BLUE + "Status: " + Style.RED + "Offline");
                        lore.add(Style.BLUE + "Last Seen at: " + Style.WHITE + redisPlayerData.getLastSeenServer());
                        lore.add(Style.BLUE + "Updated: " + Style.WHITE + redisPlayerData.getTimeAgo());
                    } else if(redisPlayerData.getLastAction() == RedisPlayerData.LastAction.JOINING_SERVER){
                        lore.add(Style.BLUE + "Status: " + Style.GREEN + "Online");
                        lore.add(Style.BLUE + "Server: " + Style.WHITE + redisPlayerData.getLastSeenServer());
                        lore.add(Style.BLUE + "Updated: " + Style.WHITE + redisPlayerData.getTimeAgo());
                    }
                    lore.add(Style.MENU_BAR);

                    return itemBuilder.name(profile.getColoredUsername()).lore(lore).build();
                }
            });
            buttons.put(5, new BackButton(new FriendListMenu(Profile.getByUuid(player.getUniqueId()).getFriend()), 54));

            buttons.put(19, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    if(redisPlayerData == null || redisPlayerData.getLastAction() == RedisPlayerData.LastAction.LEAVING_SERVER){
                        return new ItemBuilder(Material.FEATHER).name(Style.RED + "This user is offline").build();
                    } else if(redisPlayerData.getLastAction() == RedisPlayerData.LastAction.JOINING_SERVER){
                        return new ItemBuilder(Material.FEATHER).name(Style.YELLOW + "Jump to " + Style.GOLD + redisPlayerData.getLastSeenServer()).build();
                    }
                    return new ItemBuilder(Material.FEATHER).name(Style.RED + "This user is offline").build();
                }
                @Override
                public void clicked(Player player, ClickType clickType) {
                    if(redisPlayerData == null || redisPlayerData.getLastAction() == RedisPlayerData.LastAction.LEAVING_SERVER){
                        player.sendMessage(profile.getColoredUsername() + Style.GRAY + " is offline.");
                    } else if(redisPlayerData.getLastAction() == RedisPlayerData.LastAction.JOINING_SERVER){
                        player.closeInventory();
                        BungeeUtil.connect(player, redisPlayerData.getLastSeenServer());
                    }
                }
            });
            buttons.put(22, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return new ItemBuilder(Material.LEASH).name(Style.YELLOW + Style.BOLD + "Mutual Friends").build();
                }
                @Override
                public void clicked(Player player, ClickType clickType) {
                    new MutualFriendListMenu(profile, redisPlayerData).openMenu(player, 54);
                }
            });
            if(player.getUniqueId().equals(profile.getUuid())) {
                buttons.put(25, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return new ItemBuilder(Material.IRON_FENCE).name(Style.RED + Style.BOLD + "Can't yourself").build();
                    }
                });
            }
            else if(Profile.getByUuid(player.getUniqueId()).getFriend().getAcceptedPlayersUUID().contains(profile.getUuid().toString())) {
                buttons.put(25, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return new ItemBuilder(Material.FIRE).name(Style.DARK_RED + Style.BOLD + "Delete").build();
                    }

                    @Override
                    public void clicked(Player player, ClickType clickType) {
                        new ConfirmMenu(Style.RED + "Delete " + profile.getUsername() + Style.RED + "?", (TypeCallback<Boolean>) data -> {
                            if (data) {
                                friend.removeFromAcceptedPlayers(profile.getUuid().toString());
                                profile.getFriend().removeFromAcceptedPlayers(player.getUniqueId().toString());
                                player.sendMessage(Style.GREEN + "You have been confirmed to delete " + profile.getColoredUsername() + Style.GREEN + " from your friends.");
                                new UserMenu(profile, redisPlayerData).openMenu(player);
                                Core.get().getPidgin().sendPacket(new PacketFriendDelete(profile.getUuid().toString(), player.getUniqueId().toString()));
                            } else {
                                new UserMenu(profile, redisPlayerData).openMenu(player);
                            }
                        }, true, "Delete", "Cancel").openMenu(player);
                    }
                });
            }
            else if(Profile.getByUuid(player.getUniqueId()).getFriend().getRequestingPlayersUUID().contains(profile.getUuid().toString())) {
                buttons.put(25, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return new ItemBuilder(Material.REDSTONE_BLOCK).name(Style.RED + "Cancel Request").build();
                    }
                    @Override
                    public void clicked(Player player, ClickType clickType) {
                        new ConfirmMenu(Style.GOLD + "Cancel Request?", (TypeCallback<Boolean>) data -> {
                            if (data) {
                                profile.getFriend().removeFromReceivingPlayers(player.getUniqueId().toString());
                                friend.removeFromRequestingPlayers(profile.getUuid().toString());
                                player.sendMessage(Style.GREEN + "You have been confirmed to cancel request of " + profile.getColoredUsername());
                                new RequestingListMenu().openMenu(player, 54);
                            } else {
                                new RequestingListMenu().openMenu(player, 54);
                            }
                        }, true, "Confirm", "Cancel").openMenu(player);
                    }
                });
            }
            else if(profile.getFriend().getReceivingPlayersUUID().contains(player.getUniqueId().toString())){
                buttons.put(25, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return new ItemBuilder(Material.PAPER).name(Style.GREEN + "Click to Accept").build();
                    }
                    @Override
                    public void clicked(Player player, ClickType clickType) {
                        new ConfirmMenu(Style.GOLD + "Accept or Cancel or Reject?", (TypeCallback<Boolean>) data -> {
                            if (data) {
                                profile.getFriend().removeFromRequestingPlayers(player.getUniqueId().toString());
                                friend.removeFromReceivingPlayers(profile.getUuid().toString());
                                profile.getFriend().addToAcceptedPlayers(player.getUniqueId().toString());
                                friend.addToAcceptedPlayers(profile.getUuid().toString());
                                player.sendMessage(Style.GREEN + "You have been accepted " + profile.getColoredUsername() + Style.GREEN + "'s friend request.");
                                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                                new ReceivingListMenu().openMenu(player, 54);
                                Core.get().getPidgin().sendPacket(new PacketFriendAccepted(profile.getUuid().toString(), player.getUniqueId().toString()));
                            } else {
                                player.sendMessage(Style.RED + "You have been rejected request of " + profile.getColoredUsername());
                                new ReceivingListMenu().openMenu(player, 54);
                            }
                        }, true, "Accept", "Reject", new Button() {
                            @Override
                            public ItemStack getButtonItem(Player player) {
                                return new ItemBuilder(Material.WOOL).durability(4).name(Style.YELLOW + "Cancel").build();
                            }

                            @Override
                            public void clicked(Player player, ClickType clickType) {
                                new ReceivingListMenu().openMenu(player, 54);
                            }
                        }).openMenu(player);
                    }
                });
            }else{
                buttons.put(25, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return new ItemBuilder(Material.NAME_TAG).name(Style.GREEN + Style.BOLD + "Send Request").build();
                    }

                    @Override
                    public void clicked(Player player, ClickType clickType) {
                        new ConfirmMenu(Style.BLUE + "Send to " + profile.getColoredUsername() + Style.RED + "?", (TypeCallback<Boolean>) data -> {
                            if (data) {
                                friend.addToRequestingPlayers(profile.getUuid().toString());
                                profile.getFriend().addToReceivingPlayers(player.getUniqueId().toString());
                                player.sendMessage(Style.GREEN + "You have been sent request to " + profile.getColoredUsername());
                                new UserMenu(profile, redisPlayerData).openMenu(player);
                                Core.get().getPidgin().sendPacket(new PacketFriendSendRequest(profile.getUuid().toString(), player.getUniqueId().toString()));
                            } else {
                                new UserMenu(profile, redisPlayerData).openMenu(player);
                            }
                        }, true, "Send", "Cancel").openMenu(player);
                    }
                });
            }
            return buttons;
        }
    }

    @AllArgsConstructor
    private class MutualFriendListMenu extends PaginatedMenu{

        private Profile profile;
        private RedisPlayerData redisPlayerData;

        @Override
        public String getPrePaginatedTitle(Player player) {
            return Style.GOLD + profile.getUsername() + "'s Mutual Friends";
        }

        @Override
        public Map<Integer, Button> getAllPagesButtons(Player player) {
            Map<Integer, Button> buttons = new HashMap<>();
            List<String> mutual = profile.getFriend().getMutualFriendsUUID();
            for(int i = 0; i < mutual.size(); i++){
                UUID uuid = UUID.fromString(mutual.get(i));
                Profile profile = Profile.getByUuid(uuid);
                RedisPlayerData redisPlayerData = Core.get().getRedisCache().getPlayerData(uuid);
                buttons.put(i, new MutualUserButton(profile, redisPlayerData));
            }
            return buttons;
        }

        @Override
        public Map<Integer, Button> getGlobalButtons(Player player) {
            Map<Integer, Button> buttons = new HashMap<>();
            buttons.put(4, new BackButton(new UserMenu(profile, redisPlayerData)));
            return buttons;
        }
    }

    private class SendRequestButton extends Button {

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.NAME_TAG).name(Style.AQUA + "/f <username>").lore(Style.GRAY + "Add Friend").build();
        }
    }

    private class RequestingListButton extends Button {

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.CHEST).name(Style.BLUE + "Requesting Players").lore(Style.GRAY + "Click to view", Style.GOLD + "Requesting: " + Style.GOLD + friend.getRequestingPlayersUUID().size()).build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            new RequestingListMenu().openMenu(player, 54);
        }
    }

    private class ReceivingListButton extends Button{

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.CHEST).name(Style.BLUE + "Receiving Players").lore(Style.GRAY + "Click to view", Style.GOLD + "Receiving: " + Style.GOLD + friend.getReceivingPlayersUUID().size()).build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            new ReceivingListMenu().openMenu(player, 54);
        }
    }

    private class RequestingListMenu extends PaginatedMenu{

        @Override
        public String getPrePaginatedTitle(Player player) {
            return Style.GOLD + "Requesting Players (" + friend.getRequestingPlayersUUID().size() + ")";
        }

        @Override
        public Map<Integer, Button> getAllPagesButtons(Player player) {
            Map<Integer, Button> buttons = new HashMap<>();
            for(int i = 0; i < friend.getRequestingPlayersUUID().size(); i++){
                UUID uuid = UUID.fromString(friend.getRequestingPlayersUUID().get(i));
                Profile profile = Profile.getByUuid(uuid);
                RedisPlayerData redisPlayerData = Core.get().getRedisCache().getPlayerData(uuid);
                buttons.put(i, new RequestingUserButton(profile, redisPlayerData));
            }
            return buttons;
        }

        @Override
        public Map<Integer, Button> getGlobalButtons(Player player) {
            Map<Integer, Button> buttons = new HashMap<>();
            buttons.put(4, new BackButton(new FriendListMenu(friend), 54));
            return buttons;
        }
    }

    private class ReceivingListMenu extends PaginatedMenu{

        @Override
        public String getPrePaginatedTitle(Player player) {
            return Style.GOLD + "Receiving Players (" + friend.getReceivingPlayersUUID().size() + ")";
        }

        @Override
        public Map<Integer, Button> getAllPagesButtons(Player player) {
            Map<Integer, Button> buttons = new HashMap<>();
            for(int i = 0; i < friend.getReceivingPlayersUUID().size(); i++){
                UUID uuid = UUID.fromString(friend.getReceivingPlayersUUID().get(i));
                Profile profile = Profile.getByUuid(uuid);
                RedisPlayerData redisPlayerData = Core.get().getRedisCache().getPlayerData(uuid);
                buttons.put(i, new ReceivingUserButton(profile, redisPlayerData));
            }
            return buttons;
        }

        @Override
        public Map<Integer, Button> getGlobalButtons(Player player) {
            Map<Integer, Button> buttons = new HashMap<>();
            buttons.put(4, new BackButton(new FriendListMenu(friend), 54));
            return buttons;
        }
    }

    private enum UserButtonType {
        REQUESTING, RECEIVING, ACCEPTED, MUTUAL
    }

    private static ItemStack skull(String name){
        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setOwner(name);
        itemStack.setItemMeta(skullMeta);
        return itemStack;
    }
}
