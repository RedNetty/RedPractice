package com.rednetty.redpractice.mechanic.player.toggles;

import com.rednetty.redpractice.RedPractice;
import com.rednetty.redpractice.mechanic.Mechanics;
import com.rednetty.redpractice.mechanic.player.GamePlayer;
import com.rednetty.redpractice.mechanic.player.PlayerHandler;
import com.rednetty.redpractice.utils.items.ItemBuilder;
import com.rednetty.redpractice.utils.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ToggleHandler extends Mechanics {


    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }


    public void doToggle(ToggleType toggleType, GamePlayer gamePlayer) {
        ToggleHandler toggleHandler = RedPractice.getMechanicManager().getToggleHandler();
        PlayerHandler playerHandler = RedPractice.getMechanicManager().getPlayerHandler();
        List<ToggleType> togglesList = new ArrayList<>();
        togglesList.addAll(gamePlayer.getToggleList());
        if (toggleHandler.isEnabled(toggleType, gamePlayer)) {
            togglesList.remove(toggleType);
        } else {
            togglesList.add(toggleType);
        }
        gamePlayer.setToggleList(togglesList);
        playerHandler.updateGamePlayer(gamePlayer);
    }

    public ItemStack getToggleItem(ToggleType toggleType, GamePlayer gamePlayer) {
        if (isEnabled(toggleType, gamePlayer)) {
            return new ItemBuilder(Material.INK_SACK).setDurability((short) 10).setName("&e" + toggleType.getDisplayName() + " &7 - &a&lENABLED").setLore(Arrays.asList("&7Click to &c&lDISABLE")).build();
        } else {
            return new ItemBuilder(Material.INK_SACK).setDurability((short) 1).setName("&e" + toggleType.getDisplayName() + " &7 - &c&lDISABLED").setLore(Arrays.asList("&7Click to &alENABLE")).build();
        }
    }

    public boolean isEnabled(ToggleType toggleType, GamePlayer gamePlayer) {
        if (gamePlayer.getToggleList().contains(toggleType)) {
            return true;
        } else {
            return false;
        }
    }
}
