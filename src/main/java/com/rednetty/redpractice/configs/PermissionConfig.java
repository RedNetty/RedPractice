package com.rednetty.redpractice.configs;

import com.rednetty.redpractice.RedPractice;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PermissionConfig {

    private static FileConfiguration fileConfig;
    private static File file;


    public static void setupConfig() {
        file = new File(RedPractice.getInstance().getDataFolder(), "permissions.yml");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

        }catch(IOException e) {
            e.printStackTrace();
        }
        fileConfig = YamlConfiguration.loadConfiguration(file);
        fileConfig.options().copyDefaults(true);
        saveConfig();


    }

    public static FileConfiguration getConfig() {
        return fileConfig;
    }

    public static void saveConfig() {
        try {
            fileConfig.save(file);
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void reloadConfig() {
        fileConfig = YamlConfiguration.loadConfiguration(file);
    }
}
