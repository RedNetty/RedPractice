package com.rednetty.redpractice.mechanic.player.toggles;

import com.rednetty.redpractice.RedPractice;
import com.rednetty.redpractice.mechanic.player.GamePlayer;
import com.rednetty.redpractice.utils.menu.Button;
import com.rednetty.redpractice.utils.menu.Menu;
import org.bukkit.Sound;

public class ToggleMenu extends Menu {


    public ToggleMenu(GamePlayer gamePlayer) {
        super("&eToggle Menu", 9);
        ToggleHandler toggleHandler = RedPractice.getMechanicManager().getToggleHandler();
        for (ToggleType toggleType : ToggleType.values()) {
            newButton(new Button(this, this.emptySlot(), toggleHandler.getToggleItem(toggleType, gamePlayer), true) {
                @Override
                protected void onClick(int slot, GamePlayer gamePlayer) {
                    gamePlayer.getPlayer().playSound(gamePlayer.getPlayer().getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1F, 1F);
                    toggleHandler.doToggle(toggleType, gamePlayer);
                    plainSet(slot, toggleHandler.getToggleItem(toggleType, gamePlayer));
                }
            });

        }
    }

    @Override
    public void onClose(GamePlayer player) {

    }

    @Override
    public void onOpen(GamePlayer player) {

    }
}
