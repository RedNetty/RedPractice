package com.rednetty.redpractice.mechanic.player.guild;


import org.bukkit.entity.Player;

import java.util.List;

public class Guild {

    private String guildName;
    private String guildTag;
    private List<Player> guildMembers;
    private List<Player> guildOfficers;
    private Player guildOwner;

    public Guild(String guildName, String guildTag, List<Player> guildMembers, List<Player> guildOfficers, Player guildOwner) {
        this.guildMembers = guildMembers;
        this.guildName = guildName;
        this.guildTag = guildTag;
        this.guildOfficers = guildOfficers;
        this.guildOwner = guildOwner;
    }

    /**
     * Used to get a List of all The Members in the Guild
     * @return - Returns a List of Players
     */
    public List<Player> getGuildMembers() {
        return guildMembers;
    }

    /**
     * Used to get a List of all The Officers in the Guild
     * @return - Returns a List of Players
     */
    public List<Player> getGuildOfficers() {
        return guildOfficers;
    }

    /**
     * Used to get the Owner of the Guild
     * @return - Returns a The Player (Owner)
     */
    public Player getGuildOwner() {
        return guildOwner;
    }

    /**
     * Used to get the Guild Name in String Format
     * @return - Returns a String
     */
    public String getGuildName() {
        return guildName;
    }

    /**
     * Used to get the Guild Tag used for Name Prefix and Other Things
     * Typically Looks like [TAG] In-game
     * @return - Returns a String
     */
    public String getGuildTag() {
        return guildTag;
    }

    /**
     * Allows you to set/change the Guild Member List
     * @param guildMembers - Requires a List of Players
     */
    public void setGuildMembers(List<Player> guildMembers) {
        this.guildMembers = guildMembers;
    }

    /**
     * Allows you to change the Guild Name
     * @param guildName - The String you would Like the Guild Name to be (Also what will be saved in the Config)
     */
    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }

    /**
     * Allows you to set/change the Guild Officer List
     * @param guildOfficers -  Requires a List of Players
     */
    public void setGuildOfficers(List<Player> guildOfficers) {
        this.guildOfficers = guildOfficers;
    }

    /**
     * Allows you to set a Player as the Owner of a Guild
     * @param guildOwner - A Player that you want to set as owner
     */
    public void setGuildOwner(Player guildOwner) {
        this.guildOwner = guildOwner;
    }

    /**
     * Allows you to set the tag of a Guild
     *
     * @param guildTag - The Tag you would like to set
     */
    public void setGuildTag(String guildTag) {
        this.guildTag = guildTag;
    }
}
