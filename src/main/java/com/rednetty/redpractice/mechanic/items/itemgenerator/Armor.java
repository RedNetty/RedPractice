package com.rednetty.redpractice.mechanic.items.itemgenerator;

import com.rednetty.redpractice.utils.items.NBTEditor;
import com.rednetty.redpractice.utils.strings.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Armor {

    private ItemStack itemStack;
    private String itemName;
    private ItemRarity itemRarity;
    private int armor = 0;
    private int DPS = 0;
    private int health = 0;
    private int energy = 0;
    private int hps = 0;
    private int thorns = 0;
    private int str = 0;
    private int vit = 0;
    private int intelect = 0;
    private int reflection = 0;
    private int dex = 0;
    private int dodge = 0;
    private int block = 0;


    public Armor(ItemStack itemStack, int health, ItemRarity itemRarity) {
        this.itemRarity = itemRarity;
        this.itemStack = itemStack;
        this.health = health;
    }

    public Armor(Material material, int health, ItemRarity itemRarity) {
        this.itemRarity = itemRarity;
        this.itemStack = new ItemStack(material);
        this.health = health;
    }

    public Armor generateBaseName() {
        switch (itemStack.getType()) {
            case GOLD_BOOTS:
                itemName = "&eLegendary Platemail Boots";
                break;
            case GOLD_LEGGINGS:
                itemName = "&eLegendary Platemail Leggings";
                break;
            case GOLD_CHESTPLATE:
                itemName = "&eLegendary Platemail of Fortitude";
                break;
            case GOLD_HELMET:
                itemName = "&eLegendary Full Helmet of Fortitude";
                break;
        }
        return this;
    }


    public void storeStats() {
        NBTEditor nbtEditor = new NBTEditor(itemStack);
        nbtEditor.check();
        nbtEditor.setInt("armor", armor);
        nbtEditor.setInt("dps", DPS);
        nbtEditor.setInt("health", health);
        nbtEditor.setInt("hps", hps);
        nbtEditor.setInt("energy", energy);
        nbtEditor.setInt("thorns", thorns);
        nbtEditor.setInt("reflect", reflection);
        nbtEditor.setInt("vit", vit);
        nbtEditor.setInt("str", str);
        nbtEditor.setInt("dex", dex);
        nbtEditor.setInt("int", intelect);
        nbtEditor.setInt("dodge", dodge);
        Bukkit.broadcastMessage(dodge + " DODGE");
        nbtEditor.setInt("block", block);
        Bukkit.broadcastMessage(block + " bloke");
        nbtEditor.setString("rarity", itemRarity.name());
        itemStack = nbtEditor.update();
    }

    public List<String> addStats() {
        List<String> loreList = new ArrayList<>();
        if (armor > 0) {
            loreList.add("&cARMOR: " + armor + " - " + armor + "%");
        }
        if (DPS > 0) {
            loreList.add("&cDPS: " + DPS + " - " + DPS + "%");
        }
        loreList.add("&cHP: +" + health);
        if (energy > 0) {
            loreList.add("&cENERGY REGEN: +" + energy + "%");
        }
        if (hps > 0) {
            loreList.add("&cHP REGEN: +" + hps + " HP/s");
        }
        if(thorns > 0) {
            loreList.add("&cTHORNS: " + thorns + "% DMG");
        }
        if(vit > 0) {
            loreList.add("&cVIT: +" + vit);
        }
        if(str > 0) {
            loreList.add("&cSTR: +" + str);
        }
        if(intelect > 0) {
            loreList.add("&cINT: +" + intelect);
        }
        if(dex > 0) {
            loreList.add("&cDEX: +" + dex);
        }
        if(dodge > 0) {
            loreList.add("&cDODGE: " + dodge + "%");
        }
        if(block > 0) {
            loreList.add("&cBLOCK: " + block + "%");
        }
        loreList.add(itemRarity.getName()); //THIS NEEDS TO BE LAST!
        return loreList.stream().map(StringUtil::colorCode).collect(Collectors.toList());
    }

    public ItemStack build() {
        generateBaseName();
        storeStats();
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(addStats());
        meta.setDisplayName(StringUtil.colorCode(itemName));
        for (ItemFlag itemFlag : meta.getItemFlags()) {
            meta.removeItemFlags(itemFlag);
        }
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(meta);
        return itemStack;
    }


    public void setReflection(int reflection) {
        this.reflection = reflection;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public void setDPS(int DPS) {
        this.DPS = DPS;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void setHps(int hps) {
        this.hps = hps;
    }

    public void setThorns(int thorns) {
        this.thorns = thorns;
    }

    public void setStr(int str) {
        this.str = str;
    }

    public void setVit(int vit) {
        this.vit = vit;
    }

    public void setIntelect(int intelect) {
        this.intelect = intelect;
    }

    public void setDex(int dex) {
        this.dex = dex;
    }

    public void setDodge(int dodge) {
        this.dodge = dodge;
    }

    public void setBlock(int block) {
        this.block = block;
    }
}
