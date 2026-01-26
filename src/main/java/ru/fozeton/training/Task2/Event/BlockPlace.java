package ru.fozeton.training.Task2.Event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import ru.fozeton.training.Task2.PlayerScoreboard;

public class BlockPlace implements Listener {
    private final PlayerScoreboard sbManager;

    public BlockPlace(PlayerScoreboard sbManager) {
        this.sbManager = sbManager;
    }

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event){
        Player player = event.getPlayer();
        sbManager.addStat(player, PlayerScoreboard.PlayerStat.BLOCKS_PLACED);
    }
}
