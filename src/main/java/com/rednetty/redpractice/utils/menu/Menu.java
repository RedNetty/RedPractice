package com.rednetty.redpractice.utils.menu;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.rednetty.redpractice.mechanic.player.GamePlayer;
import com.rednetty.redpractice.utils.strings.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Giovanni on 11/12/2017.
 */
public abstract class Menu {

    private String name;
    private int slots = 54;
    private HashMap<Integer, Button> buttonMap;
    private Inventory inventory;

    private List<GamePlayer> viewers;

    public Menu(String name, int slots) {
        this.buttonMap = Maps.newHashMap();
        this.name = name;
        this.slots = slots;
        this.inventory = Bukkit.createInventory(null, this.slots, StringUtil.colorCode(this.name));
        this.viewers = Lists.newArrayList();
    }

    public Menu(InventoryType inventoryType, String name) {
        this.buttonMap = Maps.newHashMap();
        this.name = name;
        this.inventory = Bukkit.createInventory(null, inventoryType, StringUtil.colorCode(name));
        this.viewers = Lists.newArrayList();
    }


    /**
     * Add a button to the menu
     *
     * @param button The button
     * @return Menu
     */
    public Menu newButton(Button button) {
        buttonMap.put(button.getSlot(), button);
        inventory.setItem(button.getSlot(), button.getItemStack());
        return this;
    }

    /**
     * Register a collection of buttons
     *
     * @param buttons The buttons
     * @return Menu
     */
    public Menu newButtons(Collection<Button> buttons) {
        buttons.forEach(button -> {
            buttonMap.put(button.getSlot(), button);
            inventory.setItem(button.getSlot(), button.getItemStack());
        });
        return this;
    }

    /**
     * Just add an ItemStack to the menu
     *
     * @param itemStack The ItemStack
     * @return Menu
     */
    public Menu plainAdd(ItemStack itemStack) {
        inventory.addItem(itemStack);
        return this;
    }

    public Menu plainSet(int slot, ItemStack itemStack) {
        inventory.setItem(slot, itemStack);
        return this;
    }

    public String getName() {
        return name;
    }

    public int getSlots() {
        return slots;
    }

    /**
     * Set the name of the menu
     *
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
        this.inventory = Bukkit.createInventory(null, slots, StringUtil.colorCode(name));
        buttonMap.values().forEach(button -> {
            inventory.setItem(button.getSlot(), button.getItemStack());
        });
    }

    /**
     * Set the slots of the menu
     *
     * @param slots Amount of slots
     */
    public void setSlots(int slots) {
        this.slots = slots;
        this.inventory = Bukkit.createInventory(null, slots, StringUtil.colorCode(name));
        buttonMap.values().forEach(button -> {
            inventory.setItem(button.getSlot(), button.getItemStack());
        });
    }

    /**
     * Closes this menu for all viewers
     */
    public void closeAll() {
        viewers.forEach(gamePlayer -> {
            gamePlayer.closeMenu(true);
            onClose(gamePlayer);
        });
    }

    /**
     * Handles the click of a button in the menu
     *
     * @param slot   The slot of the button
     * @param player The player that clicks the button
     * @param event  The bukkit event
     */
    public void handleClick(int slot, GamePlayer player, InventoryClickEvent event) {
        if (buttonMap.containsKey(slot)) {
            Button button = buttonMap.get(slot);
            event.setCancelled(button.isLocked());
            button.onClick(slot, player);
        }
    }

    public boolean isFull() {
        return emptySlot() <= -1;
    }

    public Menu clear() {
        buttonMap.keySet().forEach(slot -> {
            inventory.remove(slot);
        });

        buttonMap.clear();
        return this;
    }

    public Menu clearButKeep(List<Integer> slots) {
        List<Integer> rmSlots = Lists.newArrayList();
        buttonMap.keySet().forEach(slot -> {
            if (slots.contains(slot)) return;
            rmSlots.add(slot);
            inventory.remove(inventory.getItem(slot));
        });

        rmSlots.forEach(slot -> {
            buttonMap.remove(slot);
        });
        return this;
    }

    public int emptySlot() {
        return inventory.firstEmpty();
    }

    public void deleteMenu() {
        this.closeAll();
    }

    public List<GamePlayer> getViewers() {
        return viewers;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public abstract void onClose(GamePlayer player);

    public abstract void onOpen(GamePlayer player);
}
