package com.rednetty.redpractice;

import com.rednetty.redpractice.commands.CommandManager;
import com.rednetty.redpractice.mechanic.MechanicManager;
import org.bukkit.plugin.java.JavaPlugin;

public class RedPractice extends JavaPlugin {

    /**
     * @author Jackson(Red29)
     * This project was started on 11/14/17
     * The project is currently being worked on by: Jackson(Red29)
     */

    /**
     * Instance of Main Class
     */
    private static RedPractice instance;

    /**
     * used to receive instance of the mechanicManager class.
     */
    private static MechanicManager mechanicManager;


    private static CommandManager commandManager;

    public static RedPractice getInstance() {
        return instance;
    }

    public static MechanicManager getMechanicManager() {
        return mechanicManager;
    }

    public static CommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public void onEnable() {
        instance = this; /*Sets instanceof RedPractice as this class*/
        mechanicManager = new MechanicManager();
        commandManager = new CommandManager();
        mechanicManager.init();
        commandManager.registerCommands();

        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }

    @Override
    public void onDisable() {
        mechanicManager.stop();


    }
}
