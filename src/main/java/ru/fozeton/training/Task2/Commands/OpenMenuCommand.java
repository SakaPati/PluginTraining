package ru.fozeton.training.Task2.Commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;

import static ru.fozeton.training.Task2.api.MenuManager.menu;

public class OpenMenuCommand {
    public static LiteralCommandNode<CommandSourceStack> build = Commands.literal("menu")
            .requires(src -> src.getSender() instanceof Player)
            .executes(ctx -> {
                Player player = (Player) ctx.getSource().getExecutor();
                if (player == null) return Command.SINGLE_SUCCESS;
                player.openInventory(menu());
                return Command.SINGLE_SUCCESS;
            })
            .build();
}
