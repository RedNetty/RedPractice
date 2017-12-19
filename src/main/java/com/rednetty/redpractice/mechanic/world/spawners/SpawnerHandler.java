package com.rednetty.redpractice.mechanic.world.spawners;

import com.rednetty.redpractice.RedPractice;
import com.rednetty.redpractice.mechanic.Mechanics;
import com.rednetty.redpractice.mechanic.items.ItemRandomizer;
import com.rednetty.redpractice.mechanic.items.ItemType;
import com.rednetty.redpractice.mechanic.items.itemgenerator.ItemRarity;
import com.rednetty.redpractice.utils.strings.StringUtil;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.concurrent.ThreadLocalRandom;

public class SpawnerHandler extends Mechanics{


    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {

    }

    public void spawnMob(Location location, EntityType entityType, int tier) {
        LivingEntity livingEntity = (LivingEntity) location.getWorld().spawnEntity(location, entityType);
        String mobName = StringUtil.colorCode("&eLegendary " + livingEntity.getName());

        livingEntity.getEquipment().clear();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int bootRarity = random.nextInt(ItemRarity.values().length);
        int legsRarity = random.nextInt(ItemRarity.values().length);
        int chestRarity = random.nextInt(ItemRarity.values().length);
        int helmetRarity = random.nextInt(ItemRarity.values().length);

        livingEntity.getEquipment().setBoots(ItemRandomizer.ranomizeStats(ItemRandomizer.randomMainStat(ItemType.BOOTS, 5, ItemRarity.fromInt(bootRarity))));
        livingEntity.getEquipment().setLeggings(ItemRandomizer.ranomizeStats(ItemRandomizer.randomMainStat(ItemType.LEGS, 5, ItemRarity.fromInt(legsRarity))));
        livingEntity.getEquipment().setChestplate(ItemRandomizer.ranomizeStats(ItemRandomizer.randomMainStat(ItemType.CHEST, 5, ItemRarity.fromInt(chestRarity))));
        livingEntity.getEquipment().setHelmet(ItemRandomizer.ranomizeStats(ItemRandomizer.randomMainStat(ItemType.HELMET, 5, ItemRarity.fromInt(helmetRarity))));

        livingEntity.getEquipment().setBootsDropChance(0);
        livingEntity.getEquipment().setChestplateDropChance(0);
        livingEntity.getEquipment().setLeggingsDropChance(0);
        livingEntity.getEquipment().setHelmetDropChance(0);

        livingEntity.setMaxHealth(RedPractice.getMechanicManager().getHealthHandler().getMaxHealth(livingEntity.getEquipment().getArmorContents()) * 2.5);
        livingEntity.setHealth(livingEntity.getMaxHealth());
        livingEntity.setCustomName(mobName);
        livingEntity.setCustomNameVisible(true);
    }
}
