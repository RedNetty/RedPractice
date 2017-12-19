package com.rednetty.redpractice.mechanic.server.moderation;

import com.rednetty.redpractice.configs.PermissionConfig;

import java.util.ArrayList;
import java.util.List;

public class Rank {

    private static List<String> rankList = new ArrayList<>();

    /**
     * Checks a String to decide if the Rank exists.
     *
     * @param string - The Rank you are Checking
     * @return Returns a boolean
     */
    public static boolean rankExists(String string) {
        if (rankList.contains(string.toLowerCase())) return true;
        return false;
    }


    /**
     * Grabs the Ranks from the config and adds them to the RankList
     */
    public static void loadRankList() {
        for (String string : PermissionConfig.getConfig().getKeys(false)) {
            rankList.add(string.toLowerCase());
        }
    }

    /**
     * A Bit odd But this is used to get the correct Case (AaAaALLLell) for the config
     *
     * @param rank - Rank you are trying to translate
     * @return - Returns Correct Case for checking config
     */
    private static String getConfigCase(String rank) {
        for (String string : PermissionConfig.getConfig().getKeys(false)) {
            if (rank.equalsIgnoreCase(string)) return string;
        }
        return "";
    }

    /**
     * Used to get the Permissions set inside the Config
     *
     * @param rank - Used to Specify the Rank of the Permissions you wanna grab
     * @return - Returns the List of Permissions
     */
    public static List<String> getRankPermissions(String rank) {
        List<String> permList = new ArrayList<>();
        if (!rankExists(rank)) return permList;
        PermissionConfig.getConfig().getStringList(getConfigCase(rank) + ".permissions").forEach(s -> permList.add(s));
        if (PermissionConfig.getConfig().getString(getConfigCase(rank) + ".inherits").length() > 0) {
            getRankPermissions(PermissionConfig.getConfig().getString(getConfigCase(rank) + ".inherits")).forEach(permList::add);
        }
        return permList;
    }

    /**
     * Grabs the Prefix of the Rank in the Config
     *
     * @param rank - Rank that you want to retrieve the prefix for
     * @return - Returns String of Prefix
     */
    public static String getPrefix(String rank) {
        if (!rankExists(rank)) return "";
        return PermissionConfig.getConfig().getString(getConfigCase(rank) + ".prefix");
    }
}
