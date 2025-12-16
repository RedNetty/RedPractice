package com.rednetty.redpractice.mechanic.world.entity.spawners;


import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.UUID;

public class Spawner {
    private UUID spawnerUUID;
    private EntityType entityType;
    private Location location;
    private int amount;
    private int radius;
    private int timer;

    public Spawner(UUID spawnerUUID, EntityType entityType, Location location, int amount, int radius, int timer) {
        this.spawnerUUID = spawnerUUID;
        this.entityType = entityType;
        this.location = location;
        this.amount = amount;
        this.radius = radius;
        this.timer = timer;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public UUID getSpawnerUUID() {
        return spawnerUUID;
    }

    public void setSpawnerUUID(UUID spawnerUUID) {
        this.spawnerUUID = spawnerUUID;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }
}