package ru.fozeton.training.Task2.Commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.fozeton.training.Level2;

import java.util.List;

public class OpenInvCommand {
    private static final FileConfiguration configData = Level2.config.getData();
    public static String currentMenu;

    public static LiteralCommandNode<CommandSourceStack> build = Commands.literal("openinv")
            .requires(src -> src.getSender() instanceof Player)
            .then(Commands.argument("menu", StringArgumentType.word())
                    .executes(ctx -> {
                        Player player = (Player) ctx.getSource().getExecutor();
                        String menuName = StringArgumentType.getString(ctx, "menu");
                        if (!configData.isConfigurationSection(menuName) || player == null) {
                            System.out.println("Не найдено меню или игрок");
                            return Command.SINGLE_SUCCESS;
                        }

                        ConfigurationSection buttons = configData.getConfigurationSection(menuName + ".buttons");

                        Inventory menu = Bukkit.createInventory(null, configData.getInt(menuName + ".size", 9), Component.text(configData.getString(menuName + ".title", "Error")));
                        currentMenu = menuName;

                        for (String key : buttons.getKeys(false)) {
                            ConfigurationSection btn = buttons.getConfigurationSection(key);

                            ConfigurationSection enchants = btn.getConfigurationSection("enchantment");

                            int slot = btn.getInt("slot");
                            Material material = Material.matchMaterial(btn.getString("material"));
                            Component name = Component.text(btn.getString("name")).decoration(TextDecoration.ITALIC, false);
                            String lore = btn.getString("lore");

                            ItemStack item = new ItemStack(material);
                            ItemMeta itemMeta = item.getItemMeta();
                            if (itemMeta != null) {
                                itemMeta.displayName(name);
                                if (lore != null) {
                                    itemMeta.lore(List.of(Component.text(lore).decoration(TextDecoration.ITALIC, false)));
                                }
                                item.setItemMeta(itemMeta);
                            }

                            if (enchants != null) {
                                for (String enchantKeys : enchants.getKeys(false)) {
                                    NamespacedKey enchantKey = NamespacedKey.fromString("minecraft:" + enchantKeys);
                                    int level = enchants.getInt(enchantKeys);

                                    Enchantment enchantment = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(enchantKey);

                                    item.addUnsafeEnchantment(enchantment, level);
                                }
                            }
                            menu.setItem(slot, item);
                        }

                        player.openInventory(menu);

                        return Command.SINGLE_SUCCESS;
                    }))
            .executes(ctx -> {
                ctx.getSource().getSender().sendMessage("Ошибка, напишите /openinv <название меню>");
                return Command.SINGLE_SUCCESS;
            })
            .build();
}
