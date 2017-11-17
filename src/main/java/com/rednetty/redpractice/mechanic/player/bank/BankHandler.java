package com.rednetty.redpractice.mechanic.player.bank;

import com.rednetty.redpractice.mechanic.Mechanics;
import com.rednetty.redpractice.mechanic.player.GamePlayer;
import com.rednetty.redpractice.mechanic.player.PlayerHandler;
import com.rednetty.redpractice.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.stream.IntStream;

public class BankHandler extends Mechanics implements Listener{

    @Override
    public void onEnable() {
        listener(this);
    }

    @Override
    public void onDisable() {
    }


    public ItemStack bankGem(GamePlayer gamePlayer) {
        return new ItemBuilder(Material.EMERALD).setName("&a" + gamePlayer.getGemAmount() + " &lGEM(s)").setLore(Arrays.asList("&7Right Click to create &aA GEM NOTE")).build();
    }

    /**
     * Updates the Players Bank Correctly and Updates the Inventory to Move Items over
     * @param size - The Slot Size you want the bank to be
     */
    public void updateBankSize(GamePlayer gamePlayer, int size) {
        gamePlayer.setBankSize(size);
        Inventory inventory = Bukkit.createInventory(null, size, gamePlayer.getPlayer().getName() + "'s Bank (1/1)");
        for (ItemStack itemStack : gamePlayer.getBankInventory().getContents()) {
            if(itemStack != null && itemStack.getType() != Material.EMERALD && itemStack.getType() != Material.THIN_GLASS) inventory.addItem(itemStack);
        }
        gamePlayer.setBankInventory(inventory);
        PlayerHandler.updateGamePlayer(gamePlayer);
    }

    /**
     * Gets the Players Inventory Bank and adds the Gems
     * @param gamePlayer - Who you want to retrieve's inventory
     * @return - Inventory of the Bank
     */
    public Inventory getBank(GamePlayer gamePlayer) {
        Inventory inventory = gamePlayer.getBankInventory();
        if(inventory.getSize() <= 27) {
            inventory.setItem(inventory.getSize() - 1, bankGem(gamePlayer));
        }else{
            IntStream.range(inventory.getSize() - 9, inventory.getSize()).forEach(slot -> {
                if(slot == inventory.getSize() - 5) {
                    inventory.setItem(slot, bankGem(gamePlayer));
                }else{
                    inventory.setItem(slot, new ItemStack(Material.THIN_GLASS));
                }

            });
        }
        return inventory;
    }
}
