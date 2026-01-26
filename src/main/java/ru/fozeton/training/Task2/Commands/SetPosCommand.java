package ru.fozeton.training.Task2.Commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetPosCommand {
    public static Location position;

    public static LiteralCommandNode<CommandSourceStack> build = Commands.literal("setpos")
            .executes(ctx -> {
                CommandSender sender = ctx.getSource().getSender();

                if (!(sender instanceof Player player)) {
                    sender.sendMessage("Команду может использовать только игрок");
                    return Command.SINGLE_SUCCESS;
                }
                position = player.getLocation();
                return Command.SINGLE_SUCCESS;
            })
            .build();
}
