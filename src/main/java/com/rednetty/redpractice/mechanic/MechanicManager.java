package com.rednetty.redpractice.mechanic;

import com.rednetty.redpractice.mechanic.player.PlayerHandler;
import lombok.Getter;

import java.util.stream.Stream;

public class MechanicManager {

    @Getter
    private PlayerHandler playerHandler;


    /**
     * This is used to load all the mechanics place a mechanic here and its onEnable will be called
     * The Class must Implement the Mechanics Class
     */
    public void init() {
        Stream.of(
                playerHandler = new PlayerHandler()
        ).forEach(manager -> manager.onEnable());
    }

    /**
     * This is used to unload all the mechanics place a mechanic here and its onEnable will be called
     * The Class must Implement the Mechanics Class
     */
    public void stop() {
        Stream.of(
                playerHandler
        ).forEach(manager -> manager.onEnable());
    }
}
