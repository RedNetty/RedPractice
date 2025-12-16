package com.rednetty.redpractice.mechanic.items.itemgenerator;

import com.rednetty.redpractice.utils.items.NBTEditor;
import com.rednetty.redpractice.utils.strings.StringUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Weapon {

    private ItemStack itemStack;
    private ItemRarity itemRarity;
    private String itemName;
    private int mindmg = 0;
    private int maxdmg = 0;
    private int fire = 0;
    private int ice = 0;
    private int poison = 0;
    private int pure = 0;
    private int blind = 0;
    private int lifesteal = 0;
    private int armorpen = 0;
    private int accuracy = 0;
    private int vsplayers = 0;

    public Weapon(ItemStack itemStack, int mindmg, int maxdmg, ItemRarity itemRarity) {
        this.itemRarity = itemRarity;
        this.itemStack = itemStack;
        this.mindmg = mindmg;
        this.maxdmg = maxdmg;
    }

    public Weapon(Material material, int mindmg, int maxdmg, ItemRarity itemRarity) {
        this.itemRarity = itemRarity;
        this.itemStack = new ItemStack(material);
        this.mindmg = mindmg;
        this.maxdmg = maxdmg;
    }

    public Weapon generateBaseName() {
        switch (itemStack.getType()) {
            case GOLD_SWORD:
                itemName = "&eLegendary Sword";
                break;
            case GOLD_AXE:
                itemName = "&eLegendary Axe";
                break;
            case GOLD_SPADE:
                itemName = "&eLegendary Polearm";
                break;
            case GOLD_HOE:
                itemName = "&eLegendary Staff";
                break;
        }
        return this;
    }

    public void storeStats() {
        NBTEditor nbtEditor = new NBTEditor(itemStack);
        nbtEditor.check();

        nbtEditor.setInt("mindmg", mindmg);
        nbtEditor.setInt("maxdmg", maxdmg);
        nbtEditor.setInt("fire", fire);
        nbtEditor.setInt("ice", ice);
        nbtEditor.setInt("poison", poison);
        nbtEditor.setInt("pure", pure);
        nbtEditor.setInt("blind", blind);
        nbtEditor.setInt("lifesteal", lifesteal);
        nbtEditor.setInt("vsplayers", vsplayers);
        nbtEditor.setInt("armorpen", armorpen);
        nbtEditor.setInt("accuracy", accuracy);
        nbtEditor.setString("rarity", itemRarity.name());
        itemStack = nbtEditor.update();
    }

    public List<String> addStats() {
        List<String> loreList = new ArrayList<>();

        loreList.add("&cDMG: " + mindmg + " - " + maxdmg);

        if(fire > 0) {
            loreList.add("&cFIRE DMG: +" + fire);
        }
        if(poison > 0) {
            loreList.add("&cPOISON DMG: +" + poison);
        }
        if(ice > 0) {
            loreList.add("&cICE DMG: +" + ice);
        }
        if(pure > 0) {
            loreList.add("&cPURE DMG: +" + pure);
        }
        if(blind > 0) {
            loreList.add("&cBLIND: " + blind + "%");
        }
        if(lifesteal > 0) {
            loreList.add("&cLIFE STEAL: " + lifesteal + "%");
        }
        if(vsplayers > 0) {
            loreList.add("&cvs. PLAYERS: +" + vsplayers + "%");
        }
        if(armorpen > 0) {
            loreList.add("&cARMOR PENETRATION: " + armorpen + "%");
        }
        if(accuracy > 0) {
            loreList.add("&cACCURACY: " + accuracy + "%");
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

    public void setFire(int fire) {
        this.fire = fire;
    }

    public void setIce(int ice) {
        this.ice = ice;
    }

    public void setPoison(int poison) {
        this.poison = poison;
    }

    public void setPure(int pure) {
        this.pure = pure;
    }

    public void setBlind(int blind) {
        this.blind = blind;
    }

    public void setLifesteal(int lifesteal) {
        this.lifesteal = lifesteal;
    }

    public void setArmorpen(int armorpen) {
        this.armorpen = armorpen;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public void setVsplayers(int vsplayers) {
        this.vsplayers = vsplayers;
    }
}
