package com.rednetty.redpractice.commands;

import com.rednetty.redpractice.RedPractice;
import com.rednetty.redpractice.commands.chat.GlobalCommand;
import com.rednetty.redpractice.commands.moderation.*;

public class CommandManager {


    /**
     * Register your commands here they will automatically register in the main class
     */
    public void registerCommands() {
        RedPractice.getInstance().getCommand("practicereload").setExecutor(new ReloadCommand());
        RedPractice.getInstance().getCommand("setrank").setExecutor(new SetRankCommand());
        RedPractice.getInstance().getCommand("gl").setExecutor(new GlobalCommand());
        RedPractice.getInstance().getCommand("heal").setExecutor(new HealCommand());
        RedPractice.getInstance().getCommand("spawnmob").setExecutor(new SpawnMobCommand());
        RedPractice.getInstance().getCommand("randomgear").setExecutor(new RandomGearCommand());
    }
}
