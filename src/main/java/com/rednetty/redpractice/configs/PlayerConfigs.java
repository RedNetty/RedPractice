package com.rednetty.redpractice.configs;

import com.rednetty.redpractice.RedPractice;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;

public class PlayerConfigs {

    @Getter
    private static HashMap<Player, FileConfiguration> playerFileConfigMap = new HashMap<>();

    public void setupConfig() {
        File file = new File(RedPractice.getInstance().getDataFolder(), "PlayerData");
        if (!file.exists()) {
            file.mkdirs();
        }
    }


    public static void setupPlayerConfig(Player player) {
        File file = new File(RedPractice.getInstance().getDataFolder() + "/PlayerData", player.getName() + ".yml");
        if (!file.exists()) {
            file.mkdirs();
        }
        playerFileConfigMap.put(player, YamlConfiguration.loadConfiguration(file));
    }


    public static FileConfiguration getPlayerConfig(Player player) {
        if(!playerFileConfigMap.containsKey(player)) setupPlayerConfig(player);
        return playerFileConfigMap.get(player);
    }

    public static void savePlayerConfig(Player player) {

    }
}
