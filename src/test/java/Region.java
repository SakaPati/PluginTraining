import java.util.ArrayList;
import java.util.List;

public class Region {
    public static void main(String[] args) {
        String nick = "Fozeton";
        List<Cuboid> regions = new ArrayList<>();
        for (int i = 0; i <= 10000; i++) {
            int minX = i * 10;
            int minY = 80;
            int minZ = i * 10;

            int maxX = minX + 15;
            int maxY = 80;
            int maxZ = minZ + 15;
            regions.add(new Cuboid(nick, minX, minY, minZ, maxX, maxY, maxZ));
        }
        long start = System.currentTimeMillis();

        for (int r = 0; r <= 100000; r++) {
            for (Cuboid cub : regions) {
                if(cub.isInside(50000, 80, 50000)) {
                    break;
                }
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("Time passed: " + ((end - start) / 1000));
    }
}

class Cuboid {
    private final String nick;
    private final int minX, minY, minZ, maxX, maxY, maxZ;

    public Cuboid(String nick, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.nick = nick;

        this.minX = Math.min(minX, maxX);
        this.minY = Math.min(minY, maxY);
        this.minZ = Math.min(minZ, maxZ);

        this.maxX = Math.max(minX, maxX);
        this.maxY = Math.max(minY, maxY);
        this.maxZ = Math.max(minZ, maxZ);
    }

    public boolean isInside(int x, int y, int z) {
        return x >= minX && x <= maxX &&
        y >= minY && y <= maxY &&
        z >= minZ && z <= maxZ;
    }
}
