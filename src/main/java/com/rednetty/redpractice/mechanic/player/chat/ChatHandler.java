package com.rednetty.redpractice.mechanic.player.chat;

import com.rednetty.redpractice.RedPractice;
import com.rednetty.redpractice.mechanic.MechanicManager;
import com.rednetty.redpractice.mechanic.Mechanics;
import com.rednetty.redpractice.utils.JSONMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ChatHandler extends Mechanics implements Listener {

    @Override
    public void onEnable() {
        listener(this);
    }

    @Override
    public void onDisable() {

    }

    /**
     * Used to Combine all the Player Tags
     *
     * @param player - The Player you are trying to get it for
     * @return - Returns the Prefixes/Tags
     */
    public String getFullTag(Player player) {
        MechanicManager mechanicManager = RedPractice.getMechanicManager();
        return mechanicManager.getModerationHandler().getNameTag(mechanicManager.getPlayerHandler().getGamePlayer(player).getPlayerRank());
    }


    /**
     * This is used when a player puts @i@ in his message, it will send a JSONMessage with the info of the Item Specified.
     *
     * @param sender     - This would be the person sending the message originally
     * @param recipients - The Recipients of the message that should get this message
     * @param prefix     - Mainly used for Global ChatHandler this is so you can add a prefix to the message
     * @param message    - The message the sender sent
     * @param itemStack  - ItemStack the sender is trying to show
     */
    public void sendShowMessage(Player sender, Set<Player> recipients, String prefix, String message, ItemStack itemStack) {
        if (itemStack != null && itemStack.getType() != Material.AIR) {
            String[] splitMessage = message.split("@i@");
            String beforeItem = "";
            String afterItem = "";
            if (splitMessage.length > 0)
                beforeItem = splitMessage[0];
            if (splitMessage.length > 1)
                afterItem = splitMessage[1];
            /*Moves the Item Info onto the List*/
            List<String> itemString = new ArrayList<>();
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemString.add(itemMeta.hasDisplayName() ? itemMeta.getDisplayName() : itemStack.getType().name());
            if (itemMeta.hasLore()) itemMeta.getLore().forEach(lore -> itemString.add(lore));
            /*Starts making the Json Message*/
            JSONMessage jsonMessage = new JSONMessage(ChatColor.translateAlternateColorCodes('&', prefix + ChatColor.GRAY + sender.getName() + ": "));
            jsonMessage.addText(beforeItem);
            jsonMessage.addHoverText(itemString, ChatColor.translateAlternateColorCodes('&', "&f&l&nSHOW"));
            jsonMessage.addText(afterItem);
            recipients.forEach(player -> jsonMessage.sendToPlayer(player)); /*Sends message to Recipients*/

        }
    }


    /*Local ChatHandler*/
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        event.getRecipients().clear();
        player.getLocation().getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20).forEach(entity -> {
            if (entity instanceof Player) event.getRecipients().add((Player) entity);
        });
        String playerPrefix = getFullTag(player);
        if (event.getMessage().contains("@i@") && player.getInventory().getItemInMainHand().getType() != Material.AIR) { /*Used for Showing Items in ChatHandler*/
            sendShowMessage(player, event.getRecipients(), playerPrefix, event.getMessage(), event.getPlayer().getInventory().getItemInMainHand());
            event.setCancelled(true);
        } else { /*If player is not showing item just send normal Message*/
            event.setFormat(ChatColor.translateAlternateColorCodes('&', playerPrefix + "&7" + player.getName() + ": &f" + event.getMessage()));
        }

        /*Nobody Heard You Alert (Used a Task because it would send before the message sometimes)*/
        if (event.getRecipients().size() <= 1 && !event.isCancelled()) {
            player.sendMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC + "Nobody heard you..");
        }
    }
}
