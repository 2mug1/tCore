package com.github.iamtakagi.tcore.profile;

import lombok.Getter;
import lombok.Setter;
import com.github.iamtakagi.tcore.util.Style;

public class Economy {

    private Profile profile;

    @Getter
    @Setter
    private int coins;

    public Economy(Profile profile) {
        this.profile = profile;
    }

    private void updateCoin(int to){
        this.coins = to;
        profile.save();
    }

    public boolean addCoin(int amount){
        if(amount <= 0) return false;

        int calc = coins+amount;

        /* 上限オーバーだったら */
        if(amount >= Integer.MAX_VALUE){
            calc = Integer.MAX_VALUE;
        }

        updateCoin(calc);

        profile.getPlayer().sendMessage(Style.YELLOW + " You've gained " + Style.GREEN + "+" + amount + " coins");
        return true;
    }

    public boolean removeCoin(int amount){
        if(amount <= 0) return false;

        int calc = coins-amount;

        /* マイナスだったら */
        if(calc < 0){
            calc = 0;
        }

        updateCoin(calc);

        profile.getPlayer().sendMessage(Style.YELLOW + " You've consumed " + Style.RED + "-" + amount + " coins");
        return true;
    }

}
