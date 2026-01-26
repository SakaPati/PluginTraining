package ru.fozeton.training.Task2.Event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.fozeton.training.Task2.PlayerScoreboard;

public class PlayerJoin implements Listener {
    private final PlayerScoreboard sbManager;

    public PlayerJoin(PlayerScoreboard sbManager) {
        this.sbManager = sbManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        sbManager.createScoreboard(event.getPlayer());
    }
}
