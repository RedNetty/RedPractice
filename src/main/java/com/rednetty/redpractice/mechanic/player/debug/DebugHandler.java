package com.rednetty.redpractice.mechanic.player.debug;

import com.rednetty.redpractice.RedPractice;
import com.rednetty.redpractice.mechanic.Mechanics;
import com.rednetty.redpractice.utils.strings.StringUtil;
import jdk.internal.jline.internal.Nullable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DebugHandler extends Mechanics {

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }


    public enum DebugType {
        BLOCK,
        DODGE,
        REFLECT,
        THORNS,
        FIRE,
        POISON
    }
    /**
     * Used to show the players their damage and damage taken
     *
     * @param reciever - Person Taking Damage
     * @param damager  - Person dealing the Damage
     * @param damage   - Damage Amount
     */
    public void sendDamageDebug(LivingEntity reciever, LivingEntity damager, int damage, int armorDamage, int armor) {
        String name = reciever.getName();
        if (reciever instanceof Player) {
            name = StringUtil.colorCode("&c" + reciever.getName());
            reciever.sendMessage(StringUtil.colorCode("   &c-18 &lHP &7[-" + armor + "%A -> -" + armorDamage + "&lDMG&7] &a[" + (int)reciever.getHealth() + "&lHP&a]"));
        }
        damager.sendMessage(StringUtil.colorCode("   &c" + damage + " &lDMG&c -> " + name + " &c[" + (int)reciever.getHealth() + "&c&lHP&c]"));

    }

    /**
     * Send a Debug message to a specific Player
     * @param debugType - Debug Type that you want to send
     * @param damager - Damager givin (Just set to Null if there is none)
     * @param reciever - Reciever of the Damage (Just set to Null if there is none)
     * @param stat - Damage/Thorns/ETC
     */
    public void sendDebugMessage(DebugType debugType, LivingEntity damager, LivingEntity reciever, int stat) {
        switch(debugType) {
            case BLOCK:
                StringUtil.sendCenteredMessage(damager, StringUtil.colorCode("&c&l*OPPONENT BLOCKED* (" + reciever.getName() + ")"));
            case DODGE:
                StringUtil.sendCenteredMessage(damager, StringUtil.colorCode("&c&l*OPPONENT DODGED* (" + reciever.getName() + ")"));
            case FIRE:
                StringUtil.sendCenteredMessage(reciever, StringUtil.colorCode("&c&l*ON FIRE*"));
            case POISON:
                StringUtil.sendCenteredMessage(reciever, StringUtil.colorCode("&2&l*POISONED*"));
            case THORNS:
                StringUtil.sendCenteredMessage(damager, StringUtil.colorCode("&c-" + stat + " &lHP &a&l(THORNS) &a[" + (int)damager.getHealth() + "&a&lHP&a]"));
            case REFLECT:
                StringUtil.sendCenteredMessage(damager, StringUtil.colorCode("&6&l*OPPONENT REFLECTED* (" + reciever.getName() + ")"));
        }
    }
}