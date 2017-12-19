package com.rednetty.redpractice.mechanic.player.economy;

import com.rednetty.redpractice.RedPractice;
import com.rednetty.redpractice.mechanic.Mechanics;
import com.rednetty.redpractice.mechanic.player.GamePlayer;
import com.rednetty.redpractice.utils.items.ItemBuilder;
import com.rednetty.redpractice.utils.items.NBTEditor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EconomyHandler extends Mechanics implements Listener {


    public ItemStack getGems(int Amount) {
        return new ItemBuilder(Material.EMERALD).setAmount(Amount).setName("&aGem").setLore(Arrays.asList("&7The currency of Andalucia", "&7Deposit this in a bank for safekeeping")).build();
    }

    /**
     * Used to generate a bank note
     *
     * @param amount - Amount you want the bank note to be
     * @return - Returns the Bank Note ItemStack
     */
    public ItemStack createBankNote(int amount) {
        ItemStack itemStack = new ItemStack(Material.PAPER);
        NBTEditor nbtEditor = new NBTEditor(itemStack);
        nbtEditor.check();
        nbtEditor.setInt("value", amount);
        ItemStack updatedItem = nbtEditor.update();
        return new ItemBuilder(updatedItem).setName("&aGem Note").setLore(Arrays.asList("&f&lValue: &r&f" + amount + " Gem(s)", "&7Exchange at any bank for GEM(s)")).build();
    }

    /**
     * Used to get the Value of Gems, Gem Pouches, or Gem Notes
     *
     * @param itemStack - Item you want to check
     * @return - Returns Value of Gems
     */
    public int getValue(ItemStack itemStack) {
        int amount = 0;
        switch (itemStack.getType()) {
            case PAPER:
                NBTEditor nbtEditor = new NBTEditor(itemStack);
                nbtEditor.check();
                if (nbtEditor.hasKey("value")) amount += nbtEditor.getInteger("value");
            case EMERALD:
                amount += itemStack.getAmount();

            default:
        }
        return amount;
    }

    /**
     * Used to check the players INVENTORY (NOT BANK) if he has enough gems
     *
     * @return - Returns True or False if the player does have enough gems
     */
    public boolean hasEnoughOnPerson(int amount, GamePlayer gamePlayer) {
        Player player = gamePlayer.getPlayer();
        int gemCount = 0;
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack == null) continue;
            if (itemStack.getType() == Material.EMERALD) gemCount += itemStack.getAmount();
            if (itemStack.getType() == Material.PAPER) {
                NBTEditor nbtEditor = new NBTEditor(itemStack);
                nbtEditor.check();
                if (nbtEditor.hasKey("value")) gemCount += nbtEditor.getInteger("value");
            }

        }
        return amount > gemCount;
    }

    /**
     * Used to check the players BANK (NOT INVENTORY) if he has enough gems
     *
     * @return - Returns True or False if the player does have enough gems
     */
    public boolean hasEnoughInBank(int amount, GamePlayer gamePlayer) {
        return amount < gamePlayer.getGemAmount();
    }


    /**
     * Correctly takes gems froma  players inventory
     *
     * @param amount - Amount you are taking
     * @param player - PLayer you are taking it from
     * @return - Returns the Amount you are taking
     */
    public boolean takeGemsFromInventory(int amount, Player player) {
        Inventory i = player.getInventory();
        int amountLeft = 0;

        HashMap<Integer, ? extends ItemStack> invItems = i.all(Material.EMERALD);
        for (Map.Entry<Integer, ? extends ItemStack> entry : invItems.entrySet()) {
            int index = entry.getKey();
            ItemStack item = entry.getValue();
            int stackAmount = item.getAmount();

            if (amountLeft >= amount) {
                return true;
            }

            if ((amountLeft + stackAmount) <= amount) {
                player.getInventory().setItem(index, new ItemStack(Material.AIR));
                amountLeft += stackAmount;
            } else {
                int toTake = amount - amountLeft;
                player.getInventory().setItem(index, getGems(stackAmount - toTake));
                amountLeft += toTake;
            }
        }

        HashMap<Integer, ? extends ItemStack> bankNote = i.all(Material.PAPER);
        for (Map.Entry<Integer, ? extends ItemStack> entry : bankNote.entrySet()) {
            ItemStack item = entry.getValue();
            int noteAmount = getValue(item);
            int index = entry.getKey();

            if (amountLeft >= amount) {
                return true;
            }
            if ((amountLeft + noteAmount) <= amount) {
                player.getInventory().setItem(index, new ItemStack(Material.AIR));
                amountLeft += noteAmount;
            } else {
                int toTake = amount - amountLeft;
                amountLeft += toTake;
                player.getInventory().setItem(index, createBankNote(noteAmount - toTake));
            }


        }
        return false;

    }

    /**
     * Adds money to the players account
     *
     * @param amount     - The amount you wanna add to a player
     * @param gamePlayer -  The player you wanna add it to
     */
    public void depositGems(int amount, GamePlayer gamePlayer) {
        gamePlayer.setGemAmount(gamePlayer.getGemAmount() + amount);
        Player player = gamePlayer.getPlayer();
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        player.sendMessage(ChatColor.GREEN.toString() + ChatColor.GREEN + "New Balance: " + ChatColor.GREEN + gamePlayer.getGemAmount() + " Gem(s)");
        RedPractice.getMechanicManager().getPlayerHandler().updateGamePlayer(gamePlayer);
    }

    /**
     * Removes money from the players account
     *
     * @param amount     - The amount you wanna remove from a player
     * @param gamePlayer -  The player you wanna remove it from
     */
    public void withdrawGems(int amount, GamePlayer gamePlayer) {

        gamePlayer.setGemAmount(gamePlayer.getGemAmount() - amount);
        Player player = gamePlayer.getPlayer();
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        player.sendMessage(ChatColor.GREEN.toString() + ChatColor.GREEN + "New Balance: " + ChatColor.GREEN + gamePlayer.getGemAmount() + " Gem(s)");
        RedPractice.getMechanicManager().getPlayerHandler().updateGamePlayer(gamePlayer);
    }

    public void onEnable() {
        listener(this);
    }

    public void onDisable() {
    }
}

