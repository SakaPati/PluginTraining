package ru.fozeton.training.task1;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.fozeton.training.Level1;

import java.util.*;

import static io.papermc.paper.registry.RegistryKey.MOB_EFFECT;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;

public class CommandPrac {

    public static final HashMap<String, Boolean> godList = new HashMap<>();
    public static final HashMap<String, Boolean> flyList = new HashMap<>();
    public static final ArrayList<Integer> number = new ArrayList<>();

    public static LiteralCommandNode<CommandSourceStack> btpCommand = Commands.literal("btp")
            .then(Commands.literal("diamond").executes(ctx -> {
                CommandSender sender = ctx.getSource().getSender();
                Entity executor = ctx.getSource().getExecutor();

                if (!(executor instanceof Player player)) {
                    sender.sendMessage("Команду может писать только игрок");
                    return Command.SINGLE_SUCCESS;
                }

                Material type = Material.DIAMOND_BLOCK;
                Location loc = Level1.getInstance().getTpConfig().getLocation("blockLocation." + type.name());

                if (loc == null) {
                    player.sendMessage("Поставьте блок");
                    return Command.SINGLE_SUCCESS;
                }

                player.teleport(loc.add(0.5, 1, 0.5));
                return Command.SINGLE_SUCCESS;
            }))
            .then(Commands.literal("gold").executes(ctx -> {
                CommandSender sender = ctx.getSource().getSender();
                Entity executor = ctx.getSource().getExecutor();

                if (!(executor instanceof Player player)) {
                    sender.sendMessage("Команду может писать только игрок");
                    return Command.SINGLE_SUCCESS;
                }

                Material type = Material.GOLD_BLOCK;
                Location loc = Level1.getInstance().getTpConfig().getLocation("blockLocation." + type.name());
                if (loc == null) {
                    player.sendMessage("Поставьте блок");
                    return Command.SINGLE_SUCCESS;
                }
                player.teleport(loc.add(0.5, 1, 0.5));
                return Command.SINGLE_SUCCESS;
            }))
            .executes(ctx -> {
                ctx.getSource().getSender().sendMessage("Эм, ну тип надо аргументы, например: diamond, gold");
                return Command.SINGLE_SUCCESS;
            })
            .build();

    public static LiteralCommandNode<CommandSourceStack> sethealthCommand = Commands.literal("sethealth")
            .then(Commands.argument("health", DoubleArgumentType.doubleArg()).executes(ctx -> {
                CommandSender sender = ctx.getSource().getSender();
                Entity executor = ctx.getSource().getExecutor();
                double health = DoubleArgumentType.getDouble(ctx, "health");

                if (!(executor instanceof Player player)) {
                    sender.sendMessage("Команду может выполнить только игрок");
                    return Command.SINGLE_SUCCESS;
                }
                double maxHealth = player.getAttribute(Attribute.MAX_HEALTH).getValue();

                if (health <= 0 || health > maxHealth) {
                    player.sendMessage("Недопустимое значение");
                    return Command.SINGLE_SUCCESS;
                }

                player.setHealth(health);
                return Command.SINGLE_SUCCESS;
            }))
            .executes(ctx -> {
                ctx.getSource().getSender().sendMessage("Ошибка, нужен аргумент <число>");
                return Command.SINGLE_SUCCESS;
            })
            .build();

    public static LiteralCommandNode<CommandSourceStack> showtextCommand = Commands.literal("showtext")
            .then(Commands.argument("word1", StringArgumentType.word()).executes(ctx -> {
                        CommandSender sender = ctx.getSource().getSender();
                        Entity executor = ctx.getSource().getExecutor();
                        String text = StringArgumentType.getString(ctx, "word1");

                        if (!(executor instanceof Player player)) {
                            sender.sendMessage("Команду может выполнить только игрок");
                            return Command.SINGLE_SUCCESS;
                        }
                        Component titleText = Component.text(text, GOLD);
                        Component subtitleText = Component.empty();
                        Title title = Title.title(titleText, subtitleText);

                        player.showTitle(title);
                        return Command.SINGLE_SUCCESS;
                    })
                    .then(Commands.argument("word2", StringArgumentType.word()).executes(ctx -> {
                        CommandSender sender = ctx.getSource().getSender();
                        Entity executor = ctx.getSource().getExecutor();
                        String titleText = StringArgumentType.getString(ctx, "word1");
                        String subtitleText = StringArgumentType.getString(ctx, "word2");

                        if (!(executor instanceof Player player)) {
                            sender.sendMessage("Команду может выполнить только игрок");
                            return Command.SINGLE_SUCCESS;
                        }
                        Component titleComponent = Component.text(titleText, GOLD);
                        Component subtitleComponent = Component.text(subtitleText, GOLD);
                        Title title = Title.title(titleComponent, subtitleComponent);

                        player.showTitle(title);
                        return Command.SINGLE_SUCCESS;
                    })))
            .executes(ctx -> {
                ctx.getSource().getSender().sendMessage("Ошибка, нужен аргумент <слово> <слово>");
                return Command.SINGLE_SUCCESS;
            })
            .build();

    public static LiteralCommandNode<CommandSourceStack> godCommand = Commands.literal("god")
            .executes(ctx -> {
                CommandSender sender = ctx.getSource().getSender();
                Entity executor = ctx.getSource().getExecutor();
                if (!(executor instanceof Player player)) {
                    sender.sendMessage("Команду может выполнить только игрок");
                    return Command.SINGLE_SUCCESS;
                }
                Boolean enable = !(godList.get(player.getName()));
                if (enable) {
                    player.sendMessage("god Включен");
                } else player.sendMessage("god Выключен");

                godList.put(player.getName(), enable);
                return Command.SINGLE_SUCCESS;
            })
            .build();

