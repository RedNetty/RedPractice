package com.rednetty.redpractice.utils.menu;

import com.rednetty.redpractice.mechanic.player.GamePlayer;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * Created by Giovanni on 11/12/2017.
 */
public abstract class Button {

    private final Menu menu;
    private final int slot;
    private final ItemStack itemStack;
    private boolean locked = true;

    public Button(@Nonnull Menu menu, int slot, @Nonnull ItemStack itemStack, boolean locked) {
        this.menu = menu;
        this.slot = slot;
        this.itemStack = itemStack;
        this.locked = locked;
    }

    /**
     * Set whether a player can grab the item out of the menu or not
     *
     * @param locked Boolean
     */
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isLocked() {
        return locked;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public int getSlot() {
        return slot;
    }

    public Menu getMenu() {
        return menu;
    }

    protected abstract void onClick(int slot, GamePlayer gamePlayer);
}
