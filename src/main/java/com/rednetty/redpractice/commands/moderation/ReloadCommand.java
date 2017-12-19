package com.rednetty.redpractice.commands.moderation;

import com.rednetty.redpractice.configs.PermissionConfig;
import com.rednetty.redpractice.configs.RankConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender.hasPermission("practice.reload")) {
            PermissionConfig.reloadConfig();
            RankConfig.reloadConfig();
        }
        return false;

    }
}
