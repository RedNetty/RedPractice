package com.rednetty.redpractice.mechanic.player;

import com.rednetty.redpractice.RedPractice;
import com.rednetty.redpractice.configs.PlayerConfigs;
import com.rednetty.redpractice.configs.RankConfig;
import com.rednetty.redpractice.mechanic.Mechanics;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerHandler extends Mechanics implements Listener {

    /*Hashmap that stores GamePlayer instances temporarily*/
    public static Map<Player, GamePlayer> gamePlayerHashMap = new ConcurrentHashMap<>();


    @Override
    public void onEnable() {
        PlayerConfigs.setupConfig();
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(player -> saveGamePlayer(player));
            }
        }.runTaskTimerAsynchronously(RedPractice.getInstance(), 200L, 200L);
        listener(this);
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            saveGamePlayer(player);
        }
    }


    /**
     * Generates the Default Config Entries for the Player
     *
     * @param player - Requires you to specify a SpigotAPI Player
     */
    public static void generatePlayerConfig(Player player) {
        FileConfiguration fileConfig = PlayerConfigs.getPlayerConfig(player.getUniqueId());
        if (!fileConfig.isSet("Gems")) fileConfig.set("Gems", 0);
        if (!fileConfig.isSet("Bank Size")) fileConfig.set("Bank Size", 9);
        if (!fileConfig.isSet("Bank Inventory")) fileConfig.set("Bank Inventory", "Empty");
        if (!RankConfig.getConfig().contains(player.getUniqueId().toString()))
            RankConfig.getConfig().set(player.getUniqueId().toString(), "Default");
        PlayerConfigs.savePlayerConfig(player.getUniqueId());
        RankConfig.saveConfig();
    }

    /**
     * Saves player data in all the correct configs.
     *
     * @param player - Player data that needs saving
     */
    public static void saveGamePlayer(Player player) {
        FileConfiguration fileConfig = PlayerConfigs.getPlayerConfig(player.getUniqueId());
        fileConfig.set("Gems", getGamePlayer(player).getGemAmount());
        fileConfig.set("Bank Size", getGamePlayer(player).getBankSize());
        fileConfig.set("Bank Inventory", getGamePlayer(player).getBankInventory().getContents());
        RankConfig.getConfig().set(player.getUniqueId().toString(), getGamePlayer(player).getPlayerRank().toString());
        PlayerConfigs.savePlayerConfig(player.getUniqueId());
        RankConfig.saveConfig();
    }

    @EventHandler
    public void onLogout(PlayerQuitEvent event) {
        saveGamePlayer(event.getPlayer());
    }


    @EventHandler
    public void onKick(PlayerKickEvent event) {
        saveGamePlayer(event.getPlayer());
    }


    /**
     * Loads the Data of a Player Correctly
     * @param player - The player you want to load Data
     */
    public static void loadPlayer(Player player) {
        PlayerConfigs.setupPlayerConfig(player.getUniqueId());
        generatePlayerConfig(player);
        FileConfiguration fileConfig = PlayerConfigs.getPlayerConfig(player.getUniqueId());
        /*Loads Gems and Bank Size*/
        int gemBalance = fileConfig.getInt("Gems");
        int bankSize = fileConfig.getInt("Bank Size");
        String playerRank = RankConfig.getConfig().getString(player.getUniqueId().toString());
        /*Loads Bank Inventory*/
        Inventory inventory = Bukkit.createInventory(null, bankSize, player.getName() + "'s Bank (1/1)");
        if (!fileConfig.get("Bank Inventory").equals("Empty")) {
            loadBankItems(fileConfig).forEach(itemStack -> {
                if(itemStack != null && itemStack.getType() != Material.EMERALD && itemStack.getType() != Material.THIN_GLASS) inventory.addItem(itemStack);
            });
        }
        GamePlayer gamePlayer = new GamePlayer(player, playerRank, gemBalance, inventory, bankSize);
        gamePlayerHashMap.put(player, gamePlayer);
        RedPractice.getMechanicManager().getModerationHandler().updatePermission(player);
    }
    /**
     * Deals with the loading of Data on Player Login
     * If there is any issues with a players data is would likely be here.
     */
    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (gamePlayerHashMap.containsKey(event.getPlayer())) return;
        loadPlayer(player);
    }


    /**
     *  Used to update Player Data
     * @param gamePlayer - Instance of the GamePlayer class that is stored in the HashMap above
     */
    public static void updateGamePlayer(GamePlayer gamePlayer) {
        gamePlayerHashMap.put(gamePlayer.getPlayer(), gamePlayer);
    }

    /*Returns instance of the GamePlayer of this Player*/
    public static GamePlayer getGamePlayer(Player player) {
        if(!gamePlayerHashMap.containsKey(player)) loadPlayer(player);
        return gamePlayerHashMap.get(player);
    }


    /**
     * Method used for turning the Strings into ItemStacks
     *
     * @return Returns a ItemStack[] that is used to add the items into the invenntory
     */
    public static  ArrayList<ItemStack> loadBankItems(FileConfiguration fileConfig) {
        ArrayList<ItemStack> content = (ArrayList<ItemStack>) fileConfig.getList("Bank Inventory");
        for (int i = 0; i < content.size(); i++) {
            ItemStack item = content.get(i);
            if (item == null) continue;
            content.set(i, item);

        }
        return content;
    }
}
