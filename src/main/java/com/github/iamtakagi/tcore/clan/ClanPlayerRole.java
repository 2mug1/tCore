package com.github.iamtakagi.tcore.clan;

import com.github.iamtakagi.tcore.util.Style;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ClanPlayerRole {

    Owner(Style.DARK_RED, 2),
    Leader(Style.PINK, 1),
    Regular(Style.GREEN, 0);


    private String color;
    private int weight;

}
