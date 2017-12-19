package com.rednetty.redpractice.commands.moderation;

import com.rednetty.redpractice.configs.RankConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender.hasPermission("practice.heal")) {
            if (args.length < 0 && commandSender instanceof Player) {
                Player player = (Player) commandSender;
                player.setHealth(player.getMaxHealth());
            } else {
                Player player = Bukkit.getPlayer(args[0]);
                if (player.isOnline()) {
                    player.setHealth(player.getMaxHealth());
                }
            }
            RankConfig.reloadConfig();
        }
        return false;

    }
}
