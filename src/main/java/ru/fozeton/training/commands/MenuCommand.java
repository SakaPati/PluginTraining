package ru.fozeton.training.commands;

import com.menuapi.Menu;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import ru.fozeton.training.Level3;

public class MenuCommand {
    @Getter
    private final LiteralCommandNode<CommandSourceStack> build = Commands.literal("menu")
            .requires(src -> src.getSender() instanceof Player)
            .executes(ctx -> {

                World world = Level3.getInstance().getServer().createWorld(new WorldCreator("SkyWars"));
                Location loc = new Location(world, 100, 100, 100);
                Player player = (Player) ctx.getSource().getExecutor();
                Menu menu = new Menu();
                menu.create("Menu", 27);
                menu.setItem("SkyWars", NamedTextColor.GOLD, 11, Material.IRON_SWORD, executor -> {
                    executor.teleport(loc);
                });
                if(player != null){
                    menu.open(player);
                }

                return 1;
            })
            .build();
}
