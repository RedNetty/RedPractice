package com.rednetty.redpractice.commands.moderation;

import com.rednetty.redpractice.events.RankChangeEvent;
import com.rednetty.redpractice.mechanic.server.moderation.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetRankCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender.hasPermission("practice.setrank")) {
            if (args.length < 0 || args.length > 2) {
                sender.sendMessage(ChatColor.RED + "Incorrect Args! /setrank <user> <rank>");
                return false;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (!target.isOnline()) {
                sender.sendMessage(ChatColor.RED + "Player is invalid!");
                return false;

            }
            if (!Rank.rankExists(args[1])) {
                sender.sendMessage(ChatColor.RED + "Rank doesn't exist.");
                return false;
            }
            RankChangeEvent event = new RankChangeEvent(sender, target, args[1]);
            Bukkit.getServer().getPluginManager().callEvent(event);
        }
        return false;

    }
}