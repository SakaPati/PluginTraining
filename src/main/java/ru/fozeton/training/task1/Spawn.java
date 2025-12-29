package ru.fozeton.training.task1;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import ru.fozeton.training.Level1;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;

public class Spawn implements Listener {
    private final Random random = new Random();
    private final Component customName = text("Adrian", GOLD);
    private final HashMap<Location, Player> playerPlaceBlock = new HashMap<>();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        if (!(playerPlaceBlock.get(block.getLocation()) == player)) {
            event.setCancelled(true);
            return;
        }
        playerPlaceBlock.remove(block.getLocation());

        if (block.getType() == Material.MYCELIUM || block.getType() == Material.DIRT) {
            event.setDropItems(false);
            ItemStack mycelium = new ItemStack(Material.MYCELIUM);
            World world = block.getWorld();
            Location location = block.getLocation();
            world.dropItemNaturally(location, mycelium);
        }
        World world = player.getWorld();

        if (block.getType() == Material.GRASS_BLOCK || block.getType() == Material.DIRT) {
            new BukkitRunnable() {
                byte sec = 5;

                @Override
                public void run() {
                    if (sec > 0) {
                        player.sendMessage("Зомби появится через: " + sec--);
                    } else {
                        Vector vec = player.getLocation().getDirection();
                        Location location = player.getLocation().add(vec.multiply(2.0));

                        if (!location.getBlock().isSolid() && location.add(0, -1, 0).getBlock().getType().isAir()) {
                            for (int y = location.getBlock().getY(); y >= 0; y--) {
                                location.add(0, -1, 0);
                                if (location.getBlock().isSolid()) {
                                    location.add(0, 1, 0);
                                    break;
                                }
                            }
                        } else {
                            for (int y = location.getBlock().getY(); y <= 255; y++) {
                                location.add(0, 1, 0);
                                if (location.clone().set(location.getBlockX(), y - 1, location.getBlockZ()).getBlock().isSolid() && location.getBlock().getType().isAir()) {
                                    break;
                                }
                            }
                        }

                        byte distance = 1;
                        Location center = location.clone();
                        Location left = center.clone().add(vec.clone().crossProduct(new Vector(0, -distance, 0)));
                        Location right = center.clone().add(vec.clone().crossProduct(new Vector(0, distance, 0)));

                        Zombie zombie = (Zombie) world.spawnEntity(left, EntityType.ZOMBIE);
                        zombieSetting(zombie, "Adrian");

                        Zombie zombie2 = (Zombie) world.spawnEntity(right, EntityType.ZOMBIE);
                        zombieSetting(zombie2, "Sofi");

                        sec = 5;
                        this.cancel();
                    }
                }
            }.runTaskTimer(Level1.getInstance(), 0, 20);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!(event.getBlockPlaced().getType() == Material.DIRT || event.getBlockPlaced().getType() == Material.MYCELIUM)) {
            event.setCancelled(true);
        }
        Location block = event.getBlockPlaced().getLocation();
        Player player = event.getPlayer();
        playerPlaceBlock.put(block, player);
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity damagedEntity = event.getEntity();

        if (!(damagedEntity instanceof Zombie zombie)) {
            return;
        }
        Entity damagerEntity = event.getDamager();
        if (!(damagerEntity instanceof Player player)) {
            return;
        }
        if (zombie.customName() != null && Objects.equals(zombie.customName(), customName)) {
            byte health = (byte) (zombie.getHealth() - event.getFinalDamage());
            player.sendMessage("Осталось: " + Math.max(health, 0));

        } else if (zombie.customName() != null) {
            PotionEffect[] effectTypes = {
                    new PotionEffect(PotionEffectType.SPEED, 100, 1),
                    new PotionEffect(PotionEffectType.REGENERATION, 100, 1),
                    new PotionEffect(PotionEffectType.NIGHT_VISION, 100, 1)
            };
            int randomEffectType = random.nextInt(effectTypes.length);
            PotionEffect randomEffect = effectTypes[randomEffectType];

            player.addPotionEffect(randomEffect);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity.getType() == EntityType.ZOMBIE && entity.customName() != null) {
            event.getDrops().clear();
            ItemStack dirt = new ItemStack(Material.DIRT);

            event.getDrops().add(dirt);
        }
    }

    private void zombieSetting(Zombie zombie, String text) {
        zombie.setCustomNameVisible(true);
        zombie.customName(text(text, GOLD));
        zombie.getEquipment().setHelmet(leather(Material.LEATHER_HELMET, Color.PURPLE));
        zombie.getEquipment().setChestplate(leather(Material.LEATHER_CHESTPLATE, Color.YELLOW));
        zombie.getEquipment().setLeggings(leather(Material.LEATHER_LEGGINGS, Color.YELLOW));
        zombie.getEquipment().setBoots(leather(Material.LEATHER_BOOTS, Color.PURPLE));
    }

    private ItemStack leather(Material material, Color color) {
        ItemStack item = new ItemStack(material);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(color);
        item.setItemMeta(meta);
        return item;
    }
}