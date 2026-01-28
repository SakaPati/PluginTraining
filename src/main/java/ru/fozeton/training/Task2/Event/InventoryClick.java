package ru.fozeton.training.Task2.Event;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.fozeton.training.Level2;
import ru.fozeton.training.Task2.Commands.OpenInvCommand;
import ru.fozeton.training.Task2.api.MenuManager;

public class InventoryClick implements Listener {
    private static final FileConfiguration config = Level2.config.getData();

    // 10
    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getView().title().equals(Component.text("Menu"))) {
            event.setCancelled(true);
            ItemStack item = event.getCurrentItem();

            if (item == null || !item.getItemMeta().hasDisplayName()) return;

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

            if (item == null || !item.getItemMeta().hasDisplayName()) return;

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

    // 11
    @EventHandler
    public void onCfgMenuClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        String title = config.getString(OpenInvCommand.currentMenu + ".title");
        ConfigurationSection buttons = config.getConfigurationSection(OpenInvCommand.currentMenu + ".buttons");

        if(title != null && event.getView().title().equals(Component.text(title)) && event.getCurrentItem() != null) {
            event.setCancelled(true);
            Material type = event.getCurrentItem().getType();

            if(buttons == null) return;
            for(String key : buttons.getKeys(false)) {
                ConfigurationSection btn = buttons.getConfigurationSection(key);

                Material material = Material.matchMaterial(btn.getString("material", "stone"));
                String command = btn.getString("command");

                if(type == material && command != null) {
                    player.chat(command);
                    break;
                }
                System.out.println(btn);
            }
        }
    }
}
