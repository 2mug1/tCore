package com.github.iamtakagi.tcore.profile.conversation.command;

import com.github.iamtakagi.tcore.profile.Profile;
import com.github.iamtakagi.tcore.profile.conversation.Conversation;
import com.github.iamtakagi.tcore.util.Style;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = { "message", "msg", "whisper", "tell", "t" })
public class MessageCommand {

    public void execute(Player player, Player target, String message) {
        if (player.equals(target)) {
            player.sendMessage(Style.RED + "You cannot message yourself!");
            return;
        }

        if (target == null) {
            player.sendMessage(Style.RED + "A player with that name could not be found.");
            return;
        }

        Profile playerProfile = Profile.getByUuid(player.getUniqueId());
        Profile targetProfile = Profile.getByUuid(target.getUniqueId());

        if (targetProfile.getConversations().canBeMessagedBy(player)) {
            Conversation conversation = playerProfile.getConversations().getOrCreateConversation(target);

            if (conversation.validate()) {
                conversation.sendMessage(player, target, message);
            } else {
                player.sendMessage(Style.RED + "That player is not receiving new conversations right now.");
            }
        } else {
            player.sendMessage(Style.RED + "That player is not receiving new conversations right now.");
        }
    }

}
