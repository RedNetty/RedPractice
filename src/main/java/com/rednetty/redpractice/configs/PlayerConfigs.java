package com.rednetty.redpractice.configs;

import com.rednetty.redpractice.RedPractice;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class PlayerConfigs {

    @Getter
    private static HashMap<UUID, FileConfiguration> playerFileConfigMap = new HashMap<>();

    public static void setupConfig() {
        File file = new File(RedPractice.getInstance().getDataFolder(), "PlayerData");
        if (!file.exists()) {
            file.mkdirs();
        }
    }


    public static void setupPlayerConfig(UUID playerID) {
        File file = new File(RedPractice.getInstance().getDataFolder() + "/PlayerData", playerID.toString() + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        playerFileConfigMap.put(playerID, YamlConfiguration.loadConfiguration(file));
    }


    public static FileConfiguration getPlayerConfig(UUID playerID) {
        if (!playerFileConfigMap.containsKey(playerID)) setupPlayerConfig(playerID);
        return playerFileConfigMap.get(playerID);
    }

    public static void savePlayerConfig(UUID playerID) {
        if (!playerFileConfigMap.containsKey(playerID)) setupPlayerConfig(playerID);
        try {
            File file = new File(RedPractice.getInstance().getDataFolder() + "/PlayerData", playerID.toString() + ".yml");
            FileConfiguration fileConfiguration = playerFileConfigMap.get(playerID);
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
