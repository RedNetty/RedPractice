package com.rednetty.redpractice.mechanic;

import com.rednetty.redpractice.RedPractice;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;


public class Mechanics {


    /**
     * This will be called when the plugin is initialized
     * If you know your way around the SpigotAPI you will understand how this works.
     */
    public void onEnable() {
    }


    /**
     * This method will be called on plugin shutdown
     * If you know your way around the SpigotAPI you will understand how this works.
     */
    public void onDisable() {
    }

    /**
     * This will initialize when it is called
     *
     * @param listener - Listener is what is used in the SpigotAPI to Listen for Events, implement your class with a Listener and call that here.
     *                 The best place to put this would be onEnable
     */
    protected void listener(Listener listener) {
        Bukkit.getServer().getPluginManager().registerEvents(listener, RedPractice.getInstance());
    }
}
