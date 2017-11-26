package com.rednetty.redpractice.mechanic.player.guild;

import com.rednetty.redpractice.RedPractice;
import com.rednetty.redpractice.configs.GuildConfigs;
import com.rednetty.redpractice.mechanic.Mechanics;
import com.rednetty.redpractice.mechanic.player.GamePlayer;
import com.rednetty.redpractice.mechanic.player.PlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GuildHandler extends Mechanics implements Listener {

    private static ConcurrentHashMap<String, Guild> guildHashMap = new ConcurrentHashMap<>();

    @Override
    public void onEnable() {
        listener(this);
        Bukkit.getServer().getPluginManager().registerEvents(new GuildCreation(), RedPractice.getInstance());
        GuildConfigs.setupConfig();
    }

    @Override
    public void onDisable() {
    }

    /**
     * Get the Guild Instance from a String
     *
     * @param guildName - The Guild Name you are trying to get the Guild
     * @return - Returns either Null if it doesn't exist or the Guild
     * @apiNote - MUST BE USED ONLY IF THE GUILD IS LOADED!!
     */
    public static Guild guildFromString(String guildName) {
        if (guildHashMap.contains(guildName)) {
            return guildHashMap.get(guildName);
        }
        return null;
    }

    /**
     * Allows you to see if a Guild is Loaded by its name
     * Typically used to check if a Guild is loaded on the Players Data Loading
     *
     * @param guildName - Name of the guild you are checking
     * @return - Returns true or false - True = Loaded | False = Not Loaded
     */
    public static boolean isGuildLoaded(String guildName) {
        return guildHashMap.contains(guildName) ? true : false;
    }

    /**
     * Allows you to check if a Player is in a Guild
     *
     * @param player - The Player you want to check for
     * @return - Returns true or false
     */
    public static boolean isInAGuild(Player player) {
        if (PlayerHandler.getGamePlayer(player).getGuildName() != "") return true;
        return false;
    }


    /**
     * Checks if a Player is officer of Specified Guild
     * @param player - Player you're checking
     * @param guild - Guild you're checking
     * @return - Returns True or False
     */
    public static boolean isOfficer(Player player, Guild guild) {
        for (OfflinePlayer officer : guild.getGuildOfficers()) {
            if (officer == player)
                return true;
        }
        return false;
    }

    /**
     * Checks the GuildConfigs if the Guild Exists
     * @param guildName - Guild you are checking
     * @return - Returns True or False
     */
    public static boolean guildExists(String guildName) {
        return GuildConfigs.guildExists(guildName.toLowerCase());
    }

    /**
     * Allows you to load a Guild from its Guild Name
     * @param guildName - The name of the Guild
     */
    public static void loadGuild(String guildName) {
        GuildConfigs.setupGuildConfig(guildName.toLowerCase());
        String guildTag = GuildConfigs.getGuildConfig(guildName).getString("Guild Tag");
        List<OfflinePlayer> memberList = new ArrayList<>();
        GuildConfigs.getGuildConfig(guildName.toLowerCase()).getStringList("Guild Members").forEach(memberUUID -> memberList.add(Bukkit.getOfflinePlayer(UUID.fromString(guildName))));
        List<OfflinePlayer> officerList = new ArrayList<>();
        GuildConfigs.getGuildConfig(guildName.toLowerCase()).getStringList("Guild Officers").forEach(memberUUID -> officerList.add(Bukkit.getOfflinePlayer(UUID.fromString(guildName))));
        OfflinePlayer guildOwner = Bukkit.getOfflinePlayer(UUID.fromString(GuildConfigs.getGuildConfig(guildName.toLowerCase()).getString("Guild Owner")));
        guildHashMap.put(guildName, new Guild(guildName, guildTag, memberList, officerList, guildOwner));
    }

    /**
     * Updates the Guild Hashmap with a newer version of the Guild
     * @param guild - Guild that you are trying to update
     */
    public void updateGuild(Guild guild) {
        guildHashMap.put(guild.getGuildName(), guild);
    }

    /**
     * Correctly Kicks a Player from a Guild
     * @param guild - Guild you want to kick the Player from
     * @param player - Player you want to kick from the Guild
     */
    public void kickPlayer(Guild guild, Player player) {
        List<OfflinePlayer> newMemberList = new ArrayList<>();
        guild.getGuildMembers().forEach(member -> { if(member != player) newMemberList.add(member); });
        guild.setGuildMembers(newMemberList);
        GamePlayer gamePlayer = PlayerHandler.getGamePlayer(player);
        gamePlayer.setGuildName("");
        PlayerHandler.updateGamePlayer(gamePlayer);
        updateGuild(guild);
    }

    /**
     * Correctly Demotes a Player in a Guild
     * @param guild - Guild you want the Demote Player in
     * @param player - Player you want to Demote
     */
    public void demotePlayer(Guild guild, Player player) {
        guild.getGuildOfficers().remove(player);
        guild.getGuildMembers().add(player);
        updateGuild(guild);
    }

    /**
     * Checks if a Guild Tag already exists
     * @param guildTag - Guild tag you are checking for
     * @return - Returns true or false
     */
    public static boolean tagExists(String guildTag) {
        YamlConfiguration config = new YamlConfiguration();
        File[] files = new File(RedPractice.getInstance().getDataFolder(), "GuildData").listFiles();
        for(File file : files){
            try {
                config.load(file);
                if(config.get("Guild Tag").equals(guildTag)){
                    return true;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    /**
     * Promotes a Player to Officer in a Guild
     * @param guild - Guild you want to Promote the Player in
     * @param player - Player you want to Promote
     */
    public void promotePlayer(Guild guild, Player player) {
        guild.getGuildOfficers().add(player);
        guild.getGuildMembers().remove(player);
        updateGuild(guild);
    }

    /**
     * Creates a Guild and Registers is
     * @param guildOwner - Owner of the Guild
     * @param guildName - Guild Name
     * @param guildTag - Tag of the Guild
     */
    public static void createGuild(Player guildOwner, String guildName, String guildTag) {
        List<OfflinePlayer> guildMembers = new ArrayList<>();
        List<OfflinePlayer> guildOfficers = new ArrayList<>();
        guildHashMap.put(guildName, new Guild(guildName, guildTag, guildMembers, guildOfficers, guildOwner));
        GuildConfigs.setupGuildConfig(guildName.toLowerCase()); //Adding the Config for the Guild
        GamePlayer gamePlayer = PlayerHandler.getGamePlayer(guildOwner);
        gamePlayer.setGuildName(guildName);
        PlayerHandler.updateGamePlayer(gamePlayer);
    }

    /**
     * Used to get ALL the players inside a Guild
     * @param guild - Guild that you are checking
     * @return - Returns a list of OfflinePlayers
     */
    public List<OfflinePlayer> getAllMembers(Guild guild) {
        List<OfflinePlayer> allList = new ArrayList<>();
        guild.getGuildMembers().forEach(member -> allList.add(member));
        guild.getGuildOfficers().forEach(officer -> allList.add(officer));
        allList.add(guild.getGuildOwner());
        return allList;
    }

    /**
     * Allows the Sending of a Message to all Online Guild Members
     * @param Message - Message you want to send
     * @param guild - Guild you want to send it to
     */
    public void sendGuildMessage(String Message, Guild guild) {
        getAllMembers(guild).forEach(member -> { if(member.isOnline()) ((Player)member).sendMessage(Message); });

    }


}
