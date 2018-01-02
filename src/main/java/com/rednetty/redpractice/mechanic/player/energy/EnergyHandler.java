package com.rednetty.redpractice.mechanic.player.energy;

import com.rednetty.redpractice.RedPractice;
import com.rednetty.redpractice.mechanic.Mechanics;
import com.rednetty.redpractice.mechanic.player.damage.DamageHandler;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ConcurrentHashMap;

public class EnergyHandler extends Mechanics implements Listener {


    public ConcurrentHashMap<Player, Integer> cooldownMap = new ConcurrentHashMap<>();


    @Override
    public void onEnable() {
        listener(this);

        new BukkitRunnable() { // Removes Energy from Running
            @Override
            public void run() {
                removePlayerEnergySprint();
            }
        }.runTaskTimer(RedPractice.getInstance(), 40, 9L);

        new BukkitRunnable() { // Regen Player Energy
            @Override
            public void run() {
                regenAllPlayerEnergy();
            }
        }.runTaskTimer(RedPractice.getInstance(), 1, 1);
    }

    @Override
    public void onDisable() {
    }

    //Used when a player attacks something
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamageEnergy(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (cooldownMap.contains(player)) {
                player.playSound(player.getLocation(), Sound.ENTITY_WOLF_PANT, 1, 1);
                player.playEffect(player.getEyeLocation(), Effect.CRIT, 10);
                event.setCancelled(true);
                return;
            }
            player.sendMessage(getPlayerTotalEnergy(player) + " NRG");
            takeEnergy(player, getEnergyUsage(player.getInventory().getItemInMainHand().getType()));
        }
    }


    //Used when a player clicks the air
    @EventHandler
    public void onEnergyUse(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_AIR) {
            Player player = event.getPlayer();
            if (cooldownMap.contains(player)) {
                player.playSound(player.getLocation(), Sound.ENTITY_WOLF_PANT, 1, 1);
                player.playEffect(player.getEyeLocation(), Effect.CRIT, 10);
                event.setCancelled(true);
            }
            player.sendMessage(getPlayerTotalEnergy(player) + " NRG");
            takeEnergy(player, getEnergyUsage(player.getInventory().getItemInMainHand().getType()) / 1.5F);
        }
    }


    /**
     * Regens player Energy for all Players online
     */
    private void regenAllPlayerEnergy() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            float playerEnergy = getPlayerTotalEnergy(player);
            //Deals with the cooldown when a player runs out of energy
            if (cooldownMap.containsKey(player)) {
                if (cooldownMap.get(player) > 0) {
                    cooldownMap.put(player, cooldownMap.get(player) - 1);
                }
                if (cooldownMap.get(player) == 0) {
                    cooldownMap.remove(player);
                }
            } else { // Deals with Energy Regen
                if (getPlayerEnergy(player) > 1F || (player.getExp() + (playerEnergy / 15)) > 1F) { //If player energy is greater than 100 just set to 100 obviously.
                    player.setExp(1.0F);
                } else if (!(getPlayerEnergy(player) == 100)) {
                    player.setExp(player.getExp() + (playerEnergy / 15));

                }
            }
            updatePlayerEnergyBar(player);
        }
    }


    /**
     * Updates the players Level Bar to match with the percentage of the Players Bar
     * @param player - Player you want to change the bar for
     */
    private void updatePlayerEnergyBar(Player player) {
        float currExp = getPlayerEnergy(player);
        double percent = currExp * 100.00D;
        if (percent > 100) {
            percent = 100;
        }
        if (percent < 0) {
            percent = 0;
        }
        player.setLevel(((int) percent));
    }


    /**
     * Removes energy for players sprinting online
     */
    private void removePlayerEnergySprint() {
        Bukkit.getOnlinePlayers().stream().filter(Player::isSprinting).forEach(player -> {
            takeEnergy(player, 0.15F);
            if (getPlayerEnergy(player) <= 0) {
                player.setSprinting(false);
            }
        });
    }


    /**
     * Takes energy from the player and updates it
     * @param player - Player you want to change it for
     * @param amount - Amount you wanna take (FLOAT 1F would be all of it)
     */
    public void takeEnergy(Player player, float amount) {
        if ((player.getExp() - amount) <= 0) {
            player.setExp(0.0F);
            updatePlayerEnergyBar(player);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 35, 1, true, false));
            cooldownMap.put(player, 35);
        } else {
            player.setExp(player.getExp() - amount);
        }
    }

    /**
     * Gets the players total energy from all his gear
     * @param player - the  player you want to get
     * @return - Returns float of players total energy
     */
    public float getPlayerTotalEnergy(Player player) {
        float energy = .10F; // base energy pretty much 15 because it would be SUPER slow otherwise (pss ingame its 5 energy just for looks)
        DamageHandler damageHandler = RedPractice.getMechanicManager().getDamageHandler();
        for (ItemStack itemStack : player.getInventory().getArmorContents()) {
            if (damageHandler.hasStat(itemStack, "energy"))
                energy += (damageHandler.getStat(itemStack, "energy") / 100.0);
            if (damageHandler.hasStat(itemStack, "int"))
                energy += (damageHandler.getStat(itemStack, "int") / 100.0);

        }
        return energy;
    }


    /**
     * Gets the players current energy
     * @param player - Players you wanna get
     * @return -  Returns the players Energy Float
     */
    public float getPlayerEnergy(Player player) {
        if (player.getExp() > 1F) {
            return 1F;
        } else {
            return player.getExp();
        }
    }

    /**
     * Gets the energy requires to use specific items
     * @param material - Material you are trying to get the energy for
     * @return - Returns the float (Energy)
     */
    public float getEnergyUsage(Material material) {
        switch (material) {
            case GOLD_SWORD:
            case BOW:
            case GOLD_HOE:
                return .08F;
            case GOLD_AXE:
                return .1F;
            case GOLD_SPADE:
                return .07F;
        }
        return .04F;
    }
}
