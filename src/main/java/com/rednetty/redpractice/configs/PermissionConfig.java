package com.rednetty.redpractice.configs;

import com.rednetty.redpractice.RedPractice;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class PermissionConfig {

    private static FileConfiguration fileConfig;
    private static File file;


    public static void setupConfig() {
        file = new File(RedPractice.getInstance().getDataFolder(), "permissions.yml");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

        } catch (IOException e) {
            Bukkit.broadcastMessage("Error: Could not Create Config 'permissions.yml'");
            e.printStackTrace();
        }
        fileConfig = YamlConfiguration.loadConfiguration(file);
        try {
            Reader defConfigStream = new InputStreamReader(RedPractice.getInstance().getResource("permissions.yml"), "UTF8");
            if (defConfigStream != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                fileConfig.setDefaults(defConfig);
            }
            fileConfig.options().copyDefaults(true);
            saveConfig();
        } catch (UnsupportedEncodingException e) {
            Bukkit.broadcastMessage("Error: Could not Copy Defaults in the Config 'permissions.yml'");
            e.printStackTrace();
        }


    }

    public static FileConfiguration getConfig() {
        return fileConfig;
    }

    public static void saveConfig() {
        try {
            fileConfig.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reloadConfig() {
        fileConfig = YamlConfiguration.loadConfiguration(file);
    }
}
