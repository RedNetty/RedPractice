package com.rednetty.redpractice.mechanic.player.chat;

import com.rednetty.redpractice.RedPractice;
import com.rednetty.redpractice.mechanic.Mechanics;
import com.rednetty.redpractice.mechanic.player.PlayerHandler;
import com.rednetty.redpractice.utils.JSONMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Chat extends Mechanics implements Listener {

    @Override
    public void onEnable() {
        listener(this);
    }

    @Override
    public void onDisable() {

    }



    /**
     * This is used when a player puts @i@ in his message, it will send a JSONMessage with the info of the Item Specified.
     * @param sender     - This would be the person sending the message originally
     * @param recipients - The Recipients of the message that should get this message
     * @param prefix     - Mainly used for Global Chat this is so you can add a prefix to the message
     * @param message    - The message the sender sent
     * @param itemStack  - ItemStack the sender is trying to show
     */
    public void sendShowMessage(Player sender, Set<Player> recipients, String prefix, String message, ItemStack itemStack) {
        if (itemStack != null && itemStack.getType() != Material.AIR) {
            String[] splitMessage = message.split("@i@");
            String beforeItem = "";
            String afterItem = "";
            if(splitMessage.length > 0)
                beforeItem = splitMessage[0];
            if(splitMessage.length > 1)
                afterItem = splitMessage[1];
            /*Moves the Item Info onto the List*/
            List<String> itemString = new ArrayList<>();
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemString.add(itemMeta.hasDisplayName() ? itemMeta.getDisplayName() : itemStack.getType().name());
            if(itemMeta.hasLore()) itemMeta.getLore().forEach(lore -> itemString.add(lore));
            /*Starts making the Json Message*/
            JSONMessage jsonMessage = new JSONMessage(prefix + ChatColor.GRAY + sender.getName() + ": ");
            jsonMessage.addText(beforeItem);
            jsonMessage.addHoverText(itemString, ChatColor.translateAlternateColorCodes('&', "&f&l&nSHOW"));
            jsonMessage.addText(afterItem);
            recipients.forEach(player -> jsonMessage.sendToPlayer(player)); /*Sends message to Recipients*/

        }
    }


    /*Local Chat*/
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        event.getRecipients().clear();
        player.getLocation().getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20).forEach(entity -> {
            if (entity instanceof Player) event.getRecipients().add((Player) entity);
        });
        String playerRank =  RedPractice.getMechanicManager().getModerationHandler().getRankTag(PlayerHandler.getGamePlayer(player).getPlayerRank());
        if (event.getMessage().contains("@i@")) { /*Used for Showing Items in Chat*/
            sendShowMessage(player, event.getRecipients(), playerRank, event.getMessage(), event.getPlayer().getInventory().getItemInMainHand());
            event.setFormat("");
        } else { /*If player is not showing item just send normal Message*/
            event.setFormat(ChatColor.translateAlternateColorCodes('&', playerRank + "&7" + player.getName() + ": &f" + event.getMessage()));
        }

        /*Nobody Heard You Alert (Used a Task because it would send before the message sometimes)*/
        new BukkitRunnable() {
            @Override
            public void run() {
                event.getRecipients().forEach(recipient -> recipient.sendMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC + "Nobody heard you.."));
            }
        }.runTaskLaterAsynchronously(RedPractice.getInstance(), 1L);
    }
}
