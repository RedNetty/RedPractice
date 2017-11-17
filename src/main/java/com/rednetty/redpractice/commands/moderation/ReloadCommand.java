package com.rednetty.redpractice.commands.moderation;

import com.rednetty.redpractice.RedPractice;
import com.rednetty.redpractice.configs.PermissionConfig;
import com.rednetty.redpractice.configs.RankConfig;
import com.rednetty.redpractice.mechanic.player.PlayerHandler;
import com.rednetty.redpractice.mechanic.player.bank.BankHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender.hasPermission("practice.reload")) {
            PermissionConfig.reloadConfig();
            RankConfig.reloadConfig();
            if(commandSender instanceof Player) {
                Player player = (Player)commandSender;
                player.openInventory(RedPractice.getMechanicManager().getBankHandler().getBank(PlayerHandler.getGamePlayer(player)));
            }
        }
        return false;

    }
}
