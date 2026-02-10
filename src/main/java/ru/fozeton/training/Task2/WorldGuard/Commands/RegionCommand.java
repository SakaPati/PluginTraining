package ru.fozeton.training.Task2.WorldGuard.Commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ru.fozeton.training.Task2.WorldGuard.Events.ClickBlock;
import ru.fozeton.training.Task2.WorldGuard.RegionCuboid;
import ru.fozeton.training.Task2.WorldGuard.RegionManager;

import java.util.List;
import java.util.UUID;


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
            .then(Commands.literal("addMember")
                    .then(Commands.argument("region", StringArgumentType.word())
                            .then(Commands.argument("member", ArgumentTypes.player())
                                    .executes(ctx -> addParty(ctx, "members")))))
            .then(Commands.literal("addOfficer")
                    .then(Commands.argument("region", StringArgumentType.word())
                            .then(Commands.argument("member", ArgumentTypes.player())
                                    .executes(ctx -> addParty(ctx, "officers")))))
            .then(Commands.literal("removeMember")
                    .then(Commands.argument("region", StringArgumentType.word())
                            .then(Commands.argument("member", ArgumentTypes.player())
                                    .executes(ctx -> removeParty(ctx, "members")))))
            .then(Commands.literal("removeOfficer")
                    .then(Commands.argument("region", StringArgumentType.word())
                            .then(Commands.argument("member", ArgumentTypes.player())
                                    .executes(ctx -> removeParty(ctx, "officers")))))
            .then(Commands.literal("flag")
                    .then(Commands.argument("region", StringArgumentType.word())
                            .then(Commands.literal("PvP")
                                    .then(Commands.argument("permission", BoolArgumentType.bool())
                                            .executes(ctx -> {
                                                Player player = (Player) ctx.getSource().getExecutor();
                                                String regionName = StringArgumentType.getString(ctx, "region");
                                                String ownerUUID = configData.getString("Regions." + regionName + ".owner");

                                                if (player != null && ownerUUID != null) {
                                                    if (!isOwner(player, regionName)) {
                                                        player.sendMessage("§cВы не владелец региона");
                                                        return 1;
                                                    } else if (!configData.isConfigurationSection("Regions." + regionName)) {
                                                        player.sendMessage("§cРегион не найден");
                                                        return 1;
                                                    }
                                                    boolean permission = BoolArgumentType.getBool(ctx, "permission");
                                                    configData.set("Regions." + regionName + ".flags.pvp", permission);
                                                    RegionManager.config.saveData();
                                                    player.sendMessage("§aФлаг PvP изменен на " + permission);
                                                }
                                                return 1;
                                            })))
                            .then(Commands.literal("walking")
                                    .then(Commands.literal("all")
                                            .then(Commands.argument("access", BoolArgumentType.bool())
                                                    .executes(ctx -> editFlags(ctx, "Walking", "all"))))
                                    .then(Commands.literal("members")
                                            .then(Commands.argument("access", BoolArgumentType.bool())
                                                    .executes(ctx -> editFlags(ctx, "Walking", "members"))))
                                    .then(Commands.literal("member")
                                            .then(Commands.argument("access", BoolArgumentType.bool())
                                                    .executes(ctx -> editFlags(ctx, "Walking", "member"))))
                                    .then(Commands.literal("officer")
                                            .then(Commands.argument("access", BoolArgumentType.bool())
                                                    .executes(ctx -> editFlags(ctx, "Walking", "officer"))))
                            )
                            .then(Commands.literal("breaking")
                                    .then(Commands.literal("all")
                                            .then(Commands.argument("access", BoolArgumentType.bool())
                                                    .executes(ctx -> editFlags(ctx, "Breaking", "all"))))
                                    .then(Commands.literal("members")
                                            .then(Commands.argument("access", BoolArgumentType.bool())
                                                    .executes(ctx -> editFlags(ctx, "Breaking", "members"))))
                                    .then(Commands.literal("member")
                                            .then(Commands.argument("access", BoolArgumentType.bool())
                                                    .executes(ctx -> editFlags(ctx, "Breaking", "member"))))
                                    .then(Commands.literal("officer")
                                            .then(Commands.argument("access", BoolArgumentType.bool())
                                                    .executes(ctx -> editFlags(ctx, "Breaking", "officer"))))
                            )
                            .then(Commands.literal("placed")
                                    .then(Commands.literal("all")
                                            .then(Commands.argument("access", BoolArgumentType.bool())
                                                    .executes(ctx -> editFlags(ctx, "Placed", "all"))))
                                    .then(Commands.literal("members")
                                            .then(Commands.argument("access", BoolArgumentType.bool())
                                                    .executes(ctx -> editFlags(ctx, "Placed", "members"))))
                                    .then(Commands.literal("member")
                                            .then(Commands.argument("access", BoolArgumentType.bool())
                                                    .executes(ctx -> editFlags(ctx, "Placed", "member"))))
                                    .then(Commands.literal("officer")
                                            .then(Commands.argument("access", BoolArgumentType.bool())
                                                    .executes(ctx -> editFlags(ctx, "Placed", "officer"))))
                            )
                            .then(Commands.literal("interaction")
                                    .then(Commands.literal("all")
                                            .then(Commands.argument("access", BoolArgumentType.bool())
                                                    .executes(ctx -> editFlags(ctx, "Interaction", "all"))))
                                    .then(Commands.literal("members")
                                            .then(Commands.argument("access", BoolArgumentType.bool())
                                                    .executes(ctx -> editFlags(ctx, "Interaction", "members"))))
                                    .then(Commands.literal("member")
                                            .then(Commands.argument("access", BoolArgumentType.bool())
                                                    .executes(ctx -> editFlags(ctx, "Interaction", "member"))))
                                    .then(Commands.literal("officer")
                                            .then(Commands.argument("access", BoolArgumentType.bool())
                                                    .executes(ctx -> editFlags(ctx, "Interaction", "officer"))))
                            )
                    )
            )
            .build();

    private static int addParty(CommandContext<CommandSourceStack> ctx, String party) throws CommandSyntaxException {
        Player player = (Player) ctx.getSource().getExecutor();
        String regionName = StringArgumentType.getString(ctx, "region");
        String ownerUUID = configData.getString("Regions." + regionName + ".owner");

        PlayerSelectorArgumentResolver memberName = ctx.getArgument("member", PlayerSelectorArgumentResolver.class);
        Player member = memberName.resolve(ctx.getSource()).getFirst();
        List<String> members = configData.getStringList("Regions." + regionName + "." + party);


        if (player != null && ownerUUID != null) {
            if (!isOwner(player, regionName)) {
                player.sendMessage("§cВы не владелец региона");
                return 1;
            } else if (!configData.isConfigurationSection("Regions." + regionName)) {
                player.sendMessage("§cРегион не найден");
                return 1;
            } else if (member == null) {
                player.sendMessage("§cИгрок не найден");
                return 1;
            } else if (members.contains(member.getUniqueId().toString())) {
                player.sendMessage("§cЭтот игрок уже является участником региона");
                return 1;
            } else if (member.getUniqueId().equals(UUID.fromString(ownerUUID))) {
                player.sendMessage("§cВы владелец региона");
                return 1;
            }
            members.add(member.getUniqueId().toString());

            configData.set("Regions." + regionName + "." + party, members);
            RegionManager.config.saveData();
            player.sendMessage("§aИгрок " + member.getName() + " добавлен в регион!");
        }
        return 1;
    }

    private static int removeParty(CommandContext<CommandSourceStack> ctx, String party) throws CommandSyntaxException {
        Player player = (Player) ctx.getSource().getExecutor();
        String regionName = StringArgumentType.getString(ctx, "region");
        String ownerUUID = configData.getString("Regions." + regionName + ".owner");

        PlayerSelectorArgumentResolver memberName = ctx.getArgument("member", PlayerSelectorArgumentResolver.class);
        Player member = memberName.resolve(ctx.getSource()).getFirst();
        List<String> members = configData.getStringList("Regions." + regionName + "." + party);

        if (player != null && ownerUUID != null) {
            if (isOwner(player, regionName) && members.contains(member.getUniqueId().toString())) {
                members.remove(member.getUniqueId().toString());
                configData.set("Regions." + regionName + "." + party, members);
                RegionManager.config.saveData();
                player.sendMessage("§cИгрок " + member.getName() + " удален из региона!");
                return 1;
            }
            player.sendMessage("§cИгрок не найден");
        }
        return 1;
    }

    private static int editFlags(CommandContext<CommandSourceStack> ctx, String flag, String group) {
        Player player = (Player) ctx.getSource().getExecutor();
        String regionName = StringArgumentType.getString(ctx, "region");
        boolean access = BoolArgumentType.getBool(ctx, "access");

        if (player != null) {
            if (isOwner(player, regionName)) {
                configData.set("Regions." + regionName + ".flags." + flag.toLowerCase() + "." + group, access);
                RegionManager.config.saveData();
                player.sendMessage("§aФлаг " + flag + " изменен на " + group + " " + access);
                return 1;
            }
            player.sendMessage("§cНе удалось изменить флаг региона");
        }

        return 1;
    }

    private static boolean isOwner(Player player, String regionName) {
        String ownerUUID = configData.getString("Regions." + regionName + ".owner");

        if (player != null && ownerUUID != null) return player.getUniqueId().equals(UUID.fromString(ownerUUID));

        return false;
    }
}