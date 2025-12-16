package com.rednetty.redpractice.mechanic.items;


import com.rednetty.redpractice.mechanic.items.itemgenerator.ItemRarity;
import org.bukkit.Material;

public enum ItemType {
    STAFF(Material.GOLD_HOE),
    POLEARM(Material.GOLD_SPADE),
    SWORD(Material.GOLD_SWORD),
    AXE(Material.GOLD_AXE),
    BOW(Material.BOW),
    HELMET(Material.GOLD_HELMET),
    CHEST(Material.GOLD_CHESTPLATE),
    LEGS(Material.GOLD_LEGGINGS),
    BOOTS(Material.GOLD_BOOTS);

    private Material material;

    ItemType(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public static ItemType fromInt(int count) {
        for(ItemType itemType : ItemType.values()) {
            if(itemType.ordinal() == count) {
                return itemType;
            }
        }
        return ItemType.SWORD;
    }
}
