package ru.fozeton.training.Task2.Commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class TabNameCommand {
    public static LiteralCommandNode<CommandSourceStack> build = Commands.literal("tabname")
            .requires(src -> src.getSender() instanceof Player)
            .then(Commands.argument("prefix", StringArgumentType.word())
                    .executes(ctx -> {
                        ctx.getSource().getSender().sendMessage("Ошибка, нужны аргументы <prefix> <suffix>");
                        return Command.SINGLE_SUCCESS;
                    })
                    .then(Commands.argument("suffix", StringArgumentType.word())
                            .executes(ctx -> {
                                Player player = (Player) ctx.getSource().getExecutor();
                                String prefix = StringArgumentType.getString(ctx, "prefix");
                                String suffix = StringArgumentType.getString(ctx, "suffix");
                                if (player == null) return Command.SINGLE_SUCCESS;

                                Component newListName = Component.text(prefix + " " + player.getName() + " " + suffix);
                                player.playerListName(newListName);

                                return Command.SINGLE_SUCCESS;
                            })
                    )
            )
            .executes(ctx -> {
                ctx.getSource().getSender().sendMessage("Ошибка, нужны аргументы <prefix> <suffix>");
                return Command.SINGLE_SUCCESS;
            })
            .build();
}
