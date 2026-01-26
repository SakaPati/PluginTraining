package ru.fozeton.training.Task2.Commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.scheduler.BukkitRunnable;
import ru.fozeton.training.Level2;

import static ru.fozeton.training.Task2.Commands.SetPosCommand.position;

public class SpawnSheepCommand {

    public static LiteralCommandNode<CommandSourceStack> build = Commands.literal("spawnsheep")
            .executes(ctx -> {
                CommandSender sender = ctx.getSource().getSender();

                if (!(sender instanceof Player player)) {
                    sender.sendMessage("Команду может использовать только игрок");
                    return Command.SINGLE_SUCCESS;
                } else if (position == null) {
                    sender.sendMessage("Сначала выполните команду /setpos");
                    return Command.SINGLE_SUCCESS;
                }

                World world = player.getWorld();
                Location location = player.getLocation();
                Mob sheep = world.spawn(location, Sheep.class);
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        if (!(sheep.getLocation().distanceSquared(position) < 1.0)) {
                            sheep.getPathfinder().moveTo(position);
                        } else {
                            sheep.damage(100.0);
                            this.cancel();
                        }
                    }
                }.runTaskTimer(Level2.getInstance(), 0, 20);
                return Command.SINGLE_SUCCESS;
            })
            .build();
}