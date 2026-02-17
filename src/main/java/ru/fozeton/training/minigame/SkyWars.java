package ru.fozeton.training.minigame;

import com.menuapi.Menu;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import ru.fozeton.training.Level3;

import java.util.*;

public class SkyWars {
    @Getter
    @Setter
    private static boolean running = false;
    private final World world = Level3.getInstance().skywars;
    private final Location[] startPos = new Location[] {
            new Location(world, 38.5, 55, 61.5),
            new Location(world, 9.5, 57, 70.5)
    };
    @Getter
    private final Map<UUID, Integer> money = new HashMap<>();

    public void start(List<Player> players) {
        new BukkitRunnable(){
            final Menu menu = new Menu();
            int delay = 5;

            @Override
            public void run() {
                if(delay > 0) {
                    for(Player player : players) {
                        player.showTitle(Title.title(Component.text(delay, NamedTextColor.GREEN), Component.empty()));
                    }
                    delay--;
                } else {
                    menu.create("Магазин наборов", 27);
                    menu.setItem("Воин", NamedTextColor.RED, 11, Material.GOLDEN_SWORD, player -> {
                        int price = 1000;
                        if(player != null && money.getOrDefault(player.getUniqueId(), 0) >= price) {
                            Inventory inv = player.getInventory();
                            inv.addItem(ItemStack.of(Material.GOLDEN_HELMET));
                            inv.addItem(ItemStack.of(Material.GOLDEN_CHESTPLATE));
                            inv.addItem(ItemStack.of(Material.GOLDEN_LEGGINGS));
                            inv.addItem(ItemStack.of(Material.GOLDEN_BOOTS));
                            inv.addItem(ItemStack.of(Material.GOLDEN_SWORD));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.CONDUIT_POWER, 3 * 60 * 20, 1));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 3 * 60 * 20, 1));
                            money.put(player.getUniqueId(), money.getOrDefault(player.getUniqueId(), 0) - price);
                            player.closeInventory();
                        } else if (player != null) player.sendMessage(Component.text("Недостаточно средств", NamedTextColor.RED));
                    });
                    menu.setItem("Лучник", NamedTextColor.RED, 13, Material.BOW, player -> {
                        int price = 1200;
                        if(player != null && money.getOrDefault(player.getUniqueId(), 0) >= price) {
                            Inventory inv = player.getInventory();
                            inv.addItem(ItemStack.of(Material.IRON_HELMET));
                            inv.addItem(ItemStack.of(Material.IRON_CHESTPLATE));
                            inv.addItem(ItemStack.of(Material.IRON_LEGGINGS));
                            inv.addItem(ItemStack.of(Material.IRON_BOOTS));
                            ItemStack bow = new ItemStack(Material.BOW);
                            @NonNull ItemMeta meta = bow.getItemMeta();
                            meta.addEnchant(Enchantment.POWER, 2, false);
                            meta.addEnchant(Enchantment.KNOCKBACK, 2, false);
                            bow.setItemMeta(meta);
                            inv.addItem(bow);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60 * 20, 1));
                            money.put(player.getUniqueId(), money.getOrDefault(player.getUniqueId(), 0) - price);
                            player.closeInventory();
                        } else if (player != null) player.sendMessage(Component.text("Недостаточно средств", NamedTextColor.RED));
                    });
                    menu.setItem("Ниндзя", NamedTextColor.RED, 15, Material.IRON_SWORD, player -> {
                        // Да, мне лень было думать над классом
                        int price = 2000;
                        if(player != null && money.getOrDefault(player.getUniqueId(), 0) >= price) {
                            Inventory inv = player.getInventory();
                            inv.addItem(ItemStack.of(Material.IRON_HELMET));
                            inv.addItem(ItemStack.of(Material.IRON_CHESTPLATE));
                            inv.addItem(ItemStack.of(Material.IRON_LEGGINGS));
                            inv.addItem(ItemStack.of(Material.IRON_BOOTS));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 30 * 20, 1, false));
                            money.put(player.getUniqueId(), money.getOrDefault(player.getUniqueId(), 0) - price);
                            player.closeInventory();
                        } else if (player != null) player.sendMessage(Component.text("Недостаточно средств", NamedTextColor.RED));
                    });
                    menu.setItem("Монетки", NamedTextColor.GOLD, 26, Material.GOLD_BLOCK, player -> {
                        player.sendMessage(Component.text("Текущий баланс: " + money.getOrDefault(player.getUniqueId(), 0)));
                        money.put(player.getUniqueId(), money.getOrDefault(player.getUniqueId(), 0) +100);
                    });

                    running = true;
                    for(int i = 0; i < startPos.length; i++) {
                        Location loc = startPos[i];
                        players.get(i).teleport(loc);
                        menu.open(players.get(i));
                    }
                    this.cancel();
                }
            }
        }.runTaskTimer(Level3.getInstance(), 0, 20);
    }

    public List<Player> lifePlayers() {
        List<Player> players = new ArrayList<>();
        for(Player player : world.getPlayers()) {
            if(player.getGameMode() != GameMode.SPECTATOR){
                players.add(player);
            }
        }

        return players;
    }
}
