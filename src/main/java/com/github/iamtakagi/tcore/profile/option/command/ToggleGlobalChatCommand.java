package com.github.iamtakagi.tcore.profile.option.command;

import com.github.iamtakagi.tcore.Locale;
import com.github.iamtakagi.tcore.profile.Profile;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = { "toggleglobalchat", "tgc", "togglepublicchat", "tpc" })
public class ToggleGlobalChatCommand {

    public void execute(Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        profile.getOptions().publicChatEnabled(!profile.getOptions().publicChatEnabled());

        if (profile.getOptions().publicChatEnabled()) {
            player.sendMessage(Locale.OPTIONS_GLOBAL_CHAT_ENABLED.format());
        } else {
            player.sendMessage(Locale.OPTIONS_GLOBAL_CHAT_DISABLED.format());
        }
    }

}
