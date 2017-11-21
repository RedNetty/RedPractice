package com.rednetty.redpractice.mechanic.player.economy;

import com.rednetty.redpractice.mechanic.Mechanics;
import com.rednetty.redpractice.mechanic.player.GamePlayer;
import com.rednetty.redpractice.mechanic.player.PlayerHandler;
import com.rednetty.redpractice.utils.items.ItemBuilder;
import com.rednetty.redpractice.utils.items.NBTEditor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class EconomyHandler extends Mechanics implements Listener{

    public void onEnable() {
        listener(this);
    }

    public void onDisable() {
    }

    /**
     * Used to generate a bank note
     * @param amount - Amount you want the bank note to be
     * @return - Returns the Bank Note ItemStack
     */
    public static ItemStack createBankNote(int amount) {
        ItemStack itemStack = new ItemStack(Material.PAPER);
        NBTEditor nbtEditor =  new NBTEditor(itemStack);
        nbtEditor.setInt("value", amount);
        ItemStack updatedItem = nbtEditor.update();
        return new ItemBuilder(updatedItem).setName("&aGem Note").setLore(Arrays.asList("&f&lValue: &r&f" + amount + "Gem(s)", "&7Exchange at any bank for GEM(s)")).build();
    }

    /**
     * Used to check the players INVENTORY (NOT BANK) if he has enough gems
     * @return - Returns True or False if the player does have enough gems
     */
    public static boolean hasEnoughOnPerson(int amount, GamePlayer gamePlayer) {
        Player player = gamePlayer.getPlayer();
        int gemCount = 0;
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if(itemStack.getType() == Material.EMERALD) gemCount += itemStack.getAmount();
            if(itemStack.getType() == Material.PAPER) {
                NBTEditor nbtEditor = new NBTEditor(itemStack);
                if(nbtEditor.hasKey("value")) gemCount += nbtEditor.getInteger("value");
            }

        }
        return amount < gemCount;
    }


    /**
     * Used to check the players BANK (NOT INVENTORY) if he has enough gems
     * @return - Returns True or False if the player does have enough gems
     */
    public static boolean hasEnoughInBank(int amount, GamePlayer gamePlayer) {
        return amount < gamePlayer.getGemAmount();
    }


    /**
     * Adds money to the players account
     * @param amount - The amount you wanna add to a player
     * @param gamePlayer -  The player you wanna add it to
     */
    public static void depositGems(int amount, GamePlayer gamePlayer) {
        gamePlayer.setGemAmount(gamePlayer.getGemAmount() + amount);
        PlayerHandler.updateGamePlayer(gamePlayer);
    }


    /**
     * Removes money from the players account
     * @param amount - The amount you wanna remove from a player
     * @param gamePlayer -  The player you wanna remove it from
     */
    public static void withdrawGems(int amount, GamePlayer gamePlayer) {
        gamePlayer.setGemAmount(gamePlayer.getGemAmount() - amount);
        PlayerHandler.updateGamePlayer(gamePlayer);
    }
}

