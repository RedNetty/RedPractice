package com.rednetty.redpractice.mechanic.player.bank;

import com.rednetty.redpractice.RedPractice;
import com.rednetty.redpractice.mechanic.Mechanics;
import com.rednetty.redpractice.mechanic.player.GamePlayer;
import com.rednetty.redpractice.mechanic.player.PlayerHandler;
import com.rednetty.redpractice.mechanic.player.economy.EconomyHandler;
import com.rednetty.redpractice.utils.items.ItemBuilder;
import com.rednetty.redpractice.utils.strings.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

public class BankHandler extends Mechanics implements Listener {

    private ConcurrentHashMap<Player, Integer> upgradingMap = new ConcurrentHashMap<>();
    private ArrayList<Player> convertingNoteList = new ArrayList<>();

    @Override
    public void onEnable() {
        listener(this);
    }

    @Override
    public void onDisable() {
    }


    public ItemStack gemItem(GamePlayer gamePlayer) {
        return new ItemBuilder(Material.EMERALD).setName("&a" + gamePlayer.getGemAmount() + " &lGEM(s)").setLore(Arrays.asList("&7Right Click to create &aA GEM NOTE")).build();
    }

    /**
     * Updates the Players Bank Correctly and Updates the Inventory to Move Items over
     */
    public void updateBankSize(GamePlayer gamePlayer) {
        PlayerHandler playerHandler = RedPractice.getMechanicManager().getPlayerHandler();
        gamePlayer.setBankSize(gamePlayer.getBankSize() + 9);
        Inventory inventory = Bukkit.createInventory(null, gamePlayer.getBankSize() + 9, gamePlayer.getPlayer().getName() + "'s Bank (1/1)");
        for (ItemStack itemStack : gamePlayer.getBankInventory().getContents()) {
            if (itemStack != null && itemStack.getType() != Material.EMERALD && itemStack.getType() != Material.THIN_GLASS)
                inventory.addItem(itemStack);
        }
        gamePlayer.setBankInventory(inventory);
        playerHandler.updateGamePlayer(gamePlayer);
    }

    /**
     * Gets the Players Inventory Bank and adds the Gems
     *
     * @param gamePlayer - Who you want to retrieve's inventory
     * @return - Inventory of the Bank
     */
    public Inventory getBank(GamePlayer gamePlayer) {
        Inventory inventory = gamePlayer.getBankInventory();
        if (inventory.getSize() <= 27) {
            inventory.setItem(inventory.getSize() - 1, gemItem(gamePlayer));
        } else {
            IntStream.range(inventory.getSize() - 9, inventory.getSize()).forEach(slot -> {
                if (slot == inventory.getSize() - 5) {
                    inventory.setItem(slot, gemItem(gamePlayer));
                } else {
                    inventory.setItem(slot, new ItemStack(Material.THIN_GLASS));
                }

            });
        }
        return inventory;
    }

