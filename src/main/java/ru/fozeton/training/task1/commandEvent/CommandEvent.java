package ru.fozeton.training.task1.commandEvent;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import ru.fozeton.training.Level1;

import java.util.HashMap;

import static ru.fozeton.training.task1.CommandPrac.flyList;
import static ru.fozeton.training.task1.CommandPrac.godList;

public class CommandEvent implements Listener {
    public static HashMap<Material, Location> position = new HashMap<>();
    Level1 plugin = Level1.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        godList.put(player.getName(), false);
        flyList.put(player.getName(), false);

        ItemStack diamondBlock = new ItemStack(Material.DIAMOND_BLOCK);
        ItemStack goldenBlock = new ItemStack(Material.GOLD_BLOCK);
        inventory.addItem(diamondBlock, goldenBlock);
    }

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();
        EquipmentSlot slot = event.getHand();
        ItemStack item = event.getItemInHand();

        if (block.getType() == Material.DIAMOND_BLOCK || block.getType() == Material.GOLD_BLOCK) {
            player.getInventory().setItem(slot, item);
//            position.put(block.getType(), block.getLocation());
            plugin.getTpConfig().set("blockLocation." + block.getType().name(), block.getLocation());
            plugin.saveTpConfig();
        } else return;
    }

    @EventHandler
    public void onChatEvent(AsyncChatEvent event) {
        Component message = event.originalMessage();
        String serializer = LegacyComponentSerializer.legacyAmpersand().serialize(message);
        TextComponent deserialize = LegacyComponentSerializer.legacyAmpersand().deserialize(serializer);
        event.message(deserialize);
    }

    @EventHandler
    public void onDamageEvent(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player player) {
            if (godList.get(player.getName())
                    && event.getCause() == DamageCause.ENTITY_ATTACK
                    || event.getCause() == DamageCause.LAVA
                    || event.getCause() == DamageCause.FIRE
                    || event.getCause() == DamageCause.FIRE_TICK) {
                event.setCancelled(true);
            }
        }
    }

    public static Location blockLoc(Material material) {
        return position.get(material);
    }
}
