package com.rednetty.redpractice.mechanic.player.bossbar;

import com.rednetty.redpractice.RedPractice;
import com.rednetty.redpractice.mechanic.Mechanics;
import com.rednetty.redpractice.mechanic.player.GamePlayer;
import com.rednetty.redpractice.utils.strings.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BarHandler extends Mechanics {

    @Override
    public void onEnable() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    updateBar(RedPractice.getMechanicManager().getPlayerHandler().getGamePlayer(player));
                }
            }
        }.runTaskTimerAsynchronously(RedPractice.getInstance(), 1L, 1L);
    }

    @Override
    public void onDisable() {

    }

    public void updateBar(GamePlayer gamePlayer) {
        BossBar bossBar = gamePlayer.getBossBar() == null ? Bukkit.createBossBar("Loading..", BarColor.PINK, BarStyle.SOLID) : gamePlayer.getBossBar();
        float barProgress = 1F / (float) (gamePlayer.getPlayer().getMaxHealth() / gamePlayer.getPlayer().getHealth());
        bossBar.setTitle(StringUtil.colorCode("&d&lHP &r&d" + (int) gamePlayer.getPlayer().getHealth() + " &d&l/ &r&d" + (int) gamePlayer.getPlayer().getMaxHealth()));
        bossBar.setProgress(barProgress);
        gamePlayer.setBossBar(bossBar);
        bossBar.addPlayer(gamePlayer.getPlayer());
        RedPractice.getMechanicManager().getPlayerHandler().updateGamePlayer(gamePlayer);
    }
}
