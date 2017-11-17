package com.rednetty.redpractice.mechanic.moderation;

import com.rednetty.redpractice.RedPractice;
import com.rednetty.redpractice.configs.PermissionConfig;
import com.rednetty.redpractice.configs.RankConfig;
import com.rednetty.redpractice.events.RankChangeEvent;
import com.rednetty.redpractice.mechanic.Mechanics;
import com.rednetty.redpractice.mechanic.player.GamePlayer;
import com.rednetty.redpractice.mechanic.player.PlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public class ModerationHandler extends Mechanics implements Listener {

    @Override
    public void onEnable() {
        PermissionConfig.setupConfig();
        RankConfig.setupConfig();
        listener(this);
    }

    @Override
    public void onDisable() {
    }


    /**
     * Used to grab the Display Tag for the Players Rank
     * @return - Returns the Display Tag that is shown ingame for the rank
     */
    public static String getRankTag(RankEnum rankEnum) {
        return ChatColor.translateAlternateColorCodes('&', RankEnum.getRankDisplay(rankEnum));
    }

    /*Deals with when a players rank is changed*/
    @EventHandler
    public void onRankChange(RankChangeEvent event) {

        CommandSender setter = event.getSetter();
        Player target = event.getTarget();
        GamePlayer gamePlayer = PlayerHandler.getGamePlayer(target);
        setter.sendMessage(ChatColor.GREEN + "You set " + target.getName() + "'s rank to " + getRankTag(event.getRank()));
        target.sendMessage(ChatColor.GREEN + "Your rank was set to " +  getRankTag(event.getRank()));
        gamePlayer.setPlayerRank(event.getRank());
        PlayerHandler.updateGamePlayer(gamePlayer);
        updatePermission(target);
    }


    /**
     * Updates the players permissions based on the Permissions set inside the Config
     */
    public void updatePermission(Player player) {
        GamePlayer gamePlayer = PlayerHandler.getGamePlayer(player);
        gamePlayer.setPermissions(player.addAttachment(RedPractice.getInstance()));
        RankEnum.getRankPerms(gamePlayer.getPlayerRank()).forEach(perm -> gamePlayer.getPermissions().setPermission(perm, true));
        PlayerHandler.updateGamePlayer(gamePlayer);
    }
}
