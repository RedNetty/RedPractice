package com.rednetty.redpractice.mechanic.player;

import com.rednetty.redpractice.mechanic.moderation.RankEnum;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.permissions.PermissionAttachment;

public class GamePlayer {

    public Player player;
    public int gemAmount;
    public Inventory bankInventory;
    public int bankSize;
    public RankEnum playerRank;
    PermissionAttachment permissions;


    /**
     * Used to get the Player
     * @return - Player Specified in Class
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Used to get the Size of the Players Bank
     * @return - Returns a int that gives the size of the players bank
     */
    public int getBankSize() {
        return bankSize;
    }

    /**
     * Used to get the Gem Amount of the Player
     * @return - Returns a int that gives you that players gem count
     */
    public int getGemAmount() {
        return gemAmount;
    }

    /**
     * Used to get the Bank of the Player
     * @return - Returns a int that gives you that players Bank Inventory
     */
    public Inventory getBankInventory() {
        return bankInventory;
    }

    /**
     * Used to get the PermissionAttachmennt from the Player
     * @return - Returns the Permission file for the Player
     */
    public PermissionAttachment getPermissions() {
        return permissions;
    }

    /**
     * Used to get the Rank of the Specified Player
     * @return - Returns the Players Rank
     */
    public RankEnum getPlayerRank() {
        return playerRank;
    }

    /**
     * Used to set the Bank Inventory of the Player
     */
    public void setBankInventory(Inventory bankInventory) {
        this.bankInventory = bankInventory;
    }


    /**
     * Used to set the Bank Size of the Player
     */
    public void setBankSize(int bankSize) {
        this.bankSize = bankSize;
    }

    /**
     * Used to set the Gem Amount of the Player
     */
    public void setGemAmount(int gemAmount) {
        this.gemAmount = gemAmount;
    }

    /**
     * Used to set the Permissions of the Player
     */
    public void setPermissions(PermissionAttachment permissions) {
        this.permissions = permissions;
    }

    public void setPlayerRank(RankEnum playerRank) {
        this.playerRank = playerRank;
    }

    /**
     * This is used the initialize a instance of the GamePlayer class
     * @param gemAmount - The players currency amount
     * @param bankInventory - The players bank inventory
     * @param bankSize - slot size of players bank
     * @param playerRank - Requires a RankEnum
     */
    public GamePlayer(Player player, RankEnum playerRank, int gemAmount, Inventory bankInventory, int bankSize) {
        setBankInventory(bankInventory);
        setBankSize(bankSize);
        setGemAmount(gemAmount);
        setPlayerRank(playerRank);
        this.player = player;
    }


}
