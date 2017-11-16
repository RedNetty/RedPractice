package com.rednetty.redpractice.commands;

import com.rednetty.redpractice.RedPractice;
import com.rednetty.redpractice.commands.chat.GlobalCommand;
import com.rednetty.redpractice.commands.moderation.ReloadCommand;
import com.rednetty.redpractice.commands.moderation.SetRankCommand;

public class CommandManager {


    /**
     * Register your commands here they will automatically register in the main class
     */
    public static void registerCommands() {
        RedPractice.getInstance().getCommand("practicereload").setExecutor(new ReloadCommand());
        RedPractice.getInstance().getCommand("setrank").setExecutor(new SetRankCommand());
        RedPractice.getInstance().getCommand("gl").setExecutor(new GlobalCommand());
    }
}
