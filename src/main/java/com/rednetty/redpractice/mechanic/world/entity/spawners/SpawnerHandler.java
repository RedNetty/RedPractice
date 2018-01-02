package com.rednetty.redpractice.mechanic.world.entity.spawners;

import com.rednetty.redpractice.RedPractice;
import com.rednetty.redpractice.configs.SpawnerConfig;
import com.rednetty.redpractice.mechanic.Mechanics;
import com.rednetty.redpractice.mechanic.items.ItemRandomizer;
import com.rednetty.redpractice.mechanic.items.ItemType;
import com.rednetty.redpractice.mechanic.items.itemgenerator.ItemRarity;
import com.rednetty.redpractice.utils.LocationUtils;
import com.rednetty.redpractice.utils.strings.StringUtil;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class SpawnerHandler extends Mechanics{


    private static ConcurrentHashMap<Spawner, Integer> spawnerMap = new ConcurrentHashMap<>();

    @Override
    public void onEnable() {
        SpawnerConfig.setupConfig();
        loadSpawners();

    }

    @Override
    public void onDisable() {
        saveSpawners();
    }


    public void loadSpawners() {
        for (String UUIDString : SpawnerConfig.getConfig().getKeys(false)) {
            FileConfiguration config = SpawnerConfig.getConfig();
            UUID spawnerUUID = UUID.fromString(UUIDString);
            EntityType entityType = EntityType.valueOf(config.getString(UUIDString + ".EntityType"));
            int amount = config.getInt(UUIDString + ".Amount");
            int radius = config.getInt(UUIDString + ".Radius");
            int timer = config.getInt(UUIDString + ".Timer");
            Location location = LocationUtils.fromString(config.getString(config.getString(UUIDString + ".Location")));
            spawnerMap.put(new Spawner(spawnerUUID, entityType, location, amount, radius, timer), timer);
        }

    }

    public void saveSpawners() {
        for (Spawner spawner : spawnerMap.keySet()) {
            FileConfiguration config = SpawnerConfig.getConfig();
            String UUIDString = spawner.getSpawnerUUID().toString();
            Location loc = spawner.getLocation();
            config.set(UUIDString + ".EntityType", spawner.getEntityType().name());
            config.set(UUIDString + ".Amount", spawner.getAmount());
            config.set(UUIDString + ".Radius", spawner.getRadius());
            config.set(UUIDString + ".Timer", spawner.getTimer());
            config.set(UUIDString + ".Location", loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
            SpawnerConfig.saveConfig();
        }
    }

    public void spawnMob(Location location, EntityType entityType, int tier) {
        LivingEntity livingEntity = (LivingEntity) location.getWorld().spawnEntity(location, entityType);
        String mobName = StringUtil.colorCode("&eInfernal " + livingEntity.getName());

        livingEntity.getEquipment().clear();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int bootRarity = random.nextInt(ItemRarity.values().length + 1);
        int legsRarity = random.nextInt(ItemRarity.values().length + 1);
        int chestRarity = random.nextInt(ItemRarity.values().length + 1);
        int helmetRarity = random.nextInt(ItemRarity.values().length + 1);
        int weaponType = random.nextInt(5);
        int weaponRarity = random.nextInt(ItemRarity.values().length + 1);

        livingEntity.getEquipment().setItemInMainHand(ItemRandomizer.ranomizeStats(ItemRandomizer.randomMainStat(ItemType.fromInt(weaponType), 5, ItemRarity.fromInt(weaponRarity))));
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
