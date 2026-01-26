package ru.fozeton.training.Task2;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerScoreboard {
    private final Map<UUID, Scoreboard> playerScoreboard = new HashMap<>();
    private final Map<UUID, Map<PlayerStat, Integer>> stats = new HashMap<>();

    public void createScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective("statistic", Criteria.DUMMY, Component.text("Ваша статистика"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Map<PlayerStat, Integer> playerStats = new HashMap<>();

        for(PlayerStat stat : PlayerStat.values()){
            playerStats.put(stat, 0);
            objective.getScore(stat.getDisplayName()).setScore(0);
        }

        player.setScoreboard(scoreboard);
        stats.put(player.getUniqueId(), playerStats);
        playerScoreboard.put(player.getUniqueId(), scoreboard);
    }

    public void addStat(Player player, PlayerStat stat) {
        UUID uuid = player.getUniqueId();
        if(!stats.containsKey(uuid)) return;
        Map<PlayerStat, Integer> playerStats = stats.get(uuid);
        int newValue = playerStats.get(stat) + 1;
        playerStats.put(stat, newValue);

        Scoreboard scoreboard = playerScoreboard.get(uuid);
        Objective objective = scoreboard.getObjective("statistic");

        if(objective == null) return;
        objective.getScore(stat.getDisplayName()).setScore(newValue);
    }

    public enum PlayerStat {
        KILLS("Убийства"),
        BLOCKS_BROKEN("Сломанные блоки"),
        BLOCKS_PLACED("Поставленные блоки"),
        DEATHS("Смерти");

        private final String displayName;

        PlayerStat(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

}
