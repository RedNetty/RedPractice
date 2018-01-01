package com.rednetty.redpractice.configs;

import com.rednetty.redpractice.RedPractice;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class SpawnerConfig {

    private static FileConfiguration config;
    private static File file;


    public static void setupConfig() {
        file = new File(RedPractice.getInstance().getDataFolder(), "spawner.yml");
        try{
            if(!file.exists()) {
                file.createNewFile();
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
        config = YamlConfiguration.loadConfiguration(file);
    }


    public static FileConfiguration getConfig() {
        return config;
    }

    public static void saveConfig() {
        try{
            config.save(file);
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
}
