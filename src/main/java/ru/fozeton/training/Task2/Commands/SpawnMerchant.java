package ru.fozeton.training.Task2.Commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SpawnMerchant {
    public static LiteralCommandNode<CommandSourceStack> build = Commands.literal("spawnmerchant")
            .requires(sender -> sender.getSender().isOp())
            .executes(ctx -> {
                CommandSender sender = ctx.getSource().getSender();

                if (!(sender instanceof Player player)) {
                    sender.sendMessage("Команду может выполнить только игрок");
                    return Command.SINGLE_SUCCESS;
                } else if (!player.getName().equals("Fozeton")) {
                    player.sendMessage("Вы не избранный");
                    return Command.SINGLE_SUCCESS;
                }

                Location location = player.getLocation();
                World world = location.getWorld();

                Villager villager = (Villager) world.spawnEntity(location, EntityType.VILLAGER);

                villager.setInvulnerable(true);
                villager.setAI(false);

                List<MerchantRecipe> recipes = new ArrayList<>();

                ItemStack sellSword = new ItemStack(Material.GOLDEN_SWORD);
                ItemStack sellSnowBall = new ItemStack(Material.SNOWBALL, 16);

                ItemMeta swordMeta = sellSword.getItemMeta();
                swordMeta.displayName(Component.text("Просто меч, ничего более").color(NamedTextColor.GOLD));
                sellSword.setItemMeta(swordMeta);

                MerchantRecipe swordRecipe = new MerchantRecipe(sellSword, Integer.MAX_VALUE);
                swordRecipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 4));
                recipes.add(swordRecipe);

                MerchantRecipe snowBallRecipe = new MerchantRecipe(sellSnowBall, 2);
                snowBallRecipe.addIngredient(new ItemStack(Material.DIRT, 1));
                recipes.add(snowBallRecipe);

                villager.setRecipes(recipes);

                return Command.SINGLE_SUCCESS;
            })
            .build();
}
