package ru.fozeton.training.Task2.Event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import ru.fozeton.training.Task2.PlayerScoreboard;
import ru.fozeton.training.Task2.PlayerScoreboard;

public class PlayerDeath implements Listener {
    private final PlayerScoreboard sbManager;

    public PlayerDeath(PlayerScoreboard sbManager){
        this.sbManager = sbManager;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player player = event.getPlayer();
        sbManager.addStat(player, PlayerScoreboard.PlayerStat.DEATHS);
    }

}
