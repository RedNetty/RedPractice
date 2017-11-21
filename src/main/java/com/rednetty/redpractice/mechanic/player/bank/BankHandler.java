package com.rednetty.redpractice.mechanic.player.bank;

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

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

public class BankHandler extends Mechanics implements Listener {

    private ConcurrentHashMap<Player, Integer> upgradingMap = new ConcurrentHashMap<>();

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
     */
    public void updateBankSize(GamePlayer gamePlayer) {
        gamePlayer.setBankSize(gamePlayer.getBankSize() + 9);
        Inventory inventory = Bukkit.createInventory(null, gamePlayer.getBankSize() + 9, gamePlayer.getPlayer().getName() + "'s Bank (1/1)");
        for (ItemStack itemStack : gamePlayer.getBankInventory().getContents()) {
            if (itemStack != null && itemStack.getType() != Material.EMERALD && itemStack.getType() != Material.THIN_GLASS)
                inventory.addItem(itemStack);
        }
        gamePlayer.setBankInventory(inventory);
        PlayerHandler.updateGamePlayer(gamePlayer);
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
            inventory.setItem(inventory.getSize() - 1, bankGem(gamePlayer));
        } else {
            IntStream.range(inventory.getSize() - 9, inventory.getSize()).forEach(slot -> {
                if (slot == inventory.getSize() - 5) {
                    inventory.setItem(slot, bankGem(gamePlayer));
                } else {
                    inventory.setItem(slot, new ItemStack(Material.THIN_GLASS));
                }

            });
        }
        return inventory;
    }

    //This is where opening the bank is handled
    @EventHandler
    public void onBankOpen(InventoryOpenEvent event) {
        if (event.getInventory().getType() == InventoryType.ENDER_CHEST) {
            event.setCancelled(true);
            if (event.getPlayer() instanceof Player) {
                Player player = (Player) event.getPlayer();
                player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1F, 1F);
                player.openInventory(getBank(PlayerHandler.getGamePlayer(player)));
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
        return current * 150;
    }

    //Deals with Taking out Gems
    @EventHandler
    public void onInventoryInteract(InventoryClickEvent event) {
        if (event.getInventory().getTitle().contains("Bank")) {
            if (event.getCurrentItem().getType() == Material.EMERALD || event.getCurrentItem().getType() == Material.THIN_GLASS) {
                event.setCancelled(true);
            }
        }
    }


    //Handles the starting of upgrading the bank
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.LEFT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.ENDER_CHEST) {
            GamePlayer gamePlayer = PlayerHandler.getGamePlayer(player);
            if (upgradingMap.containsKey(player)) return;

            if (gamePlayer.getBankSize() >= 54) {
                player.sendMessage(ChatColor.RED + "Your bank is already max size.");
                return;
            }
            int price = upgradePrice(gamePlayer.getBankSize());
            StringUtil.sendCenteredMessage(player, ChatColor.translateAlternateColorCodes('&', "&8*** &a&lBank Upgrade Confirmation &r&8***"));
            StringUtil.sendCenteredMessage(player, ChatColor.DARK_GRAY + "Current Slots: " + ChatColor.GREEN + gamePlayer.getBankSize() + ChatColor.DARK_GRAY + " New Slots:" + ChatColor.GREEN + (gamePlayer.getBankSize() + 9));
            player.sendMessage("");
            StringUtil.sendCenteredMessage(player, ChatColor.DARK_GRAY + "Upgrade Cost: " + ChatColor.GREEN + price + " Gem(s)");
            player.sendMessage("");
            StringUtil.sendCenteredMessage(player, ChatColor.translateAlternateColorCodes('&', "&aEnter '&lConfirm&r&a' to confirm your upgrade"));
            StringUtil.sendCenteredMessage(player, ChatColor.translateAlternateColorCodes('&', "&c&lWARNING: &r&cBank upgrades are NOT reversible or refundable. Type 'cancel' to void this upgrade request."));
            upgradingMap.put(player, price);
        }
    }

    //Handles the Chat Event for Bank Upgrade
    @EventHandler
    public void chatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (upgradingMap.containsKey(player)) {
            event.setCancelled(true);
            String message = event.getMessage().toLowerCase();
            if (!message.contains("confirm") || !EconomyHandler.hasEnoughInBank(upgradingMap.get(player), PlayerHandler.getGamePlayer(player))) {
                player.sendMessage(ChatColor.RED + "Bank upgrade cancelled");
                player.sendMessage(ChatColor.RED + "You either cancelled the upgrade or do not have enough money.");
            }
            if (message.contains("confirm")) {
                StringUtil.sendCenteredMessage(player, ChatColor.translateAlternateColorCodes('&', "&a&l*** &a&lBANK UPGRADE COMPLETE ***"));
                updateBankSize(PlayerHandler.getGamePlayer(player));
                EconomyHandler.withdrawGems(upgradingMap.get(player), PlayerHandler.getGamePlayer(player));

            }
            upgradingMap.remove(player);
        }
    }
}
