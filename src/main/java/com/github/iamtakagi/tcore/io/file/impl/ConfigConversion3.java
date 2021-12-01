package com.github.iamtakagi.tcore.io.file.impl;

import com.github.iamtakagi.tcore.io.file.ConfigConversion;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigConversion3 implements ConfigConversion {

    @Override
    public void convert(File file, FileConfiguration fileConfiguration) {
        fileConfiguration.set("CONFIG_VERSION", 3);
        fileConfiguration.set("CONVERSATION.SEND_MESSAGE", "&7(To &r{5}{3}&7) %MSG%");
        fileConfiguration.set("CONVERSATION.RECEIVE_MESSAGE", "&7(From &r{5}{3}&7) %MSG%");
        fileConfiguration.set("OPTIONS.GLOBAL_CHAT_ENABLED", "&eYou &aenabled &eglobal chat.");
        fileConfiguration.set("OPTIONS.GLOBAL_CHAT_DISABLED", "&eYou &cdisabled &eglobal chat.");
        fileConfiguration.set("OPTIONS.PRIVATE_MESSAGES_ENABLED", "&aYou can now receive new conversations.");
        fileConfiguration.set("OPTIONS.PRIVATE_MESSAGES_DISABLED", "&cYou can no longer receive new conversations. If you start a conversation with a player, they will be able to message you back.");
        fileConfiguration.set("OPTIONS.PRIVATE_MESSAGE_SOUNDS_ENABLED", "&eYou &aenabled &eprivate message sounds.");
        fileConfiguration.set("OPTIONS.PRIVATE_MESSAGE_SOUNDS_DISABLED", "&eYou &cdisabled &eprivate message sounds.");

        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
