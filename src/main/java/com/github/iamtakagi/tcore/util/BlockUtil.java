package com.github.iamtakagi.tcore.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class BlockUtil {

    public static List<Block> getBlocksByType(Location loc1, Location loc2, World w, Material type) {

        //First of all, we create the list:
        List<Block> blocks = new ArrayList<>();

        //Next we will name each coordinate
        int x1 = loc1.getBlockX();
        int y1 = loc1.getBlockY();
        int z1 = loc1.getBlockZ();

        int x2 = loc2.getBlockX();
        int y2 = loc2.getBlockY();
        int z2 = loc2.getBlockZ();

        //Then we create the following integers
        int xMin, yMin, zMin;
        int xMax, yMax, zMax;
        int x, y, z;

        //Now we need to make sure xMin is always lower then xMax
        if (x1 > x2) { //If x1 is a higher number then x2
            xMin = x2;
            xMax = x1;
        } else {
            xMin = x1;
            xMax = x2;
        }

        //Same with Y
        if (y1 > y2) {
            yMin = y2;
            yMax = y1;
        } else {
            yMin = y1;
            yMax = y2;
        }

        //And Z
        if (z1 > z2) {
            zMin = z2;
            zMax = z1;
        } else {
            zMin = z1;
            zMax = z2;
        }

        //Now it's time for the loop
        for (x = xMin; x <= xMax; x++) {
            for (y = yMin; y <= yMax; y++) {
                for (z = zMin; z <= zMax; z++) {
                    Block b = new Location(w, x, y, z).getBlock();
                    if (b.getType() == type){
                        blocks.add(b);
                    }
                }
            }
        }
        //And last but not least, we return with the list
        return blocks;
    }
}
