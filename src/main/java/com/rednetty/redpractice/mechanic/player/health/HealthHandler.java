package com.rednetty.redpractice.mechanic.player.health;

import com.google.gson.Gson;
import com.rednetty.redpractice.mechanic.Mechanics;
import com.rednetty.redpractice.utils.items.NBTEditor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class HealthHandler extends Mechanics implements Listener {

    @Override
    public void onEnable() {
        listener(this);
    }

    @Override
    public void onDisable() {
    }

    public int getMaxHealth(ItemStack[] itemStacks) {
        int health = 50;
        for (ItemStack item : itemStacks) {
            NBTEditor nbtEditor = new NBTEditor(item).check();
            if (nbtEditor.hasKey("health")) {
                health += nbtEditor.getInteger("health");
            }
        }
        return health;
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null) {
            if (event.getSlot() == 5 || event.getSlot() == 6 || event.getSlot() == 7 || event.getSlot() == 8) {
                event.getWhoClicked().setMaxHealth(getMaxHealth(event.getWhoClicked().getInventory().getArmorContents()));
            }
        }
    }


}
