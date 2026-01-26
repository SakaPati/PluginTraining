package ru.fozeton.training.Task2.Event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import ru.fozeton.training.Task2.PlayerScoreboard;

public class BlockBreak implements Listener {
    private final PlayerScoreboard sbManager;

    public BlockBreak(PlayerScoreboard sbManager) {
        this.sbManager = sbManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        sbManager.addStat(player, PlayerScoreboard.PlayerStat.BLOCKS_BROKEN);
    }
}
