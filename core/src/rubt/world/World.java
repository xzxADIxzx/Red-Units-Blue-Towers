package rubt.world;

import arc.math.geom.Position;
import arc.util.Structs;
import rubt.Axial;
import rubt.Groups;
import rubt.Groups.Entity;
import rubt.io.Reads;
import rubt.io.Writes;

import static arc.Core.*;
import static rubt.Vars.*;

import java.io.*;

public class World {

    public static byte[] header = { 'R', 'B', 'W', 'L', 'D' };

    public Tile[] tiles;
    public int width, height;

    public void set(Tile tile) {
        tiles[tile.q + tile.r * width] = tile;
    }

    public Tile get(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) return null; // out of bounds
        return tiles[x + y * width];
    }

    public Tile get(float x, float y) {
        return get(Axial.hexQ(tilesize, x, y), Axial.hexR(tilesize, x, y));
    }

    public Tile get(Position position) {
        return get(position.getX(), position.getY());
    }

    public static InputStream random() {
        var maps = files.local("maps").seq();
        if (maps.isEmpty()) throw new RuntimeException("Maps folder is empty!");

        return maps.random().read();
    }

    public void load(InputStream input) throws IOException {
        for (byte b : header) {
            if (input.read() != b) throw new IOException("Invalid file header!");
        }

        try (var reads = Reads.of(input)) {
            width = reads.i();
            height = reads.i();
            tiles = new Tile[width * height];

            int amount = reads.i();
            for (int i = 0; i < amount; i++)
                new Tile().read(reads);

            amount = reads.i();
            for (int i = 0; i < amount; i++) {
                var entity = Entities.newEntity(reads.b());
                entity.read(reads);
            }
        }

        if (Structs.contains(tiles, (Object) null))
            throw new IOException("Corrupted or invalid save file: some tile is null");

        // cache some info about tiles such as x, y and neighbors
        Structs.each(Tile::cache, tiles);
    }

    public void save(OutputStream output) throws IOException {
        output.write(header);

        try (var writes = Writes.of(output)) {
            writes.i(width);
            writes.i(height);

            writes.i(tiles.length);
            for (Tile tile : tiles)
                tile.write(writes);

            writes.i(Groups.sync.size);
            for (Entity entity : Groups.sync) {
                writes.b(entity.typeId);
                entity.write(writes);
            }
        }
    }
}
