package com.rednetty.redpractice.mechanic.player.damage;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.rednetty.redpractice.RedPractice;
import com.rednetty.redpractice.mechanic.Mechanics;
import com.rednetty.redpractice.mechanic.player.debug.DebugHandler;
import com.rednetty.redpractice.utils.items.NBTEditor;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class DamageHandler extends Mechanics implements Listener {

    @Override
    public void onEnable() {

        listener(this);

        //Used to disable the Heart Effects
        try {
            ProtocolLibrary.getProtocolManager().addPacketListener(
                    new PacketAdapter(RedPractice.getInstance(), PacketType.Play.Server.WORLD_PARTICLES) {
                        // Note that this is executed asynchronously
                        @Override
                        public void onPacketSending(PacketEvent event) {
                            if (event.getPacketType() == PacketType.Play.Server.WORLD_PARTICLES) {
                                for (EnumWrappers.Particle i : event.getPacket().getParticles().getValues()) {
                                    if (i == EnumWrappers.Particle.DAMAGE_INDICATOR) {
                                        event.setCancelled(true);
                                    }
                                    if (i == EnumWrappers.Particle.SWEEP_ATTACK) {
                                        event.setCancelled(true);
                                    }
                                }
                            }
                        }
                    });
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {

    }


    public boolean hasStat(ItemStack itemStack, String nbtStat) {
        NBTEditor nbtEditor = new NBTEditor(itemStack);
        nbtEditor.check();
        return nbtEditor.getInteger(nbtStat) > 0;
    }

    public int getStat(ItemStack itemStack, String nbtStat) {
        NBTEditor nbtEditor = new NBTEditor(itemStack);
        nbtEditor.check();
        return nbtEditor.getInteger(nbtStat);
    }

    public int getRandomDamage(ItemStack itemStack) {
        NBTEditor nbtEditor = new NBTEditor(itemStack);
        nbtEditor.check();
        if (nbtEditor.hasKey("mindmg") && nbtEditor.hasKey("maxdmg")) {
            int damage = ThreadLocalRandom.current().nextInt(nbtEditor.getInteger("maxdmg"));
            return damage;
        }
        return 1;
    }


    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        if(event.isCancelled()) {
            Bukkit.broadcastMessage("C");
        }

        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
            event.setDamage(0);
            event.setCancelled(true);
            return;
        }
        if (event.getEntity() instanceof LivingEntity) {
            if (event.getDamager() instanceof LivingEntity) {
                LivingEntity player = (LivingEntity) event.getDamager();
                LivingEntity livingEntity = (LivingEntity) event.getEntity();
                player.setNoDamageTicks(0);
                livingEntity.setNoDamageTicks(0);
                ItemStack itemStack = player.getEquipment().getItemInMainHand();
                DebugHandler debugHandler = RedPractice.getMechanicManager().getDebugHandler();
                int damage = getRandomDamage(itemStack);

                if (player instanceof Player && ((Player) player).getExp() <= 0.0F) {
                    event.setCancelled(true);
                    return;
                }

                Random random = new Random();
                //ARMOR CALCULATIONS (MOST OF THEM)
                int blockChance = random.nextInt(120) + 1;
                int dodgeChance = random.nextInt(120) + 1;
                int reflectChance = random.nextInt(120) + 1;
                int block = 0;
                int dodge = 0;
                int reflect = 0;
                int armor = 0;
                int dps = 0;
                int thorns = 0;
                if (livingEntity instanceof Player) {
                    for (ItemStack armoritem : ((Player) livingEntity).getInventory().getArmorContents()) {
                        if (hasStat(armoritem, "block")) {
                            block += getStat(armoritem, "block");
                        }
                        if (hasStat(armoritem, "dodge")) {
                            dodge += getStat(armoritem, "dodge");
                        }
                        if (hasStat(armoritem, "reflect")) {
                            reflect += getStat(armoritem, "reflect");
                        }
                        if (hasStat(armoritem, "armor")) {
                            armor += getStat(armoritem, "armor");
                        }
                        if (hasStat(armoritem, "dps")) {
                            dps += getStat(armoritem, "dps");
                        }
                        if (hasStat(armoritem, "thorns")) {
                            thorns += getStat(armoritem, "thorns");
                        }
                    }
                }
                Bukkit.broadcastMessage(block + " block><dodge " + dodge + "><reflect " + reflect);

                if (hasStat(itemStack, "accuracy")) {
                    if (block > 0) {
                        block -= getStat(itemStack, "accuracy") * block / 100;
                    }
                    if (dodge > 0) {
                        dodge -= getStat(itemStack, "accuracy") * dodge / 100;
                    }
                }

                Bukkit.broadcastMessage(block + " block><dodge " + dodge + "><reflect " + reflect);

                if (block > blockChance && block > 0) {
                    debugHandler.sendDebugMessage(DebugHandler.DebugType.BLOCK, player, livingEntity, damage);
                    livingEntity.getWorld().playSound(livingEntity.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1F, 1F);
                    event.setCancelled(true);
                    return;
                }
                if (dodge > dodgeChance && dodge > 0) {
                    debugHandler.sendDebugMessage(DebugHandler.DebugType.DODGE, player, livingEntity, damage);
                    livingEntity.getWorld().playSound(livingEntity.getLocation(), Sound.BLOCK_CLOTH_HIT, 1F, 1F);
                    livingEntity.getWorld().playEffect(livingEntity.getLocation(), Effect.EXPLOSION, 10);
                    event.setCancelled(true);
                    return;
                }
                if (reflect > reflectChance && reflect > 0) {
                    debugHandler.sendDebugMessage(DebugHandler.DebugType.REFLECT, player, livingEntity, damage);
                    player.damage(damage);
                    event.setCancelled(true);
                    return;
                }
                if (thorns > 0) {
                    livingEntity.getWorld().playEffect(livingEntity.getLocation(), Effect.STEP_SOUND, Material.LEAVES);
                    player.damage((thorns * damage / 100));
                    debugHandler.sendDebugMessage(DebugHandler.DebugType.THORNS, player, livingEntity, (thorns * damage / 100));
                }
                Bukkit.broadcastMessage(block + " block><dodge " + dodge + "><reflect " + reflect);

                int armorPen = 0;
                Location particleLocation = livingEntity.getEyeLocation();
                if (player instanceof Player) {

                    if (((Player) player).getInventory().getItemInMainHand().getType() == Material.GOLD_SPADE) { //Deals with Polearm AOE
                        for (Entity entity : livingEntity.getWorld().getNearbyEntities(livingEntity.getLocation(), 3, 3, 3)) {
                            if (entity instanceof LivingEntity) {
                                LivingEntity areaLE = (LivingEntity) entity;
                                areaLE.damage(damage);
                                areaLE.setVelocity(areaLE.getVelocity().multiply(-2).setY(+1));
                                debugHandler.sendDamageDebug(areaLE, player, damage, 0, 0);
                            }
                        }
                    }

                    //WEAPON CALCULATIONS (MOST OF THEM)
                    if (hasStat(itemStack, "vsplayers") && livingEntity instanceof Player) {
                        damage += getStat(itemStack, "vsplayers");
                    }
                    if (hasStat(itemStack, "fire")) {
                        livingEntity.setFireTicks(20);
                        damage += getStat(itemStack, "fire");
                    }
                    if (hasStat(itemStack, "ice")) {
                        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, 0, true, false));
                        livingEntity.getWorld().playEffect(particleLocation, Effect.POTION_BREAK, 10079487);
                        damage += getStat(itemStack, "ice");
                    }
                    if (hasStat(itemStack, "poison")) {
                        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 10, 0, true, false));
                        livingEntity.getWorld().playEffect(particleLocation, Effect.POTION_BREAK, 5227307);
                        damage += getStat(itemStack, "poison");
                    }
                    if (hasStat(itemStack, "pure")) {
                        damage += getStat(itemStack, "pure");
                    }
                    if (hasStat(itemStack, "lifesteal")) {
                        int healAmount = getStat(itemStack, "lifesteal") * damage / 100;
                        if ((healAmount + player.getHealth()) < player.getMaxHealth()) {
                            livingEntity.getWorld().playEffect(livingEntity.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
                            player.setHealth(player.getHealth() + healAmount);
                        }
                    }
                    if (hasStat(itemStack, "armorpen")) {
                        armorPen = getStat(itemStack, "armorpen");
                    }
                }

                //After Damage Calculation Armor Calculations
                if (hasStat(itemStack, "dps")) {
                    damage += dps * damage / 100;
                }

                int finalArmor = armor - (armorPen * armor / 100);
                if (hasStat(itemStack, "armor")) {
                    damage -= finalArmor * damage / 100;
                }
                event.setDamage(damage);
                //debugHandler.sendDamageDebug(livingEntity, player, damage, finalArmor * damage / 100, finalArmor);


            }
        }
    }


    @EventHandler
    public void onTakeDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) event.getEntity();
            livingEntity.setNoDamageTicks(0);
        }
    }
}
