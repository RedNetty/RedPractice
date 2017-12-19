package com.rednetty.redpractice.events;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RankChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private CommandSender setter;
    private Player target;
    private String rank;

    public RankChangeEvent(CommandSender setter, Player target, String rank) {
        this.target = target;
        this.setter = setter;
        this.rank = rank;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Used to get the RankEnum that was just set
     *
     * @return - Returns the Rank that was set by the setter
     */
    public String getRank() {
        return rank;
    }

    /**
     * Allows you to set the rank before it is handled
     *
     * @param rank- Requires a Enum from the EnumClass RankEnum
     */
    public void setRank(String rank) {
        this.rank = rank;
    }

    /**
     * Returns the person setting the Rank of the Target
     *
     * @return - Returns instance of Player that attempted to Set the Rank
     */
    public CommandSender getSetter() {
        return setter;
    }

    /**
     * The Target would the the Person being Targeted and rank being set
     *
     * @return - Returns the player that was targeted when the event was called.
     */
    public Player getTarget() {
        return target;
    }
}