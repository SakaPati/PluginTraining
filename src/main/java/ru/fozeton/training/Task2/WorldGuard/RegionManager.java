package ru.fozeton.training.Task2.WorldGuard;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import ru.fozeton.training.DataYML;
import ru.fozeton.training.Level2;

import java.util.List;
import java.util.UUID;

public class RegionManager {
    public static final DataYML config = Level2.configRegion;

    public static void saveRegion(Location pos1, Location pos2, Player player, String regionName) {
        String path = "Regions." + regionName;

        config.getData().set(path + ".owner", player.getUniqueId().toString());
        config.getData().set(path + ".officers", List.of());
        config.getData().set(path + ".members", List.of());

        config.getData().set(path + ".flags.pvp", false);
        config.getData().set(path + ".flags.walking.members", true);
        config.getData().set(path + ".flags.walking.member", false);
        config.getData().set(path + ".flags.walking.officer", false);
        config.getData().set(path + ".flags.walking.all", false);

        config.getData().set(path + ".flags.breaking.members", true);
        config.getData().set(path + ".flags.breaking.member", false);
        config.getData().set(path + ".flags.breaking.officer", false);
        config.getData().set(path + ".flags.breaking.all", false);

        config.getData().set(path + ".flags.placed.members", true);
        config.getData().set(path + ".flags.placed.member", false);
        config.getData().set(path + ".flags.placed.officer", false);
        config.getData().set(path + ".flags.placed.all", false);

        config.getData().set(path + ".flags.interaction.members", true);
        config.getData().set(path + ".flags.interaction.member", false);
        config.getData().set(path + ".flags.interaction.officer", false);
        config.getData().set(path + ".flags.interaction.all", false);

        config.getData().set(path + ".world", player.getWorld().getName());
        config.getData().set(path + ".min.x", pos1.getX());
        config.getData().set(path + ".min.y", pos1.getY());
        config.getData().set(path + ".min.z", pos1.getZ());
        config.getData().set(path + ".max.x", pos2.getX());
        config.getData().set(path + ".max.y", pos2.getY());
        config.getData().set(path + ".max.z", pos2.getZ());

        config.saveData();
    }

    public static boolean hasFlagPermission(Location loc, Player player, String flag) {
        boolean members = config.getData().getBoolean("Regions." + getRegionName(loc) + ".flags."+ flag + ".members");
        boolean member = config.getData().getBoolean("Regions." + getRegionName(loc) + ".flags."+ flag + ".member");
        boolean officer = config.getData().getBoolean("Regions." + getRegionName(loc) + ".flags."+ flag + ".officer");
        boolean all = config.getData().getBoolean("Regions." + getRegionName(loc) + ".flags."+ flag + ".all");
        return isParty(loc, player) && members || isMember(loc, player) && member || isOfficer(loc, player) && officer || all;
    }

    public static boolean canPvP(Location locationDefender, Player playerDefender, Location locationAttacker, Player playerAttacker) {
        boolean attackerDefender = checkRegion(locationAttacker, playerDefender);
        boolean defenderAttacker = checkRegion(locationDefender, playerAttacker);
        return attackerDefender || defenderAttacker;
    }

    public static boolean checkRegion(Location location, Player player) {
        RegionCuboid cuboid = getCuboid(location);
        return cuboid != null && cuboid.isInside(location) && !cuboid.getOwnerUUID().equals(player.getUniqueId());
    }

    public static boolean PvPDeny(Location location){
        String regionName = getRegionName(location);
        if(regionName == null) return false;
        return !config.getData().getBoolean("Regions." + regionName + ".flags.pvp");
    }

