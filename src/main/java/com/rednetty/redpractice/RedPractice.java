package com.rednetty.redpractice;

import com.rednetty.redpractice.commands.CommandManager;
import com.rednetty.redpractice.mechanic.MechanicManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class RedPractice extends JavaPlugin {

    /**
     * @author Jackson(Red29)
     * This project was started on 11/14/17
     * The project is currently being worked on by: Jackson(Red29)
     */

    /**
     * Instance of Main Class
     * */
    @Getter
    private static RedPractice instance;

    /**
     * used to receive instance of the mechanicManager class.
     */
    @Getter
    private static MechanicManager mechanicManager;


    @Override
    public void onEnable() {
        instance = this; /*Sets instanceof RedPractice as this class*/
        mechanicManager = new MechanicManager();
        mechanicManager.init();
        CommandManager.registerCommands();
    }

    @Override
    public void onDisable() {
        mechanicManager.stop();


    }
}
