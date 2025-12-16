package com.rednetty.redpractice.mechanic.player;

import com.rednetty.redpractice.RedPractice;
import com.rednetty.redpractice.configs.PlayerConfigs;
import com.rednetty.redpractice.configs.RankConfig;
import com.rednetty.redpractice.mechanic.Mechanics;
import com.rednetty.redpractice.mechanic.items.itemgenerator.Armor;
import com.rednetty.redpractice.mechanic.items.itemgenerator.ItemRarity;
import com.rednetty.redpractice.mechanic.items.itemgenerator.Weapon;
import com.rednetty.redpractice.utils.strings.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
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
    public Map<Player, GamePlayer> gamePlayerHashMap = new ConcurrentHashMap<>();


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
    public void generatePlayerConfig(Player player) {
        FileConfiguration fileConfig = PlayerConfigs.getPlayerConfig(player.getUniqueId());
        if (!fileConfig.isSet("Gems")) fileConfig.set("Gems", 0);
        if (!fileConfig.isSet("Guild Name")) fileConfig.set("Guild Name", "");
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
    public void saveGamePlayer(Player player) {
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
     *
     * @param player - The player you want to load Data
     */
    public void loadPlayer(Player player) {
        PlayerConfigs.setupPlayerConfig(player.getUniqueId());
        generatePlayerConfig(player);
        FileConfiguration fileConfig = PlayerConfigs.getPlayerConfig(player.getUniqueId());
        /*Loads Gems and Bank Size*/
        int gemBalance = fileConfig.getInt("Gems");
        int bankSize = fileConfig.getInt("Bank Size");
        String guildName = fileConfig.getString("Guild Name");
        String playerRank = RankConfig.getConfig().getString(player.getUniqueId().toString());
        /*Loads Bank Inventory*/
        Inventory inventory = Bukkit.createInventory(null, bankSize, player.getName() + "'s Bank (1/1)");
        if (!fileConfig.get("Bank Inventory").equals("Empty")) {
            loadBankItems(fileConfig).forEach(itemStack -> {
                if (itemStack != null && itemStack.getType() != Material.EMERALD && itemStack.getType() != Material.THIN_GLASS)
                    inventory.addItem(itemStack);
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

        //Data Loading
        loadPlayer(player);

        //Damage Loading
        player.setMaximumNoDamageTicks(0);
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(7200);
        player.saveData();

        //Health Loading
        player.setMaxHealth(RedPractice.getMechanicManager().getHealthHandler().getMaxHealth(player.getInventory().getArmorContents()));
        player.setHealthScale(20);
        player.setHealthScaled(true);


        //BETA MESSAGE
        new BukkitRunnable() {
            @Override
            public void run() {
                getGamePlayer(player).openMenu(new BetaMenu(getGamePlayer(player)));
            }
        }.runTaskLaterAsynchronously(RedPractice.getInstance(), 3L);


        //TEST
        Weapon weapon = new Weapon(Material.GOLD_SWORD, 200, 500, ItemRarity.UNIQUE);
        weapon.setIce(40);
        weapon.setArmorpen(20);
        player.getInventory().addItem(weapon.build());

        Weapon weapon1 = new Weapon(Material.GOLD_SWORD, 200, 500, ItemRarity.UNIQUE);
        weapon1.setPoison(40);
        weapon1.setAccuracy(20);
        weapon1.setBlind(10);
        weapon1.setLifesteal(10);
        player.getInventory().addItem(weapon1.build());

        Armor armor = new Armor(Material.GOLD_HELMET, 5400, ItemRarity.UNIQUE);
        armor.setArmor(20);
        armor.setEnergy(6);
        armor.setVit(400);
        armor.setDodge(12);
        armor.setBlock(12);
        armor.setReflection(4);
        armor.setThorns(20);
        player.getInventory().addItem(armor.build());

        Armor chest = new Armor(Material.GOLD_CHESTPLATE, 7000, ItemRarity.UNIQUE);
        chest.setDPS(15);
        chest.setEnergy(5);
        chest.setDodge(12);
        chest.setBlock(12);
        chest.setReflection(4);
        chest.setThorns(20);
        player.getInventory().addItem(chest.build());

        Armor legs = new Armor(Material.GOLD_LEGGINGS, 7000, ItemRarity.UNIQUE);
        legs.setDPS(15);
        legs.setEnergy(5);
        legs.setDodge(12);
        legs.setBlock(12);
        legs.setReflection(4);
        player.getInventory().addItem(legs.build());

        Armor boots = new Armor(Material.GOLD_BOOTS, 7000, ItemRarity.UNIQUE);
        boots.setDPS(15);
        boots.setEnergy(5);
        boots.setDodge(12);
        boots.setBlock(12);
        boots.setReflection(4);
        player.getInventory().addItem(boots.build());


    }


    /**
     * Used to update Player Data
     *
     * @param gamePlayer - Instance of the GamePlayer class that is stored in the HashMap above
     */
    public void updateGamePlayer(GamePlayer gamePlayer) {
        gamePlayerHashMap.put(gamePlayer.getPlayer(), gamePlayer);
    }

    /*Returns instance of the GamePlayer of this Player*/
    public GamePlayer getGamePlayer(Player player) {
        if (!gamePlayerHashMap.containsKey(player)) loadPlayer(player);
        return gamePlayerHashMap.get(player);
    }


    /**
     * Method used for turning the Strings into ItemStacks
     *
     * @return Returns a ItemStack[] that is used to add the items into the invenntory
     */
    public ArrayList<ItemStack> loadBankItems(FileConfiguration fileConfig) {
        ArrayList<ItemStack> content = (ArrayList<ItemStack>) fileConfig.getList("Bank Inventory");
        for (int i = 0; i < content.size(); i++) {
            ItemStack item = content.get(i);
            if (item == null) continue;
            content.set(i, item);

        }
        return content;
    }
}
