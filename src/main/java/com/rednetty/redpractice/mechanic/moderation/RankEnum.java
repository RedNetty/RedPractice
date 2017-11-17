package com.rednetty.redpractice.mechanic.moderation;


import com.rednetty.redpractice.configs.PermissionConfig;

import java.util.List;

public enum RankEnum {
    DEFAULT,
    SUB,
    SUB1,
    SUB2,
    SUPPORTER,
    PMOD,
    GM;


    /**
     * Used to get the permissions list for a rank.
     * @return - Returns a list of Permission Strings
     */
    public static List<String> getRankPerms(RankEnum rankEnum) {
        List<String> permList = PermissionConfig.getConfig().getStringList(rankEnum.name() + ".permission");
        return permList;
    }

    /**
     * Used to get the displayName that players see ingame for Ranks
     * @apiNote ( DONT USE THIS FOR INGAME CHAT IT DOESNT HAVE COLOR )
     * @return - Returns the String Display Name
     */
    public static String getRankDisplay(RankEnum rankEnum) {
        String rankName = PermissionConfig.getConfig().getString(rankEnum.name() + ".prefix");
        return rankName;
    }

    /**
     * Used to translate a String into a Enum
     * @param rankString - Input a String that is supposed to be similar to what the Rank displayName is
     * @return It will return a RankEnum
     */

    public static RankEnum fromString(String rankString) {
        switch(rankString.toLowerCase()) {
            case "default":
                return DEFAULT;
            case "sub":
                return SUB;
            case "sub+":
                return SUB1;
            case "sub++":
                return SUB2;
            case "supporter":
                return SUPPORTER;
            case "pmod":
                return PMOD;
            case "gm":
                return GM;

        }
        return DEFAULT;

    }

    /**
     * Used to load Ranks from the Config File (Can't use FromString as they are different)
     * @param string - The String that you want to translate
     * @return - Returns RankEnum as a rank that is translated.
     */
    public static RankEnum fromConfig(String string) {
        return valueOf(string);
    }

    /**
     * used to translate the Enum into a String
     * @return - Returns the String name of the RankEnum
     */
    public String toString() {
        return name();
    }

}
