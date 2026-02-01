package ru.fozeton.training.Task2.WorldGuard;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.UUID;

public class RegionCuboid {
    private final Location min;
    private final Location max;
    private final UUID ownerUUID;
    private final World world;
    private final double minX, minY, minZ, maxX, maxY, maxZ;

    public RegionCuboid(Location pos1, Location pos2, UUID ownerUUID, World world) {
        this.ownerUUID = ownerUUID;
        this.world = world;

        this.minX = Math.min(pos1.getX(), pos2.getX());
        this.minY = Math.min(pos1.getY(), pos2.getY());
        this.minZ = Math.min(pos1.getZ(), pos2.getZ());
        this.maxX = Math.max(pos1.getX(), pos2.getX());
        this.maxY = Math.max(pos1.getY(), pos2.getY());
        this.maxZ = Math.max(pos1.getZ(), pos2.getZ());

        this.min = new Location(world, minX, minY, minZ);
        this.max = new Location(world, maxX, maxY, maxZ);
    }

    public boolean isInside(Location loc) {
        return loc.getWorld().equals(min.getWorld()) &&
                loc.getX() >= min.getX() && loc.getX() <= max.getX() &&
                loc.getY() >= min.getY() && loc.getY() <= max.getY() &&
                loc.getZ() >= min.getZ() && loc.getZ() <= max.getZ();
    }

    public boolean intersects(RegionCuboid other) {
        return (this.minX <= other.maxX && this.maxX >= other.minX) &&
                (this.minY <= other.maxY && this.maxY >= other.minY) &&
                (this.minZ <= other.maxZ && this.maxZ >= other.minZ);
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public World getWorld(){
        return world;
    }
}
