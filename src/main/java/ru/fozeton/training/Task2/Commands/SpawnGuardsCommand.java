package ru.fozeton.training.Task2.Commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import ru.fozeton.training.Level2;

public class SpawnGuardsCommand {
    public static LiteralCommandNode<CommandSourceStack> build = Commands.literal("spawnguards")
            .requires(src -> src.getSender() instanceof Player)
            .executes(ctx -> {
                Player player = (Player) ctx.getSource().getExecutor();
                if (player != null) {
                    World world = player.getWorld();
                    Location[] positions = new Location[]{
                            new Location(world, player.getX() + 2, player.getY(), player.getZ() + 2),
                            new Location(world, player.getX() - 2, player.getY(), player.getZ() - 2),
                            new Location(world, player.getX() + 2, player.getY(), player.getZ() - 2),
                            new Location(world, player.getX() - 2, player.getY(), player.getZ() + 2),
                    };

                    for (int i = 0; i < positions.length; i++) {
                        int index = i;
                        Mob guardian = world.spawn(positions[index], Zombie.class);
                        guardian.setAggressive(false);
                        guardian.customName(Component.text("GUARDIAN"));

                        equip(guardian, Material.NETHERITE_HELMET, EquipmentSlot.HEAD);
                        equip(guardian, Material.NETHERITE_CHESTPLATE, EquipmentSlot.CHEST);
                        equip(guardian, Material.NETHERITE_LEGGINGS, EquipmentSlot.LEGS);
                        equip(guardian, Material.NETHERITE_BOOTS, EquipmentSlot.FEET);
                        equip(guardian, Material.NETHERITE_SWORD, EquipmentSlot.HAND);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                Location[] newPositions = new Location[]{
                                        new Location(world, player.getX() + 2, player.getY(), player.getZ() + 2),
                                        new Location(world, player.getX() - 2, player.getY(), player.getZ() - 2),
                                        new Location(world, player.getX() + 2, player.getY(), player.getZ() - 2),
                                        new Location(world, player.getX() - 2, player.getY(), player.getZ() + 2),
                                };

                                guardian.getPathfinder().moveTo(newPositions[index]);
                            }
                        }.runTaskTimer(Level2.getInstance(), 0, 5);
                    }
                }

                return 1;
            })
            .build();

    public static void equip(Mob mob, Material material, EquipmentSlot slot) {
        EntityEquipment equipment = mob.getEquipment();
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            if (item.getType() == Material.NETHERITE_SWORD) {
                meta.addEnchant(Enchantment.SHARPNESS, 5, true);
                meta.addEnchant(Enchantment.MENDING, 1, false);
            } else {
                meta.addEnchant(Enchantment.UNBREAKING, 5, true);
                meta.addEnchant(Enchantment.PROTECTION, 5, true);
                meta.addEnchant(Enchantment.MENDING, 1, false);
            }
            item.setItemMeta(meta);
        }
        equipment.setItem(slot, item);
    }
}