package ru.fozeton.training.Task2.Event;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.fozeton.training.Task2.api.MenuManager;

public class InventoryClick implements Listener {
    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getView().title().equals(Component.text("Menu"))) {
            event.setCancelled(true);
            ItemStack item = event.getCurrentItem();

            if (item == null || item.getType() == Material.AIR || !item.getItemMeta().hasDisplayName()) return;

            switch (item.getType()) {
                case GOLDEN_SWORD:
                    player.setHealth(0.0);
                    break;
                case ELYTRA:
                    MenuManager.stat = !MenuManager.stat;
                    ItemMeta meta = item.getItemMeta();
                    if (meta != null) {
                        meta.displayName(Component.text(MenuManager.stat ? "Полет Включен" : "Полет Выключен"));
                    }
                    item.setItemMeta(meta);
                    player.setAllowFlight(MenuManager.stat);
                    break;
                case CHEST:
                    player.openInventory(MenuManager.secondMenu());
                    break;
            }
        }
    }

    @EventHandler
    public void onSecondMenuClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getView().title().equals(Component.text("Second Menu"))) {
            event.setCancelled(true);
            ItemStack item = event.getCurrentItem();

            if (item == null || item.getType() == Material.AIR || !item.getItemMeta().hasDisplayName()) return;

            switch (item.getType()) {
                case MAGENTA_GLAZED_TERRACOTTA:
                    player.openInventory(MenuManager.menu());
                    break;
                case CHERRY_SIGN:
                    player.chat("Всем привет!");
                    break;
            }
        }
    }
}
