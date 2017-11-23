package com.rednetty.redpractice.configs;

import com.rednetty.redpractice.RedPractice;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class GuildConfigs {

    @Getter
    private static HashMap<String, FileConfiguration> guildFileConfigMap = new HashMap<>();

    public static void setupConfig() {
        File file = new File(RedPractice.getInstance().getDataFolder(), "GuildData");
        if (!file.exists()) {
            file.mkdirs();
        }
    }


    public static void setupGuildConfig(String guildName) {
        File file = new File(RedPractice.getInstance().getDataFolder() + "/GuildData", guildName + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
        guildFileConfigMap.put(guildName, YamlConfiguration.loadConfiguration(file));
    }


    public static FileConfiguration getGuildConfig(String guildName) {
        if(!guildFileConfigMap.containsKey(guildName)) setupGuildConfig(guildName);
        return guildFileConfigMap.get(guildName);
    }

    public static void saveGuildConfig(String guildName) {
        if(!guildFileConfigMap.containsKey(guildName)) setupGuildConfig(guildName);
        try {
            File file = new File(RedPractice.getInstance().getDataFolder() + "/GuildData", guildName + ".yml");
            FileConfiguration fileConfiguration = guildFileConfigMap.get(guildName);
            fileConfiguration.save(file);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}


