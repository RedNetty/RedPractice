package com.rednetty.redpractice.mechanic;

import com.rednetty.redpractice.mechanic.moderation.ModerationHandler;
import com.rednetty.redpractice.mechanic.player.PlayerHandler;
import com.rednetty.redpractice.mechanic.player.bank.BankHandler;
import com.rednetty.redpractice.mechanic.player.chat.Chat;
import lombok.Getter;

import java.util.stream.Stream;

public class MechanicManager {

    @Getter
    private PlayerHandler playerHandler;
    @Getter
    private Chat chat;
    @Getter
    private ModerationHandler moderationHandler;
    @Getter
    private BankHandler bankHandler;


    /**
     * This is used to load all the mechanics place a mechanic here and its onEnable will be called
     * The Class must Implement the Mechanics Class
     */
    public void init() {
        Stream.of(
                playerHandler = new PlayerHandler(),
                moderationHandler = new ModerationHandler(),
                bankHandler = new BankHandler(),
                chat = new Chat()
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
                chat
        ).forEach(manager -> manager.onDisable());
    }
}
