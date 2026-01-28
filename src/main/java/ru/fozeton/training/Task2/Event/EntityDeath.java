package ru.fozeton.training.Task2.Event;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.fozeton.training.Level2;
import ru.fozeton.training.Task2.PlayerScoreboard;

import java.util.List;

public class EntityDeath implements Listener {
    private static final FileConfiguration config = Level2.configBoss.getData();
    private final PlayerScoreboard sbManager;

    public EntityDeath(PlayerScoreboard sbManager) {
        this.sbManager = sbManager;
    }

    @EventHandler
    public void onPlayerDeath(EntityDeathEvent event){
        Player player = event.getEntity().getKiller();
        if(player == null) return;
        sbManager.addStat(player, PlayerScoreboard.PlayerStat.KILLS);
    }

    @EventHandler
    public void onBossDeath(EntityDeathEvent event){
        String entityName = event.getEntity().getName();
        if(event.getEntity() instanceof Zombie && config.isConfigurationSection(entityName)){
            ConfigurationSection bossDrops = config.getConfigurationSection(entityName + ".drops");
            List<ItemStack> drop = event.getDrops();
            drop.clear();
            if(bossDrops != null) {
                for(String bossDrop : bossDrops.getKeys(false)) {
                    String dropType = bossDrops.getString(bossDrop + ".material");
                    String dropName = bossDrops.getString(bossDrop + ".name");
                    String dropLore = bossDrops.getString(bossDrop + ".lore");
                    ConfigurationSection dropEnchantments = bossDrops.getConfigurationSection(bossDrop + ".enchantments");

                    if(dropType != null && dropName != null) {
                        NamespacedKey key = NamespacedKey.minecraft(dropType);
                        Material material = Registry.MATERIAL.getOrThrow(key);
                        ItemStack item = new ItemStack(material);
                        ItemMeta meta = item.getItemMeta();
                        if(meta != null) {
                            meta.displayName(Component.text(dropName));
                            if(dropLore != null) meta.lore(List.of(Component.text(dropLore)));
                            item.setItemMeta(meta);
                        }
                        if(dropEnchantments != null) {
                            for (String dropEnchantment : dropEnchantments.getKeys(false)) {
                                NamespacedKey enchantmentKey = NamespacedKey.minecraft(dropEnchantment);
                                int level = dropEnchantments.getInt(dropEnchantment);
                                Enchantment enchantment = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(enchantmentKey);
                                if (enchantment != null) item.addUnsafeEnchantment(enchantment, level);
                            }
                        }
                        drop.add(item);
                    }
                }
            }
        }
    }
}