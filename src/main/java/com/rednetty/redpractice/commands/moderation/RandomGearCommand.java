package com.rednetty.redpractice.commands.moderation;

import com.rednetty.redpractice.mechanic.items.ItemRandomizer;
import com.rednetty.redpractice.mechanic.items.ItemType;
import com.rednetty.redpractice.mechanic.items.itemgenerator.ItemRarity;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.ThreadLocalRandom;

public class RandomGearCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            Player player = (Player)commandSender;
            ThreadLocalRandom random = ThreadLocalRandom.current();
            int bootRarity = random.nextInt(ItemRarity.values().length);
            int legsRarity = random.nextInt(ItemRarity.values().length);
            int chestRarity = random.nextInt(ItemRarity.values().length);
            int helmetRarity = random.nextInt(ItemRarity.values().length);

            player.getInventory().addItem(ItemRandomizer.ranomizeStats(ItemRandomizer.randomMainStat(ItemType.BOOTS, 5, ItemRarity.fromInt(bootRarity))));
            player.getInventory().addItem(ItemRandomizer.ranomizeStats(ItemRandomizer.randomMainStat(ItemType.LEGS, 5, ItemRarity.fromInt(legsRarity))));
            player.getInventory().addItem(ItemRandomizer.ranomizeStats(ItemRandomizer.randomMainStat(ItemType.CHEST, 5, ItemRarity.fromInt(chestRarity))));
            player.getInventory().addItem(ItemRandomizer.ranomizeStats(ItemRandomizer.randomMainStat(ItemType.HELMET, 5, ItemRarity.fromInt(helmetRarity))));
            player.getInventory().addItem(ItemRandomizer.ranomizeStats(ItemRandomizer.randomMainStat(ItemType.SWORD, 5, ItemRarity.COMMON)));

        }
        return true;
    }
}
