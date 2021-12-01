package com.github.iamtakagi.tcore.profile.experience;

import lombok.Getter;
import lombok.Setter;
import com.github.iamtakagi.tcore.profile.Profile;
import com.github.iamtakagi.tcore.util.Style;
import org.bukkit.Sound;

import java.util.HashMap;
import java.util.Map;

public class Experience {

    private static final int MAX_LEVEL = 100;

    /* LEVEL : EXP で格納 */
    private static final Map LEVELING_MAP = new HashMap<Integer, Integer>() {
        {
            put(1, 200);
            put(2, 225);
            put(3, 250);
            put(4, 275);
            put(5, 300);
            put(6, 700);
            put(7, 800);
            put(8, 900);
            put(9, 1000);
            put(10, 1100);

            put(11, 1300);
            put(12, 1400);
            put(13, 1500);
            put(14, 1600);
            put(15, 1700);
            put(16, 1800);
            put(17, 1900);
            put(18, 2000);
            put(19, 2100);
            put(20, 2200);

            put(21, 2300);
            put(22, 2400);
            put(23, 2500);
            put(24, 2600);
            put(25, 2700);
            put(26, 2800);
            put(27, 2900);
            put(28, 3000);
            put(29, 3100);
            put(30, 3200);

            put(31, 3300);
            put(32, 3400);
            put(33, 3500);
            put(34, 3600);
            put(35, 3700);
            put(36, 3800);
            put(37, 3900);
            put(38, 4000);
            put(39, 4100);
            put(40, 4200);

            put(41, 4300);
            put(42, 4400);
            put(43, 4500);
            put(44, 4600);
            put(45, 4700);
            put(46, 4800);
            put(47, 4900);
            put(48, 5000);
            put(49, 5100);
            put(50, 5200);

            put(51, 4300);
            put(52, 4400);
            put(53, 4500);
            put(54, 4600);
            put(55, 4700);
            put(56, 4800);
            put(57, 4900);
            put(58, 5000);
            put(59, 5100);
            put(60, 5200);

            put(61, 5300);
            put(62, 5400);
            put(63, 5500);
            put(64, 5600);
            put(65, 5700);
            put(66, 5800);
            put(67, 5900);
            put(68, 6000);
            put(69, 6100);
            put(70, 6200);

            put(71, 6300);
            put(72, 6400);
            put(73, 6500);
            put(74, 6600);
            put(75, 6700);
            put(76, 6800);
            put(77, 6900);
            put(78, 7000);
            put(79, 7100);
            put(80, 7200);

            put(81, 7300);
            put(82, 7400);
            put(83, 7500);
            put(84, 7600);
            put(85, 7700);
            put(86, 7800);
            put(87, 7900);
            put(88, 8000);
            put(89, 8100);
            put(90, 8500);

            put(91, 9000);
            put(92, 9100);
            put(93, 9200);
            put(94, 9300);
            put(95, 9400);
            put(96, 10000);
            put(97, 13000);
            put(98, 15000);
            put(99, 16000);
            put(100, 17000);
        }
    };

    private Profile profile;

    @Getter
    @Setter
    private int level;
    @Getter
    @Setter
    private int exp;
    @Getter
    @Setter
    private int totalExp;

    public Experience(Profile profile) {
        this.profile = profile;
    }

    public boolean isMax() {
        return level == MAX_LEVEL;
    }

    public int getNextLevel() {
        if (isMax()) {
            return -1;
        }
        return level + 1;
    }

    public int needNextLevelExp() {
        return (int) LEVELING_MAP.get(getNextLevel());
    }

    private void updateLevel(int to) {
        this.level = to;
        profile.save();
    }

    public void addExp(int amount) {
        setExp(exp + amount);
        setTotalExp(totalExp + amount);

        profile.getPlayer().sendMessage(Style.YELLOW + " You've gained " + Style.AQUA + "+" + amount + " exp");

        boolean levelup = false;

        final int before = level;

        int remaining = exp - needNextLevelExp();
        if (remaining >= 0) {
            setExp(remaining);
            updateLevel(getNextLevel());
            levelup = true;
        }

        while (needNextLevelExp() <= exp) {
            remaining = exp - needNextLevelExp();
            if(remaining >= 0) {
                level++;
                setExp(remaining);
                levelup = true;
            }
        }

        profile.save();

        if(levelup){
            profile.getPlayer().sendMessage(Style.YELLOW + Style.BOLD + " LEVEL UP! " + Style.AQUA + before + "→" + level);
            profile.getPlayer().playSound(profile.getPlayer().getLocation(), Sound.LEVEL_UP, 0.5F, 1);
        }
    }
}