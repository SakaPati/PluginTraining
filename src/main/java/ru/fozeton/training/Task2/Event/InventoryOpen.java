package ru.fozeton.training.Task2.Event;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class InventoryOpen implements Listener {
    private final Map<Location, Long> lastGenLoot = new HashMap<>();
    private final Random random = new Random();
    private final ItemStack[] items = {
            new ItemStack(Material.DIAMOND_BLOCK),
            new ItemStack(Material.ACACIA_LOG),
            new ItemStack(Material.ACACIA_TRAPDOOR),
            new ItemStack(Material.BRAIN_CORAL_FAN),
            new ItemStack(Material.DIAMOND_BOOTS),
            new ItemStack(Material.IRON_INGOT),
            new ItemStack(Material.GOLD_INGOT),
            new ItemStack(Material.EMERALD),
            new ItemStack(Material.NETHERITE_SCRAP),
            new ItemStack(Material.COAL),
            new ItemStack(Material.OAK_PLANKS),
            new ItemStack(Material.BIRCH_LOG),
            new ItemStack(Material.SPRUCE_STAIRS),
            new ItemStack(Material.COBBLESTONE),
            new ItemStack(Material.MOSSY_COBBLESTONE),
            new ItemStack(Material.ENCHANTING_TABLE),
            new ItemStack(Material.ANVIL),
            new ItemStack(Material.BOOKSHELF),
            new ItemStack(Material.ENDER_CHEST),
            new ItemStack(Material.CHEST),
            new ItemStack(Material.BREAD),
            new ItemStack(Material.COOKED_BEEF),
            new ItemStack(Material.GOLDEN_APPLE),
            new ItemStack(Material.CAKE),
            new ItemStack(Material.CARROT),
            new ItemStack(Material.IRON_SWORD),
            new ItemStack(Material.BOW),
            new ItemStack(Material.ARROW),
            new ItemStack(Material.SHIELD),
            new ItemStack(Material.CROSSBOW),
            new ItemStack(Material.REDSTONE),
            new ItemStack(Material.REDSTONE_TORCH),
            new ItemStack(Material.PISTON),
            new ItemStack(Material.OBSERVER),
            new ItemStack(Material.HOPPER),
            new ItemStack(Material.EXPERIENCE_BOTTLE),
            new ItemStack(Material.NAME_TAG),
            new ItemStack(Material.LEAD),
            new ItemStack(Material.TOTEM_OF_UNDYING)
    };

    @EventHandler
    public void onChestOpen(InventoryOpenEvent event) {
        if (event.getInventory().getHolder() instanceof Chest chest) {
            Inventory inventory = chest.getInventory();

            Location chestLoc = chest.getLocation();
            long currentTimeMillis = System.currentTimeMillis();

            if (lastGenLoot.containsKey(chestLoc)) {
                long time = currentTimeMillis - lastGenLoot.get(chestLoc);
                long twoMinute = 2 * 60 * 1000;
                if (time < twoMinute) return;
            }

            genLoot(inventory);
            lastGenLoot.put(chestLoc, currentTimeMillis);
        }
    }

    private void genLoot(Inventory inventory) {
        inventory.clear();

        for (int i = 0; i < random.nextInt(11); i++) {
            int slot = random.nextInt(inventory.getSize());
            ItemStack item = items[random.nextInt(items.length)];
            inventory.setItem(slot, item);
        }
    }
}