package com.rednetty.redpractice.mechanic.items;


import com.rednetty.redpractice.RedPractice;
import com.rednetty.redpractice.mechanic.items.itemgenerator.Armor;
import com.rednetty.redpractice.mechanic.items.itemgenerator.ItemRarity;
import com.rednetty.redpractice.mechanic.items.itemgenerator.Weapon;
import com.rednetty.redpractice.mechanic.player.damage.DamageHandler;
import com.rednetty.redpractice.utils.items.NBTEditor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class ItemRandomizer {


    public static ItemRarity getRarity(ItemStack itemStack) {
        NBTEditor nbtEditor = new NBTEditor(itemStack).check();
        if (nbtEditor.hasKey("rarity")) {
            return ItemRarity.valueOf(nbtEditor.getString("rarity").toUpperCase());
        }
        return null;
    }

    public static ItemStack randomMainStat(ItemType item, int tier, ItemRarity itemRarity) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Material material = item.getMaterial();
        int armorHealth = 0;
        int weaponMinDamage = 0;
        int weaponMaxDamage = 0;
        int armor = 0;
        int dps = 0;
        int energy = 0;
        int hps = 0;
        int armorDpsChance = random.nextInt(2);
        int energyHPsChance = random.nextInt(2);
        if (armorDpsChance == 1) {
            armor = random.nextInt(5) + 12;
        } else {
            dps = random.nextInt(5) + 12;
        }
        if (energyHPsChance == 1) {
            energy = random.nextInt(3) + 3;
        } else {
            hps = random.nextInt(60) + 90;
        }
        switch (material) {
            case GOLD_HOE:
            case GOLD_SPADE:
                if (itemRarity.ordinal() > 0) {
                    weaponMinDamage = random.nextInt(30) + 70 * itemRarity.ordinal();
                    weaponMaxDamage = random.nextInt(60) + weaponMinDamage * itemRarity.ordinal();
                } else {
                    weaponMinDamage = random.nextInt(30) + 70;
                    weaponMaxDamage = random.nextInt(60) + weaponMinDamage;
                }
                //TODO
                break;
            case GOLD_SWORD:
                if (itemRarity.ordinal() > 0) {
                    weaponMinDamage = random.nextInt(100) + 120 * itemRarity.ordinal();
                    weaponMaxDamage = random.nextInt(140) + weaponMinDamage * itemRarity.ordinal();
                } else {
                    weaponMinDamage = random.nextInt(100) + 120;
                    weaponMaxDamage = random.nextInt(140) + weaponMinDamage;
                }
                //TODO
                break;
            case GOLD_AXE:
            case BOW:
                if (itemRarity.ordinal() > 0) {
                    weaponMinDamage = random.nextInt(130) + 120 * itemRarity.ordinal();
                    weaponMaxDamage = random.nextInt(150) + weaponMinDamage * itemRarity.ordinal();
                } else {
                    weaponMinDamage = random.nextInt(130) + 120;
                    weaponMaxDamage = random.nextInt(150) + weaponMinDamage;
                }
                break;
            case GOLD_HELMET:
            case GOLD_BOOTS:
                if (itemRarity.ordinal() > 0) {
                    armorHealth = random.nextInt(200) + 1000 * itemRarity.ordinal();
                } else {
                    armorHealth = random.nextInt(200) + 1000;
                }
                break;
            case GOLD_CHESTPLATE:
            case GOLD_LEGGINGS:
                if (itemRarity.ordinal() > 0) {
                    armorHealth = random.nextInt(500) + 1700 * itemRarity.ordinal();
                } else {
                    armorHealth = random.nextInt(500) + 1700;
                }
                break;
        }
        if (armorHealth > 0) {
            Armor armorFinal = new Armor(item.getMaterial(), armorHealth, itemRarity);
            armorFinal.setEnergy(energy);
            armorFinal.setDPS(dps);
            armorFinal.setHps(hps);
            armorFinal.setArmor(armor);
            return armorFinal.build();
        } else {
            return new Weapon(item.getMaterial(), weaponMinDamage, weaponMaxDamage, itemRarity).build();
        }
    }

    public static ItemStack ranomizeStats(ItemStack itemStack) {
        DamageHandler damageHandler = RedPractice.getMechanicManager().getDamageHandler();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int thornsChance = random.nextInt(5);
        int strChance = random.nextInt(5);
        int vitChance = random.nextInt(5);
        int intelectChance = random.nextInt(5);
        int reflectionChance = random.nextInt(5);
        int dexChance = random.nextInt(5);
        int dodgeChance = random.nextInt(5);
        int blockChance = random.nextInt(5);
        int fireChance = random.nextInt(5);
        int iceChance = random.nextInt(5);
        int poisonChance = random.nextInt(5);
        int pureChance = random.nextInt(5);
        int blindChance = random.nextInt(5);
        int lifestealChance = random.nextInt(5);
        int armorpenChance = random.nextInt(5);
        int accuracyChance = random.nextInt(5);
        int vsplayersChance = random.nextInt(5);

        switch (itemStack.getType()) {
            case GOLD_LEGGINGS:
            case GOLD_HELMET:
            case GOLD_BOOTS:
            case GOLD_CHESTPLATE:
                int thorns = 0;
                int str = 0;
                int vit = 0;
                int intelect = 0;
                int reflection = 0;
                int dex = 0;
                int dodge = 0;
                int block = 0;
                if (thornsChance == 0) {
                    thorns = random.nextInt(7) + 1;
                }
                if (strChance == 0) {
                    str = random.nextInt(300) + 1;
                }
                if (vitChance == 0) {
                    vit = random.nextInt(300) + 1;
                }
                if (dexChance == 0) {
                    dex = random.nextInt(300) + 1;
                }
                if (intelectChance == 0) {
                    intelect = random.nextInt(300) + 1;
                }
                if (reflectionChance == 0) {
                    reflection = random.nextInt(6) + 1;
                }
                if (dodgeChance == 0) {
                    dodge = random.nextInt(12) + 1;
                }
                if (blockChance == 0) {
                    block = random.nextInt(12) + 1;
                }
                Armor armor = new Armor(itemStack, damageHandler.getStat(itemStack, "health"), getRarity(itemStack));
                armor.setArmor(damageHandler.getStat(itemStack, "armor"));
                armor.setDPS(damageHandler.getStat(itemStack, "dps"));
                armor.setEnergy(damageHandler.getStat(itemStack, "energy"));
                armor.setHps(damageHandler.getStat(itemStack, "hps"));
                armor.setThorns(thorns);
                armor.setReflection(reflection);
                armor.setBlock(block);
                armor.setVit(vit);
                armor.setDex(dex);
                armor.setIntelect(intelect);
                armor.setStr(str);
                armor.setDodge(dodge);
                return armor.build();
            case GOLD_SWORD:
            case GOLD_AXE:
            case BOW:
            case GOLD_SPADE:
            case GOLD_HOE:
                int fire = 0;
                int ice = 0;
                int poison = 0;
                int pure = 0;
                int blind = 0;
                int lifesteal = 0;
                int armorpen = 0;
                int accuracy = 0;
                int vsplayers = 0;
                if (fireChance == 0) {
                    fire = random.nextInt(40) + 1;
                }
                if (iceChance == 0 && fireChance != 0) {
                    ice = random.nextInt(40) + 1;
                }
                if (poisonChance == 0 && iceChance != 0) {
                    poison = random.nextInt(40) + 1;
                }
                if (pureChance == 0) {
                    pure = random.nextInt(40) + 1;
                }
                if (blindChance == 0) {
                    blind = random.nextInt(9) + 1;
                }
                if (lifestealChance == 0) {
                    lifesteal = random.nextInt(12) + 1;
                }
                if (vsplayersChance == 0) {
                    vsplayers = random.nextInt(12) + 1;
                }
                if (armorpenChance == 0 && itemStack.getType() == Material.GOLD_AXE) {
                    armorpen = random.nextInt(12) + 1;
                }
                if (accuracyChance == 0 && itemStack.getType() == Material.GOLD_SWORD) {
                    accuracy = random.nextInt(30) + 1;
                }
                Weapon weapon = new Weapon(itemStack, damageHandler.getStat(itemStack, "mindmg"), damageHandler.getStat(itemStack, "maxdmg"), getRarity(itemStack));
                weapon.setArmorpen(armorpen);
                weapon.setLifesteal(lifesteal);
                weapon.setBlind(blind);
                weapon.setAccuracy(accuracy);
                weapon.setVsplayers(vsplayers);
                weapon.setPure(pure);
                weapon.setPoison(poison);
                weapon.setFire(fire);
                weapon.setIce(ice);
                return weapon.build();
        }
        return null;
    }
}
