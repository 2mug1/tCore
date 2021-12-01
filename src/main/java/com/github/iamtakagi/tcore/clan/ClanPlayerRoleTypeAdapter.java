package com.github.iamtakagi.tcore.clan;

import com.qrakn.honcho.command.adapter.CommandTypeAdapter;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ClanPlayerRoleTypeAdapter implements CommandTypeAdapter {

    @Override
    public <T> T convert(String string, Class<T> type) {
        return type.cast(ClanPlayerRole.valueOf(string));
    }

    @Override
    public boolean onException(Exception exception, CommandSender sender, String input) {
        return false;
    }

    @Override
    public List<String> onTabComplete(String string) {
        List<String> completed = new ArrayList<>();

        for (ClanPlayerRole role : ClanPlayerRole.values()) {
            if (role.name().toLowerCase().startsWith(string)) {
                completed.add(role.name());
            }
        }

        return completed;
    }
}

