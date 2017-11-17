package com.rednetty.redpractice.mechanic.player.economy;

import com.rednetty.redpractice.mechanic.Mechanics;
import org.bukkit.event.Listener;

public class EconomyHandler extends Mechanics implements Listener{

    public void onEnable() {
        listener(this);
    }

    public void onDisable() {
    }
}