    public static boolean checkInserts(RegionCuboid newRegion) {
        ConfigurationSection regions = config.getData().getConfigurationSection("Regions");
        if (regions == null) return false;

        for (String regionName : regions.getKeys(false)) {
            ConfigurationSection region = regions.getConfigurationSection(regionName);
            if (region == null) return false;
            String world = region.getString("world");
            String ownerUUID = region.getString("owner");
            if (world == null || !world.equals(newRegion.getWorld().getName()) || ownerUUID == null) return false;
            Location minPos = new Location(newRegion.getWorld(), region.getDouble("min.x"), region.getDouble("min.y"), region.getDouble("min.z"));
            Location maxPos = new Location(newRegion.getWorld(), region.getDouble("max.x"), region.getDouble("max.y"), region.getDouble("max.z"));
            RegionCuboid rg = new RegionCuboid(minPos, maxPos, UUID.fromString(ownerUUID), newRegion.getWorld());
            if (newRegion.intersects(rg)) return true;
        }
        return false;
    }

    public static boolean isParty(Location location, Player player) {
        List<String> membersParty = config.getData().getStringList("Regions." + getRegionName(location) + ".members");
        List<String> officersParty = config.getData().getStringList("Regions." + getRegionName(location) + ".officers");
        return membersParty.contains(player.getUniqueId().toString()) || officersParty.contains(player.getUniqueId().toString());
    }

    public static boolean isMember(Location location, Player player) {
        List<String> membersParty = config.getData().getStringList("Regions." + getRegionName(location) + ".members");
        return membersParty.contains(player.getUniqueId().toString());
    }

    public static boolean isOfficer(Location location, Player player) {
        List<String> officersParty = config.getData().getStringList("Regions." + getRegionName(location) + ".officers");
        return officersParty.contains(player.getUniqueId().toString());
    }

    public static RegionCuboid getCuboid(Location location) {
        ConfigurationSection regions = RegionManager.config.getData().getConfigurationSection("Regions");

        if (location != null && regions != null) {
            for (String regionName : regions.getKeys(false)) {
                ConfigurationSection region = regions.getConfigurationSection(regionName);
                if (region != null) {
                    String ownerUUID = region.getString("owner");
                    String worldType = region.getString("world");
                    ConfigurationSection min = region.getConfigurationSection("min");
                    ConfigurationSection max = region.getConfigurationSection("max");

                    if (min != null && max != null && ownerUUID != null && worldType != null) {
                        World world = Bukkit.getWorld(worldType);
                        Location minPos = new Location(location.getWorld(), min.getDouble("x"), min.getDouble("y"), min.getDouble("z"));
                        Location maxPos = new Location(location.getWorld(), max.getDouble("x"), max.getDouble("y"), max.getDouble("z"));
                        RegionCuboid cuboid = new RegionCuboid(minPos, maxPos, UUID.fromString(ownerUUID), world);

                        if (cuboid.isInside(location)) {
                            return cuboid;
                        }
                    }
                }
            }
        }
        return null;
    }

    private static String getRegionName(Location location) {
        ConfigurationSection regions = RegionManager.config.getData().getConfigurationSection("Regions");

        if (location != null && regions != null) {
            for (String regionName : regions.getKeys(false)) {
                ConfigurationSection region = regions.getConfigurationSection(regionName);
                if (region != null) {
                    String ownerUUID = region.getString("owner");
                    String worldType = region.getString("world");
                    ConfigurationSection min = region.getConfigurationSection("min");
                    ConfigurationSection max = region.getConfigurationSection("max");

                    if (min != null && max != null && ownerUUID != null && worldType != null) {
                        World world = Bukkit.getWorld(worldType);
                        Location minPos = new Location(location.getWorld(), min.getDouble("x"), min.getDouble("y"), min.getDouble("z"));
                        Location maxPos = new Location(location.getWorld(), max.getDouble("x"), max.getDouble("y"), max.getDouble("z"));
                        RegionCuboid cuboid = new RegionCuboid(minPos, maxPos, UUID.fromString(ownerUUID), world);

                        if (cuboid.isInside(location)) {
                            return regionName;
                        }
                    }
                }
            }
        }
        return null;
    }
}
