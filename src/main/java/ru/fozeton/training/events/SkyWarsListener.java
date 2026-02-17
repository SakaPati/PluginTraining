package ru.fozeton.training.events;

import lombok.val;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import ru.fozeton.training.Level3;
import ru.fozeton.training.minigame.SkyWars;

import java.util.*;

public class SkyWarsListener implements Listener {
    private final Random random = new Random();
    private final List<Player> players = new ArrayList<>();
    private final Map<Location, Long> openChest = new HashMap<>();
    private final SkyWars skywars = new SkyWars();

    private final ItemStack[] items = new ItemStack[]{
            // Блоки
            new ItemStack(Material.STONE, 20),
            new ItemStack(Material.OAK_PLANKS, 15),
            new ItemStack(Material.GLASS, 10),

            // Мечи
            new ItemStack(Material.IRON_SWORD),
            new ItemStack(Material.DIAMOND_SWORD),

            // Луки и стрелы
            new ItemStack(Material.BOW),
            new ItemStack(Material.ARROW, 16),

            // Зелья
            new ItemStack(Material.POTION, 1),
            new ItemStack(Material.SPLASH_POTION, 1),
            new ItemStack(Material.LINGERING_POTION, 1),
            // Инструменты
            new ItemStack(Material.IRON_PICKAXE),
            new ItemStack(Material.IRON_AXE),
            new ItemStack(Material.WOODEN_SHOVEL),

            // Еда
            new ItemStack(Material.COOKED_BEEF, 8),
            new ItemStack(Material.APPLE, 4),
            new ItemStack(Material.BREAD, 6),

            // Броня
            new ItemStack(Material.IRON_HELMET),
            new ItemStack(Material.IRON_CHESTPLATE),
            new ItemStack(Material.IRON_LEGGINGS),
            new ItemStack(Material.IRON_BOOTS),

            // Вспомогательные предметы
            new ItemStack(Material.TNT, 2),
            new ItemStack(Material.ENDER_PEARL, 1),
            new ItemStack(Material.FIRE_CHARGE, 3),
            new ItemStack(Material.SNOWBALL, 8),
            new ItemStack(Material.EXPERIENCE_BOTTLE, 4),
            new ItemStack(Material.OAK_DOOR, 2),
            new ItemStack(Material.WATER_BUCKET, 1),
            new ItemStack(Material.ELYTRA),
            new ItemStack(Material.LAVA_BUCKET, 1),
            new ItemStack(Material.COBWEB, 2)
    };

    @EventHandler
    public void onPlayerJoin(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World formWorld = event.getFrom();
        World toWorld = player.getWorld();

        if (toWorld.getName().equals("skywars")) {
            players.add(player);
            if (players.size() == 2) skywars.start(players);
        } else if (formWorld.getName().equals("skywars")) {
            players.remove(player);
        }
    }

    @EventHandler
    public void onOpenChest(InventoryOpenEvent event) {
        if (event.getInventory().getHolder() instanceof Chest chest && skyWarsWorld(chest.getLocation())) {
            Inventory inv = chest.getInventory();
            Location loc = chest.getLocation();
            Long currentTime = System.currentTimeMillis();
            if (openChest.containsKey(loc)) {
                long time = currentTime - openChest.get(loc);
                int coolDown = 3 * 60 * 1000;
                if (time < coolDown) return;
            }

            genLoot(inv);
            openChest.put(loc, currentTime);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (skyWarsWorld(event.getBlock().getLocation())) event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();

        if (skyWarsWorld(player.getLocation())) {
            event.setCancelled(true);
            player.setGameMode(GameMode.SPECTATOR);

            if (skywars.lifePlayers().size() == 1) {
                Player winer = skywars.lifePlayers().getFirst();
                for (Player loser : players) {
                    if (loser != winer) {
                        loser.showTitle(Title.title(Component.text("Победил игрок " + winer.getName(), NamedTextColor.GREEN), Component.empty()));
                    } else {
                        val money = skywars.getMoney();
                        winer.showTitle(Title.title(Component.text("Поздравляю вы победили!", NamedTextColor.GREEN), Component.empty()));
                        winer.sendMessage(Component.text("Вы получили 100 монет", NamedTextColor.GREEN));
                        money.put(winer.getUniqueId(), money.getOrDefault(winer.getUniqueId(), 0) +100);
                    }
                }

                new BukkitRunnable() {
                    final Location loc = new Location(Bukkit.getWorld("world"), 0.5, 65, 0.5);

                    @Override
                    public void run() {
                        for (int i = 0; i <= players.size(); i++) {
                            Player player = players.getFirst();

                            player.getInventory().clear();
                            player.setGameMode(GameMode.SURVIVAL);
                            player.teleport(loc);
                        }
                        SkyWars.setRunning(false);
                        this.cancel();
                    }
                }.runTaskLater(Level3.getInstance(), 60);
            }
        }
    }

    private boolean skyWarsWorld(Location location) {
        return location.getWorld().getName().equals("skywars");
    }

    private void genLoot(Inventory inventory) {
        inventory.clear();

        for (int i = 0; i < random.nextInt(15) + 1; i++) {
            int slot = random.nextInt(inventory.getSize());
            ItemStack item = items[random.nextInt(items.length)];
            inventory.setItem(slot, item);
        }
    }
}

