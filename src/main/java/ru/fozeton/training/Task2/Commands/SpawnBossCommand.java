package ru.fozeton.training.Task2.Commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.fozeton.training.Level2;

public class SpawnBossCommand {
    private static final FileConfiguration config = Level2.configBoss.getData();

    public static LiteralCommandNode<CommandSourceStack> build = Commands.literal("spawnboss")
            .requires(src -> src.getSender() instanceof Player)
            .then(Commands.argument("boss", StringArgumentType.word())
                    .executes(ctx -> {
                        Player player = (Player) ctx.getSource().getExecutor();
                        if (player == null) return 1;

                        String boss = StringArgumentType.getString(ctx, "boss");
                        World world = player.getWorld();
                        Location location = player.getLocation();
                        String bossType = config.getString(boss + ".mobType");
                        String bossName = config.getString(boss + ".name");
                        int bossHealth = config.getInt(boss + ".health");
                        ConfigurationSection bossEffects = config.getConfigurationSection(boss + ".effects");
                        ConfigurationSection bossEquipments = config.getConfigurationSection(boss + ".equipments");

                        try {
                            LivingEntity entity = (LivingEntity) world.spawnEntity(location, EntityType.valueOf(bossType));
                            AttributeInstance maxHealth = entity.getAttribute(Attribute.MAX_HEALTH);

                            if (bossName != null && maxHealth != null && bossEffects != null && bossEquipments != null) {
                                entity.setCustomNameVisible(true);
                                entity.customName(Component.text(bossName));
                                maxHealth.setBaseValue(bossHealth);
                                entity.setHealth(bossHealth);

                                for (String bossEffect : bossEffects.getKeys(false)) {
                                    NamespacedKey key = NamespacedKey.minecraft(bossEffect);
                                    PotionEffectType effectType = Registry.POTION_EFFECT_TYPE.get(key);
                                    int duration = bossEffects.getInt(bossEffect + ".duration") * 20;
                                    int level = bossEffects.getInt(bossEffect + ".level");
                                    if (effectType == null) throw new IllegalArgumentException("Эффект не найден");

                                    PotionEffect effect = new PotionEffect(effectType, duration, level);
                                    entity.addPotionEffect(effect);
                                }

                                for (String bossEquipment : bossEquipments.getKeys(false)) {
                                    String equipmentType = bossEquipments.getString(bossEquipment);
                                    if(equipmentType != null) {
                                        NamespacedKey key = NamespacedKey.minecraft(equipmentType);
                                        Material equipmentMaterial = Registry.MATERIAL.get(key);
                                        if(equipmentMaterial != null) {
                                            ItemStack item = new ItemStack(equipmentMaterial);
                                            if (entity.getEquipment() != null) entity.getEquipment().setItem(EquipmentSlot.valueOf(bossEquipment), item);
                                        }
                                    }
                                }
                            }
                        } catch (IllegalArgumentException e) {
                            throw new IllegalArgumentException(e);
                        }

                        return 1;
                    }))
            .executes(ctx -> {
                ctx.getSource().getSender().sendMessage("Ошибка, напишите /spawnboss <Имя босса>");
                return 1;
            })
            .build();
}
