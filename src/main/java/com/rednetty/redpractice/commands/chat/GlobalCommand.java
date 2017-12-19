package com.rednetty.redpractice.commands.chat;

import com.rednetty.redpractice.RedPractice;
import com.rednetty.redpractice.mechanic.player.chat.ChatHandler;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class GlobalCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.hasPermission("practice.globalchat") && commandSender instanceof Player) {
            ChatHandler chatHandler = RedPractice.getMechanicManager().getChatHandler();
            Player player = (Player) commandSender;
            Set<Player> recipients = new HashSet<>(Bukkit.getOnlinePlayers());
            String globalPrefix = "&b<&lG&r&b> "; /*Used to Determine what Prefix to use*/
            String fullMessage = StringUtils.join(strings, " "); /*Joins the Spaces between the Args into a Single Message*/
            String lowerCase = fullMessage.toLowerCase();
            if (lowerCase.contains("wts") || lowerCase.contains("wtb") || lowerCase.contains("trading") || lowerCase.contains("buying")) {
                globalPrefix = "&a<&lT&r&a> ";
            }
            String beforeMessage = ChatColor.translateAlternateColorCodes('&', globalPrefix + chatHandler.getFullTag(player) + "&7" + player.getName() + ": &f");
            if (lowerCase.contains("@i@") && player.getInventory().getItemInMainHand().getType() != Material.AIR) { /*If player is trying to show a Item Display it with the Method Below*/
                chatHandler.sendShowMessage(player, recipients, globalPrefix + chatHandler.getFullTag(player), fullMessage, player.getInventory().getItemInMainHand());
            } else {
                for (Player target : recipients) {
                    target.sendMessage(beforeMessage + fullMessage);
                }
            }
        }
        return false;
    }
}
