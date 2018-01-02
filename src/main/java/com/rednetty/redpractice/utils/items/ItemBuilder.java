package com.rednetty.redpractice.utils.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {


    private List<String> lore;
    private String name;
    private Material material;
    private ItemStack item = null;
    private int amount = 1;
    private short durability;


    /**
     * Starts the ItemBuilder
     *
     * @param material - Requires a Bukkit Material Enum
     */
    public ItemBuilder(Material material) {
        this.material = material;
    }


    /**
     * Starts the ItemBuilder (Allows Editing of ItemStacks)
     *
     * @param itemStack - Requires a Bukkit ItemStack
     */
    public ItemBuilder(ItemStack itemStack) {
        this.item = itemStack;
    }


    /**
     * Used to add Lore to a Item
     *
     * @param lore - This takes a List<String> and allows you to add multiple lines of Lore on a Item
     * @returns - This returns the ItemBuilder so you can keep building onto the ItemBuilder
     */
    public ItemBuilder setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }


    /**
     * Used to set the Name of a item
     *
     * @param name - This takes a String and allows you to set the displayName() on a Item
     * @returns - This returns the ItemBuilder so you can keep building onto the ItemBuilder
     */
    public ItemBuilder setName(String name) {
        this.name = name;
        return this;
    }


    /**
     * Used to set the amount of a item
     *
     * @param amount - This takes a int and allows you to set the amount of the item (UP TO 64)
     * @returns - This returns the ItemBuilder so you can keep building onto the ItemBuilder
     */
    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }


    /**
     * Used to set the durability on a item
     *
     * @param durability - Takes a short to set the durability on a item
     * @returns - This returns the ItemBuilder so you can keep building onto the ItemBuilder
     */
    public ItemBuilder setDurability(short durability) {
        this.durability = durability;
        return this;
    }

    /**
     * Builds the Item after what you've decided to add/change and returns a itemStack
     *
     * @returns - Returns the Finished Item Stack
     */
    public ItemStack build() {
        ItemStack itemStack = item != null ? item : new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> coloredLore = new ArrayList<>();
        lore.forEach(s -> coloredLore.add(ChatColor.translateAlternateColorCodes('&', s)));
        itemMeta.setLore(coloredLore);
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        itemStack.setItemMeta(itemMeta);
        itemStack.setAmount(amount);
        itemStack.setDurability(durability);
        return itemStack;
    }
}
