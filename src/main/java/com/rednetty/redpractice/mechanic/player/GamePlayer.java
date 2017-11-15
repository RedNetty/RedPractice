package com.rednetty.redpractice.mechanic.player;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.Inventory;

public class GamePlayer {

    @Getter
    @Setter
    public int gemAmount;

    @Getter
    @Setter
    public Inventory bankInventory;

    @Getter
    @Setter
    public int bankSize;


    /**
     * This is used the initialize a instance of the GamePlayer class
     * @param gemAmount - The players currency amount
     * @param bankInventory - The players bank inventory
     * @param bankSize - slot size of players bank
     */
    public GamePlayer(int gemAmount, Inventory bankInventory, int bankSize) {
        setBankInventory(bankInventory);
        setBankSize(bankSize);
        setGemAmount(gemAmount);
    }


}
