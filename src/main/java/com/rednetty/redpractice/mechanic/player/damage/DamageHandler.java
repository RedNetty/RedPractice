package com.rednetty.redpractice.mechanic.player.damage;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.rednetty.redpractice.RedPractice;
import com.rednetty.redpractice.mechanic.Mechanics;
import com.rednetty.redpractice.utils.items.NBTEditor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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

        if (event.isCancelled()) return;

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
                int damage = getRandomDamage(itemStack);


                //ARMOR CALCULATIONS (MOST OF THEM)
                int blockChance = ThreadLocalRandom.current().nextInt(100) + 1;
                int dodgeChance = ThreadLocalRandom.current().nextInt(100) + 1;
                int reflectChance = ThreadLocalRandom.current().nextInt(100) + 1;
                int block = 0;
                int dodge = 0;
                int reflect = 0;
                int armor = 0;
                int dps = 0;
                int thorns = 0;
                if(livingEntity instanceof Player) {
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

                if (hasStat(itemStack, "accuracy")) {
                    block -= getStat(itemStack, "accuracy") * block / 100;
                    dodge -= getStat(itemStack, "accuracy") * dodge / 100;
                }

                if (block >= blockChance) {
                    //TODO: Add Debug Message Here
                    livingEntity.getWorld().playSound(livingEntity.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1F, 1F);
                    event.setCancelled(true);
                    return;
                }
                if (dodge >= dodgeChance) {
                    //TODO: Add Debug Message here http://prntscr.com/ho6nza
                    livingEntity.getWorld().playSound(livingEntity.getLocation(), Sound.BLOCK_CLOTH_HIT, 1F, 1F);
                    livingEntity.getWorld().playEffect(livingEntity.getLocation(), Effect.EXPLOSION, 10);
                    event.setCancelled(true);
                    return;
                }
                if (reflect >= reflectChance) {
                    //TODO: Add Damage Debug Message here
                    player.damage(damage);
                    event.setCancelled(true);
                    return;
                }
                if(thorns > 0) {
                    livingEntity.getWorld().playEffect(livingEntity.getLocation(), Effect.STEP_SOUND, Material.LEAVES);
                    player.damage((thorns * damage / 100));
                }


                int armorPen = 0;
                Location particleLocation = livingEntity.getEyeLocation();
                if(player instanceof Player) {
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
                if(hasStat(itemStack, "dps")) {
                    damage += dps * damage / 100;
                }
                if (hasStat(itemStack, "armor")) {
                    //TODO: DEBUG MESSAGE FOR LE
                    int finalArmor = armor - (armorPen * armor / 100);
                    damage -= finalArmor * damage / 100;
                }


                event.setDamage(damage);
                //TODO: ADD DEBUG MESSAGE
                player.sendMessage(damage + " -> " + event.getEntity().getName() + " - HP: " + ((LivingEntity) event.getEntity()).getHealth());


            }
        }
    }


    @EventHandler
    public void onTakeDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)event.getEntity();
            livingEntity.setNoDamageTicks(0);
        }
    }
}
