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
import org.bukkit.entity.Spider;
import org.bukkit.scheduler.BukkitRunnable;
import ru.fozeton.training.Level2;

import static ru.fozeton.training.Task2.Constants.FriendMobs;

public class SpawnSpiderCommand {
    public static LiteralCommandNode<CommandSourceStack> build = Commands.literal("spawnspider")
            .executes(ctx -> {
                CommandSender sender = ctx.getSource().getSender();

                if (!(sender instanceof Player player)) {
                    sender.sendMessage("Команду может использовать только игрок");
                    return Command.SINGLE_SUCCESS;
                }

                World world = player.getWorld();
                Mob spider = world.spawn(player.getLocation(), Spider.class);
                FriendMobs.put(spider.getUniqueId(), player.getUniqueId());

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Location location = player.getLocation();

                        if(!(spider.getLocation().distanceSquared(location) < 2.0)) {
                            spider.getPathfinder().moveTo(location);
                        }
                    }
                }.runTaskTimer(Level2.getInstance(), 0, 20);

                return Command.SINGLE_SUCCESS;
            })
            .build();
}
