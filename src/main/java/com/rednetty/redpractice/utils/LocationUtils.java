package com.rednetty.redpractice.utils;


import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationUtils {


    public static Location fromString(String string) {
        String[] locationString = string.split(",");
        return new Location(Bukkit.getWorld(locationString[0]), Integer.parseInt(locationString[1]), Integer.parseInt(locationString[2]), Integer.parseInt(locationString[3]));
    }
}
