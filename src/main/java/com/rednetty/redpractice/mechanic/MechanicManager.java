package com.rednetty.redpractice.mechanic;

import com.rednetty.redpractice.mechanic.player.PlayerHandler;
import com.rednetty.redpractice.mechanic.player.bank.BankHandler;
import com.rednetty.redpractice.mechanic.player.bossbar.BarHandler;
import com.rednetty.redpractice.mechanic.player.chat.ChatHandler;
import com.rednetty.redpractice.mechanic.player.damage.DamageHandler;
import com.rednetty.redpractice.mechanic.player.economy.EconomyHandler;
import com.rednetty.redpractice.mechanic.player.health.HealthHandler;
import com.rednetty.redpractice.mechanic.server.moderation.ModerationHandler;
import com.rednetty.redpractice.mechanic.world.spawners.SpawnerHandler;

import java.util.stream.Stream;

public class MechanicManager {

    private PlayerHandler playerHandler;
    private ChatHandler chatHandler;
    private ModerationHandler moderationHandler;
    private BankHandler bankHandler;
    private EconomyHandler economyHandler;
    private BarHandler barHandler;
    private HealthHandler healthHandler;
    private DamageHandler damageHandler;
    private SpawnerHandler spawnerHandler;


    /**
     * This is used to load all the mechanics place a mechanic here and its onEnable will be called
     * The Class must Implement the Mechanics Class
     */
    public void init() {
        Stream.of(
                playerHandler = new PlayerHandler(),
                moderationHandler = new ModerationHandler(),
                bankHandler = new BankHandler(),
                chatHandler = new ChatHandler(),
                economyHandler = new EconomyHandler(),
                barHandler = new BarHandler(),
                healthHandler = new HealthHandler(),
                damageHandler = new DamageHandler(),
                spawnerHandler = new SpawnerHandler()
        ).forEach(manager -> manager.onEnable());
    }

    /**
     * This is used to unload all the mechanics place a mechanic here and its onEnable will be called
     * The Class must Implement the Mechanics Class
     */
    public void stop() {
        Stream.of(
                playerHandler,
                moderationHandler,
                bankHandler,
                chatHandler,
                economyHandler,
                barHandler,
                healthHandler,
                spawnerHandler,
                damageHandler
        ).forEach(manager -> manager.onDisable());
    }



    /**
     * Get the Handlers
     *
     * @return - Returns the Class of the Handlers
     */



    public BankHandler getBankHandler() {
        return bankHandler;
    }

    public HealthHandler getHealthHandler() {
        return healthHandler;
    }

    public ChatHandler getChatHandler() {
        return chatHandler;
    }

    public ModerationHandler getModerationHandler() {
        return moderationHandler;
    }

    public PlayerHandler getPlayerHandler() {
        return playerHandler;
    }

    public EconomyHandler getEconomyHandler() {
        return economyHandler;
    }

    public BarHandler getBarHandler() {
        return barHandler;
    }

    public DamageHandler getDamageHandler() {
        return damageHandler;
    }

    public SpawnerHandler getSpawnerHandler() {
        return spawnerHandler;
    }
}
