package com.rednetty.redpractice.mechanic.items.itemgenerator;

public enum ItemRarity {
    COMMON("&7&oCommon"),
    UNCOMMON("&a&oUncommon"),
    RARE("&b&oRare"),
    UNIQUE("&e&oUnique");

    String name;

    ItemRarity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public static ItemRarity fromInt(int count) {
        for(ItemRarity itemRarity : ItemRarity.values()) {
            if(itemRarity.ordinal() == count) {
                return itemRarity;
            }
        }
        return ItemRarity.COMMON;
    }
}
