package com.rednetty.redpractice.utils.items;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NBTEditor {
    private net.minecraft.server.v1_12_R1.ItemStack itemStack;

    public NBTEditor(ItemStack itemStack) {
        this.itemStack = CraftItemStack.asNMSCopy(itemStack);
    }

    /**
     * Check if the itemstack has an NBTTagCompound and creates a new one if not.
     * <p>
     * Must be called before usage!
     *
     * @return The future NBTAccesor.
     */
    public NBTEditor check() {
        if(this.itemStack != null ) {
            if (this.itemStack.hasTag()) return this;

            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            this.itemStack.setTag(nbtTagCompound);
        }
        return this;
    }

    public void remove(String key) {
        if(this.itemStack != null ) {
            this.itemStack.getTag().remove(key);
        }
    }

    /**
     * Check if the itemstack has an NBTTagCompound.
     *
     * @return Does it?
     */
    public boolean hasTag() {
        if(this.itemStack != null ) {
            return this.itemStack.hasTag();

        }
        return false;
    }

    /**
     * Check whether the NBTTagCompound of the itemstack has a specific key.
     *
     * @param key The key to check.
     * @return Probably I guess?
     */
    public boolean hasKey(String key) {
        if(this.itemStack != null ) {
            return this.itemStack.getTag().hasKey(key);
        }
        return false;
    }

    /**
     * Check whether the NBTTagCompound of the itemstack has a specific value at a specific key.
     *
     * @param key The key to check.
     * @return Probably I guess?
     */
    public boolean hasValue(String key, Object value) {
        if(this.itemStack != null ) {
            if (!this.hasKey(key)) return false;

            return this.itemStack.getTag().get(key) == value;
        }
        return false;
    }

    /**
     * Set an NBT String at a key.
     *
     * @param key   The key.
     * @param value The value.
     * @return New NBTEditor.
     */
    public NBTEditor setString(String key, String value) {
        if(this.itemStack != null ) {
            this.itemStack.getTag().setString(key, value);
        }
        return this;
    }

    /**
     * Set an NBT Integer at a key.
     *
     * @param key   The key.
     * @param value The value.
     * @return New NBTEditor.
     */
    public NBTEditor setInt(String key, int value) {
        if(this.itemStack != null ) {
            this.itemStack.getTag().setInt(key, value);
        }

        return this;
    }

    /**
     * Set an NBT Double at a key.
     *
     * @param key   The key.
     * @param value The value.
     * @return New NBTEditor.
     */
    public NBTEditor setDouble(String key, double value) {
        if(this.itemStack != null ) {
            this.itemStack.getTag().setDouble(key, value);
        }

        return this;
    }

    /**
     * Get a String value from a key.
     *
     * @param key The key.
     * @return The value.
     */
    public String getString(String key) {

        if(this.itemStack != null ) {
            return this.itemStack.getTag().getString(key);
        }
        return "";
    }

    /**
     * Get an Integer value from a key.
     *
     * @param key The key.
     * @return The value.
     */
    public int getInteger(String key) {
        if(this.itemStack != null ) {
            return this.itemStack.getTag().getInt(key);
        }
        return 0;
    }

    /**
     * Updates the itemstack and applies all changes.
     *
     * @return The new itemstack.
     */
    public ItemStack update() {

        if(this.itemStack != null ) {
            return CraftItemStack.asBukkitCopy(this.itemStack);
        }
        return null;
    }
}
