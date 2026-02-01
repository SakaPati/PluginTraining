package ru.fozeton.training.Task2.WorldGuard.Commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ru.fozeton.training.Task2.WorldGuard.Events.ClickBlock;
import ru.fozeton.training.Task2.WorldGuard.RegionCuboid;
import ru.fozeton.training.Task2.WorldGuard.RegionManager;


public class RegionCommand {
    private static final FileConfiguration configData = RegionManager.config.getData();

    public static LiteralCommandNode<CommandSourceStack> build = Commands.literal("region")
            .requires(src -> src.getSender() instanceof Player)
            .then(Commands.literal("claim")
                    .then(Commands.argument("name", StringArgumentType.word())
                            .executes(ctx -> {
                                Player player = (Player) ctx.getSource().getExecutor();
                                if (player != null) {
                                    String regionName = StringArgumentType.getString(ctx, "name");
                                    Location pos1 = ClickBlock.getPos1(player.getUniqueId());
                                    Location pos2 = ClickBlock.getPos2(player.getUniqueId());

                                    if (pos1 != null && pos2 != null) {
                                        RegionCuboid region = new RegionCuboid(pos1, pos2, player.getUniqueId(), pos1.getWorld());

                                        if (RegionManager.checkInserts(region)) {
                                            player.sendMessage("§cНевозможно создать регион, он пересекается с другим регионом");
                                            return 1;
                                        } else if (configData.isConfigurationSection("Regions." + regionName)) {
                                            player.sendMessage("§cТакой регион уже существует");
                                            return 1;
                                        }

                                        RegionManager.saveRegion(pos1, pos2, player, regionName);
                                        player.sendMessage("§aРегион " + regionName + " создан!");
                                    } else player.sendMessage("§cСначала выделите территорию");
                                }
                                return 1;
                            })))
            .build();

}