    /**
     * Updates the Players Bank Gem so that it shows the correct Value
     *
     * @param gamePlayer - Gameplayer?
     * @param inventory  - The Inventory you are checking for (Should be a Bank) ??
     */
    public void updateBankGem(GamePlayer gamePlayer, Inventory inventory) {
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null && itemStack.getType() == Material.EMERALD) {
                itemStack.setItemMeta(gemItem(gamePlayer).getItemMeta());
            }
        }
    }


    /**
     * Get the price of the next upgrade for the players bank
     *
     * @param current - Current slots in the players bank
     * @return - Returns the Price of Gems that the upgrade will cost
     */
    public int upgradePrice(int current) {
        if (current >= 54) return 0;
        return current * 200;
    }


    /**
     * Handles the Opening of the Players Bank
     */
    @EventHandler
    public void onBankOpen(InventoryOpenEvent event) {
        PlayerHandler playerHandler = RedPractice.getMechanicManager().getPlayerHandler();
        if (event.getInventory().getType() == InventoryType.ENDER_CHEST) {
            event.setCancelled(true);
            if (event.getPlayer() instanceof Player) {
                Player player = (Player) event.getPlayer();
                player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1F, 1F);
                player.openInventory(getBank(playerHandler.getGamePlayer(player)));
            }
        }
    }

    /**
     * Deals with Adding and Removing Items from the Bank
     */
    @EventHandler
    public void onInventoryInteract(InventoryClickEvent event) {
        EconomyHandler economyHandler = RedPractice.getMechanicManager().getEconomyHandler();
        PlayerHandler playerHandler = RedPractice.getMechanicManager().getPlayerHandler();
        if (event.getView().getTopInventory().getTitle().contains("Bank")) {
            if (event.getWhoClicked() instanceof Player) {
                Player player = (Player) event.getWhoClicked();
                GamePlayer gamePlayer = playerHandler.getGamePlayer(player);
                if (event.isShiftClick() && event.getCurrentItem() != null) { // Deals with when it is Shift Clicked in
                    ItemStack itemStack = event.getCurrentItem();
                    int amount = economyHandler.getValue(itemStack);
                    if (amount > 0) {
                        event.setCancelled(true);
                        economyHandler.depositGems(amount, playerHandler.getGamePlayer(player));
                        event.setCurrentItem(null);
                        updateBankGem(gamePlayer, event.getView().getTopInventory());
                    }
                }
                if (!event.isShiftClick() && event.getCursor() != null) { // Deals with when it is placed in.
                    ItemStack itemStack = event.getCursor();
                    int amount = economyHandler.getValue(itemStack);
                    if (amount > 0) {
                        economyHandler.depositGems(amount, playerHandler.getGamePlayer(player));
                        event.setCursor(null);
                        player.updateInventory();
                        updateBankGem(gamePlayer, event.getView().getTopInventory());
                    }
                }
                if (convertingNoteList.contains(player)) return;
                if (event.getCurrentItem().getType() == Material.EMERALD || event.getCurrentItem().getType() == Material.THIN_GLASS && event.getClickedInventory().getTitle().contains("Bank")) {
                    event.setCancelled(true);
                    if (event.getCurrentItem().getType() == Material.EMERALD) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Please enter the amount you'd like to CONVERT to a gem note. Alternatively, type &c'cancel' &7to void this operation"));
                        player.closeInventory();
                        convertingNoteList.add(player);
                    }
                }
            }
        }
    }


    /**
     * Deals with the Interacting Part of Upgrading the Players Bank Account
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerHandler playerHandler = RedPractice.getMechanicManager().getPlayerHandler();
        if (event.getAction() == Action.LEFT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.ENDER_CHEST) {
            GamePlayer gamePlayer = playerHandler.getGamePlayer(player);
            if (upgradingMap.containsKey(player)) return;

            if (gamePlayer.getBankSize() >= 54) {
                player.sendMessage(ChatColor.RED + "Your bank is already max size.");
                return;
            }
            int price = upgradePrice(gamePlayer.getBankSize());
            StringUtil.sendCenteredMessage(player, ChatColor.translateAlternateColorCodes('&', "&8*** &a&lBank Upgrade Confirmation &r&8***"));
            player.sendMessage("");
            StringUtil.sendCenteredMessage(player, ChatColor.DARK_GRAY + "Current Slots: " + ChatColor.GREEN + gamePlayer.getBankSize() + ChatColor.DARK_GRAY + " New Slots:" + ChatColor.GREEN + (gamePlayer.getBankSize() + 9));
            player.sendMessage("");
            StringUtil.sendCenteredMessage(player, ChatColor.DARK_GRAY + "Upgrade Cost: " + ChatColor.GREEN + price + " Gem(s)");
            player.sendMessage("");
            StringUtil.sendCenteredMessage(player, ChatColor.translateAlternateColorCodes('&', "&aEnter '&lConfirm&r&a' to confirm your upgrade"));
            StringUtil.sendCenteredMessage(player, ChatColor.translateAlternateColorCodes('&', "&c&lWARNING: &r&cBank upgrades are NOT reversible or refundable. Type 'cancel' to void this upgrade request."));
            upgradingMap.put(player, price);
        }
    }

    /**
     * Uses the Event to Handle a Chat Prompt for Taking money out of The Bank and Upgrading
     * The Players Bank Size
     */
    @EventHandler
    public void chatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage().toLowerCase();
        EconomyHandler economyHandler = RedPractice.getMechanicManager().getEconomyHandler();
        PlayerHandler playerHandler = RedPractice.getMechanicManager().getPlayerHandler();
        if (upgradingMap.containsKey(player)) {
            event.setCancelled(true);
            if (!message.contains("confirm") || !economyHandler.hasEnoughInBank(upgradingMap.get(player), playerHandler.getGamePlayer(player))) {
                player.sendMessage(ChatColor.RED + "Bank upgrade cancelled");
                player.sendMessage(ChatColor.RED + "You either cancelled the upgrade or do not have enough money.");
            }
            if (message.contains("confirm")) {
                StringUtil.sendCenteredMessage(player, ChatColor.translateAlternateColorCodes('&', "&a&l*** &a&lBANK UPGRADE COMPLETE ***"));
                updateBankSize(playerHandler.getGamePlayer(player));
                economyHandler.withdrawGems(upgradingMap.get(player), playerHandler.getGamePlayer(player));

            }
            upgradingMap.remove(player);
        }
        if (convertingNoteList.contains(player)) {
            event.setCancelled(true);
            int noteAmount = 0;
            try {
                noteAmount = Integer.parseInt(message);
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "You have cancelled your WITHDRAW.");
            }
            if (economyHandler.hasEnoughInBank(noteAmount, playerHandler.getGamePlayer(player))) {
                economyHandler.withdrawGems(noteAmount, playerHandler.getGamePlayer(player));
                player.getInventory().addItem(economyHandler.createBankNote(noteAmount));
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 1.0f, 1.2f);
            } else {
                player.sendMessage(ChatColor.RED + "Please enter a NUMBER, the amount you'd like to WITHDRAW from your bank account. Or type 'cancel' to void the withdrawl.");
            }
            convertingNoteList.remove(player);

        }
    }
}