    public static LiteralCommandNode<CommandSourceStack> flyCommand = Commands.literal("fly")
            .then(Commands.argument("player", ArgumentTypes.player()).executes(ctx -> {
                Player player = ctx.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource()).getFirst();
                Boolean enable = !(flyList.get(player.getName()));
                flyList.put(player.getName(), enable);
                player.setAllowFlight(enable);
                return Command.SINGLE_SUCCESS;
            }))
            .executes(ctx -> {
                CommandSender sender = ctx.getSource().getSender();
                Entity executor = ctx.getSource().getExecutor();
                if (!(executor instanceof Player player)) {
                    sender.sendMessage("Команду может писать только игрок!");
                    return Command.SINGLE_SUCCESS;
                }
                Boolean enable = !(flyList.get(player.getName()));
                flyList.put(player.getName(), enable);
                player.setAllowFlight(enable);
                return Command.SINGLE_SUCCESS;
            })
            .build();

    public static LiteralCommandNode<CommandSourceStack> addEffectCommand = Commands.literal("addeffect")
            .then(Commands.argument("effect", ArgumentTypes.resource(MOB_EFFECT))
                    .then(Commands.argument("time", IntegerArgumentType.integer(1))
                            .then(Commands.argument("level", IntegerArgumentType.integer(0, 255))
                                    .executes(ctx -> {
                                        PotionEffectType effectType = ctx.getArgument("effect", PotionEffectType.class);
                                        int level = ctx.getArgument("level", Integer.class);
                                        int time = ctx.getArgument("time", Integer.class) * 20;
                                        PotionEffect effect = new PotionEffect(effectType, time, level);
                                        for (Player player : Bukkit.getOnlinePlayers()) {
                                            player.addPotionEffect(effect);
                                        }
                                        return Command.SINGLE_SUCCESS;
                                    }))))
            .build();

    public static LiteralCommandNode<CommandSourceStack> getminCommand = Commands.literal("getmin")
            .then(Commands.argument("numbers", StringArgumentType.greedyString()).executes(ctx -> {
                CommandSender sender = ctx.getSource().getSender();
                String numb = StringArgumentType.getString(ctx, "numbers");
                int minNumber = Arrays.stream(numb.split(" ")).mapToInt(Integer::parseInt).min().orElseThrow();
                sender.sendMessage("Минимальное число: " + minNumber);
                return Command.SINGLE_SUCCESS;
            }))
            .executes(ctx -> {
                CommandSender sender = ctx.getSource().getSender();
                sender.sendMessage("Нужны аргументы, пример: 1 2 3 4");
                return Command.SINGLE_SUCCESS;
            })
            .build();

    public static LiteralCommandNode<CommandSourceStack> duplicatesCommand = Commands.literal("duplicates")
            .then(Commands.argument("numbers", StringArgumentType.greedyString()).executes(ctx -> {
                CommandSender sender = ctx.getSource().getSender();
                String numb = StringArgumentType.getString(ctx, "numbers");
                int[] numberArr = Arrays.stream(numb.split(" ")).mapToInt(Integer::parseInt).toArray();
                Set<Integer> set = new HashSet<>();
                List<Object> duplicates = new ArrayList<>();
                for (Integer i : numberArr) {
                    if (!set.add(i)) {
                        duplicates.add(i);
                    }
                }
                sender.sendMessage("Дублирующиеся число: " + duplicates);
                return Command.SINGLE_SUCCESS;
            }))
            .executes(ctx -> {
                CommandSender sender = ctx.getSource().getSender();
                sender.sendMessage("Нужны аргументы, пример: 1 2 3 4");
                return Command.SINGLE_SUCCESS;
            })
            .build();

    public static LiteralCommandNode<CommandSourceStack> addnumCommand = Commands.literal("addnum")
            .then(Commands.argument("number", IntegerArgumentType.integer()).executes(ctx -> {
                CommandSender sender = ctx.getSource().getSender();
                int numb = IntegerArgumentType.getInteger(ctx, "number");
//                number.add(numb);
                List<String> numbList = Level1.getInstance().getConfig().getStringList("numbers");
                numbList.add(String.valueOf(numb));
                Level1.getInstance().getConfig().set("numbers", numbList);
                Level1.getInstance().saveConfig();

                sender.sendMessage("Добавлено число: " + numb);
                return Command.SINGLE_SUCCESS;
            }))
            .executes(ctx -> {
                CommandSender sender = ctx.getSource().getSender();
                sender.sendMessage("Необходим аргумент, пример: /addnum <число>");

                return Command.SINGLE_SUCCESS;
            })
            .build();

    public static LiteralCommandNode<CommandSourceStack> getnumCommand = Commands.literal("getnum")
            .executes(ctx -> {
                CommandSender sender = ctx.getSource().getSender();
                List<String> numbList = Level1.getInstance().getConfig().getStringList("numbers");
//                if(!number.isEmpty()) {
//                    int numb = number.getFirst();
//                    number.removeFirst();
                if (numbList.getFirst() != null) {
                    String numb = numbList.getFirst();
                    numbList.removeFirst();
                    Level1.getInstance().getConfig().set("numbers", numbList);
                    Level1.getInstance().saveConfig();
                    sender.sendMessage(numb);
                } else sender.sendMessage("Числа не найдены, напишите /addnum <число>");
                return Command.SINGLE_SUCCESS;
            })
            .build();
}
