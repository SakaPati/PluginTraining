package ru.fozeton.training.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.fozeton.training.Level3;

import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

public class StartBlockPartyCommand {
    Random random = new Random();
    World world = Bukkit.getWorld("world");

    Material[] arrColor = new Material[]{
            Material.WHITE_CONCRETE,
            Material.LIGHT_GRAY_CONCRETE,
            Material.GRAY_CONCRETE,
            Material.BLACK_CONCRETE,
            Material.BROWN_CONCRETE,
            Material.RED_CONCRETE,
            Material.ORANGE_CONCRETE,
            Material.YELLOW_CONCRETE,
            Material.LIME_CONCRETE,
            Material.GREEN_CONCRETE,
            Material.CYAN_CONCRETE,
            Material.LIGHT_BLUE_CONCRETE,
            Material.BLUE_CONCRETE,
            Material.PURPLE_CONCRETE,
            Material.MAGENTA_CONCRETE,
            Material.PINK_CONCRETE
    };

    Map<Material, Component> namedColor = Map.ofEntries(
            Map.entry(Material.WHITE_CONCRETE, Component.text("Белый", TextColor.color(0xF9FFFE))),
            Map.entry(Material.LIGHT_GRAY_CONCRETE, Component.text("Светло-серый", TextColor.color(0xD3D3D3))),
            Map.entry(Material.GRAY_CONCRETE, Component.text("Серый", TextColor.color(0x7F7F7F))),
            Map.entry(Material.BLACK_CONCRETE, Component.text("Чёрный", TextColor.color(0x1D1D21))),
            Map.entry(Material.BROWN_CONCRETE, Component.text("Коричневый", TextColor.color(0x835C3B))),
            Map.entry(Material.RED_CONCRETE, Component.text("Красный", TextColor.color(0xB02E26))),
            Map.entry(Material.ORANGE_CONCRETE, Component.text("Оранжевый", TextColor.color(0xD87F33))),
            Map.entry(Material.YELLOW_CONCRETE, Component.text("Жёлтый", TextColor.color(0xFED83D))),
            Map.entry(Material.LIME_CONCRETE, Component.text("Лаймовый", TextColor.color(0x80C71F))),
            Map.entry(Material.GREEN_CONCRETE, Component.text("Зелёный", TextColor.color(0x5E7C16))),
            Map.entry(Material.CYAN_CONCRETE, Component.text("Бирюзовый", TextColor.color(0x169C9C))),
            Map.entry(Material.LIGHT_BLUE_CONCRETE, Component.text("Голубой", TextColor.color(0x3AB3DA))),
            Map.entry(Material.BLUE_CONCRETE, Component.text("Синий", TextColor.color(0x253192))),
            Map.entry(Material.PURPLE_CONCRETE, Component.text("Фиолетовый", TextColor.color(0x7B2FBF))),
            Map.entry(Material.MAGENTA_CONCRETE, Component.text("Пурпурный", TextColor.color(0xB24CD8))),
            Map.entry(Material.PINK_CONCRETE, Component.text("Розовый", TextColor.color(0xF2A2C0)))
    );

    Material color;


    @Getter
    private final LiteralCommandNode<CommandSourceStack> build = Commands.literal("start")
            .requires(src -> src.getSender().isOp())
            .then(Commands.literal("blockParty")
                    .executes(ctx -> {
                        int x1 = 63, z1 = -26;
                        int x2 = 55, z2 = -18;

                        int minX = Math.min(x1, x2);
                        int minZ = Math.min(z1, z2);
                        int maxX = Math.max(x1, x2);
                        int maxZ = Math.max(z1, z2);

                        new BukkitRunnable() {
                            long delay = 5;

                            @Override
                            public void run() {
                                color = getRandomColor(arrColor);
                                allPlayer(player -> player.showTitle(Title.title(namedColor.get(color), Component.empty())));

                                new BukkitRunnable() {
                                    int round = 1;
                                    boolean blocksGenerating = false;

                                    @Override
                                    public void run() {
                                        if (blocksGenerating) {
                                            return;
                                        } else if (delay > 0) {
                                            allPlayer(player -> player.sendActionBar(Component.text(delay--, NamedTextColor.GREEN)));
                                        } else if (round <= 25) {
                                            blocksGenerating = true;
                                            round++;
                                            if (round <= 10) {
                                                delay = 5;
                                            } else if (round <= 20) {
                                                delay = 3;
                                            } else delay = 1;

                                            for (int x = minX; x <= maxX; x++) {
                                                for (int z = minZ; z <= maxZ; z++) {
                                                    Location blockLoc = new Location(world, x, 122, z);
                                                    Block block = blockLoc.getBlock();
                                                    if (block.getType() == color) continue;
                                                    block.setType(Material.AIR);
                                                }
                                            }

                                            new BukkitRunnable() {
                                                @Override
                                                public void run() {
                                                    for (int x = minX; x <= maxX; x++) {
                                                        for (int z = minZ; z <= maxZ; z++) {
                                                            Location blockLoc = new Location(world, x, 122, z);
                                                            Block block = blockLoc.getBlock();
                                                            block.setType(getRandomColor(arrColor));
                                                        }
                                                    }
                                                    color = getRandomColor(arrColor);
                                                    allPlayer(player -> player.showTitle(Title.title(namedColor.get(color), Component.empty())));
                                                    blocksGenerating = false;
                                                }
                                            }.runTaskLater(Level3.getInstance(), 60);


                                        } else this.cancel();
                                    }
                                }.runTaskTimer(Level3.getInstance(), 0, 20);

                                this.cancel();
                            }
                        }.runTask(Level3.getInstance());


                        return 1;
                    }))
            .then(Commands.literal("teleportPlayer")
                    .executes(ctx -> {
                        Location loc = new Location(world, 59.5, 124, -22.5);
                        allPlayer(player -> player.teleport(loc));

                        return 1;
                    }))
            .build();

    private Material getRandomColor(Material[] arrColor) {
        int randomColor = random.nextInt(arrColor.length);
        return arrColor[randomColor];
    }

    private void allPlayer(Consumer<Player> action) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            action.accept(player);
        }
    }
}
