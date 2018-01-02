package com.rednetty.redpractice.mechanic.server.menu;

import com.rednetty.redpractice.RedPractice;
import com.rednetty.redpractice.mechanic.Mechanics;
import com.rednetty.redpractice.mechanic.player.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class MenuHandler extends Mechanics implements Listener {

    @Override
    public void onEnable() {
        listener(this);
    }

    @Override
    public void onDisable() {

    }


    @EventHandler
    public void onInteract(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        GamePlayer gamePlayer = RedPractice.getMechanicManager().getPlayerHandler().getGamePlayer(player);
        if (!gamePlayer.viewingMenu()) return;
        gamePlayer.getOpenMenu().handleClick(event.getRawSlot(), gamePlayer, event);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        GamePlayer gamePlayer = RedPractice.getMechanicManager().getPlayerHandler().getGamePlayer(player);
        if (gamePlayer == null) return;
        if (!gamePlayer.viewingMenu()) return;
        gamePlayer.closeMenu(false);
    }
}
