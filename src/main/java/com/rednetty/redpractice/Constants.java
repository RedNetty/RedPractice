package com.rednetty.redpractice;

import lombok.Getter;
import org.bukkit.ChatColor;

public class Constants {
    @Getter
    private static String SERVER_NAME = ChatColor.translateAlternateColorCodes('&',
            RedPractice.getInstance().getConfig().getString("Server Name"));
}
