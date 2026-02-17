package ru.fozeton.training.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import ru.fozeton.training.minigame.BlockParty;

public class StartBlockPartyCommand {
    World world = Bukkit.getWorld("world");
    BlockParty blockParty = new BlockParty();

    @Getter
    private final LiteralCommandNode<CommandSourceStack> build = Commands.literal("start")
            .requires(src -> src.getSender().isOp())
            .then(Commands.literal("blockParty")
                    .then(Commands.literal("stop").executes(ctx -> {
                        blockParty.stop();
                        return 1;
                    }))
                    .executes(ctx -> {
                        blockParty.start();
                        return 1;
                    }))
            .then(Commands.literal("teleportPlayer")
                    .executes(ctx -> {
                        Location loc = new Location(world, 59.5, 124, -22.5);
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.teleport(loc);
                        }
                        return 1;
                    }))
            .build();


}
