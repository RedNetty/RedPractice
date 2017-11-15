package com.rednetty.redpractice.mechanic.player;

import com.rednetty.redpractice.mechanic.Mechanics;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;

public class PlayerHandler extends Mechanics implements Listener{

    /*Hashmap that stores GamePlayer instances temporarily*/
    @Getter
    private static HashMap<Player, GamePlayer> gamePlayerHashMap = new HashMap<>();


    @Override
    public void onEnable() {
        this.listener(this);
    }

    @Override
    public void onDisable() {
    }


    /*Manages player login and deals with Players data*/
    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        if(gamePlayerHashMap.containsKey(event.getPlayer())) return;

        GamePlayer gamePlayer = new GamePlayer()
    }
}
