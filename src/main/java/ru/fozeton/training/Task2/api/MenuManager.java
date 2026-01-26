package ru.fozeton.training.Task2.api;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class MenuManager {
    public static boolean stat = false;

    public static Inventory menu() {
        Inventory menu = Bukkit.createInventory(null, 27, Component.text("Menu"));

        ItemStack deathItem = new ItemStack(Material.GOLDEN_SWORD);
        customName(deathItem, "Умереть", "Нажмешь. Умрешь");

        ItemStack flyItem = new ItemStack(Material.ELYTRA);
        customName(flyItem, "Полет Выключен", "Позволяет летать");

        ItemStack secondMenuItem = new ItemStack(Material.CHEST);
        customName(secondMenuItem, "Второе меню", "Ну, допустим, мяу!");

        menu.setItem(11, deathItem);
        menu.setItem(13, flyItem);
        menu.setItem(15, secondMenuItem);

        return menu;
    }

    public static Inventory secondMenu() {
        Inventory secondMenu = Bukkit.createInventory(null, 27, Component.text("Second Menu"));

        ItemStack backMenu = new ItemStack(Material.MAGENTA_GLAZED_TERRACOTTA);
        customName(backMenu, "Вернутся обратно", "Возвращает обратно в основное меню");

        ItemStack wellcomeMassage = new ItemStack(Material.CHERRY_SIGN);
        customName(wellcomeMassage, "Пишет сообщение в чат", "Напишет \"Всем привет!\"");

        secondMenu.setItem(12, backMenu);
        secondMenu.setItem(14, wellcomeMassage);

        return secondMenu;
    }

    private static void customName(ItemStack item, String name, String description) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name).decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(Component.text(description).decoration(TextDecoration.ITALIC, false)));
            item.setItemMeta(meta);
        }
    }
}
