package com.rednetty.redpractice.mechanic.player.guild;

import com.rednetty.redpractice.mechanic.player.PlayerHandler;
import com.rednetty.redpractice.mechanic.player.economy.EconomyHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.concurrent.ConcurrentHashMap;

public class GuildCreation implements Listener {

    public ConcurrentHashMap<Player, Boolean> creatingGuildList = new ConcurrentHashMap<>();

    @EventHandler
    public void onRightClick(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked() instanceof HumanEntity && event.getRightClicked().hasMetadata("NPC")) {
            HumanEntity humanEntity = (HumanEntity) event.getRightClicked();
            if (humanEntity.getName().equals("Guild Registrar")) {
                creatingGuildList.put(event.getPlayer(), false);
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Guild Registrar: &fHello, ") +
                        ChatColor.UNDERLINE + event.getPlayer().getName() + ChatColor.WHITE + ", I'm the guild registrar, would you like to create a guild today? Please note that it will cost 5,000 GEM(s). (" +
                        ChatColor.GREEN.toString() + ChatColor.BOLD + "Y " + ChatColor.WHITE + "/" + ChatColor.RED.toString() + ChatColor.BOLD + "N" + ChatColor.WHITE + ")");
                event.getPlayer().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "THERE IS NO TURNING BACK AFTER YOU SAY YES.");

            }
        }
    }


    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        if (creatingGuildList.contains(player)) {
            String guildName = null;
            String guildTag = null;
            if (creatingGuildList.get(player) == false) {
                if(message.toLowerCase().equals("yes") || message.toLowerCase().contains("y") && EconomyHandler.hasEnoughOnPerson(5000, PlayerHandler.getGamePlayer(player))) {
                    creatingGuildList.put(player, true);
                    EconomyHandler.takeGemsFromInventory(5000, player);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Guild Registrar: &fPlease enter your requested guild name.."));

                }else{
                    creatingGuildList.remove(player);
                    player.sendMessage(ChatColor.RED + "Guild Creation Cancelled..");
                }
            }
            if(creatingGuildList.get(player) == true) {
                if(guildName == null) {
                    if(GuildHandler.guildExists(message)) {
                        player.sendMessage(ChatColor.RED + "Guild Name Already Exists!");
                        return;
                    }
                    if(message.length() > 45) {
                        player.sendMessage(ChatColor.RED + "Guild Name is too long! (Must be under 46 Characters!");
                        return;
                    }
                    guildName = message;
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Guild Registrar: &fGuild Name has been set! Now Enter the guild tag you would like!"));

                }
                if(guildTag == null) {
                    if(GuildHandler.tagExists(message)) {
                        player.sendMessage(ChatColor.RED + "Guild Tag Already Exists!");
                        return;
                    }
                    if(message.length() > 4) {
                        player.sendMessage(ChatColor.RED + "Guild Tag is too long! (Must be be under 5 Characters!");
                        return;
                    }
                    guildTag = message;
                    player.sendMessage(ChatColor.DARK_BLUE + "The Guild (" + guildName + ") has been created!");
                    GuildHandler.createGuild(player, guildName, guildTag);

                }

            }
        }
    }
}
