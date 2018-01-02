package com.rednetty.redpractice.mechanic.player;

import com.rednetty.redpractice.utils.items.ItemBuilder;
import com.rednetty.redpractice.utils.menu.Button;
import com.rednetty.redpractice.utils.menu.Menu;
import com.rednetty.redpractice.utils.strings.StringUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.stream.IntStream;

public class BetaMenu extends Menu {

    BetaMenu(GamePlayer gamePlayer) {
        super(InventoryType.HOPPER, "&c&lBETA SERVER!");
        ItemStack acceptItem = new ItemBuilder(Material.INK_SACK).setDurability((short) 10).setName("&aACCEPT").setLore(Arrays.asList("&7Clicking will allow you to play.")).build();
        ItemStack infoItem = new ItemBuilder(Material.BOOK).setName("&bINFO").setLore(Arrays.asList("&7The server is in very early Alpha", "&7Crying about bugs will not help", "&7Please report any bugs you find", "&7Abusing bugs will result in a perma-ban")).build();
        ItemStack denyItem = new ItemBuilder(Material.INK_SACK).setDurability((short) 1).setName("&cDENY").setLore(Arrays.asList("&7Clicking will &l&nREMOVE&7 you from the server.")).build();

        newButton(new Button(this, 0, acceptItem, true) {
            @Override
            protected void onClick(GamePlayer gamePlayer) {
                gamePlayer.closeMenu(true);
            }
        });

        newButton(new Button(this, 4, denyItem, true) {
            @Override
            protected void onClick(GamePlayer gamePlayer) {
                gamePlayer.getPlayer().kickPlayer(StringUtil.colorCode("&c&nYou have been kicked for not accepting the Beta Bug Rules."));
            }
        });

        IntStream.range(1, 4).forEach(slot -> newButton(new Button(this, slot, infoItem, true) {
            @Override
            protected void onClick(GamePlayer gamePlayer) {
                //DO NOTHING
            }
        }));


    }


    @Override
    public void onClose(GamePlayer player) {
        Player bukkitPlayer = player.getPlayer();
        bukkitPlayer.sendMessage(StringUtil.colorCode("&aThanks for playing and accepting the Beta Rules!"));
        bukkitPlayer.playSound(bukkitPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
    }

    @Override
    public void onOpen(GamePlayer player) {
        Player bukkitPlayer = player.getPlayer();
        bukkitPlayer.playSound(bukkitPlayer.getLocation(), Sound.BLOCK_NOTE_PLING, 1F, 1F);
    }
}
