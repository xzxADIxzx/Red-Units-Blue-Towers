package rubt.world;

import arc.math.geom.Position;
import arc.util.Structs;
import rubt.Axial;
import rubt.Groups;
import rubt.Groups.Entity;
import rubt.content.TileTypes;
import rubt.io.Reads;
import rubt.io.Writes;
import rubt.types.TileType;

import static arc.Core.*;
import static rubt.Vars.*;

import java.io.*;

public class World {

    public static final byte[] header = { 'R', 'B', 'W', 'L', 'D' };

    public Tile[] tiles;
    public int width, height;

    public void fill(TileType type) {
        for (short q = 0; q < width; q++) {
            for (short r = 0; r < height; r++) {
                var tile = new Tile();
                tile.type = type;

                set(tile, q, r);
            }
        }
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new Tile[width * height];

        fill(TileTypes.air);
    }

    // region setters/getters

    public Tile set(Tile tile) {
        return tiles[tile.q + tile.r * width] = tile;
    }

    public Tile set(Tile tile, short q, short r) {
        tile.q = q;
        tile.r = r;

        return set(tile);
    }

    public Tile get(int q, int r) {
        if (q < 0 || r < 0 || q >= width || r >= height) return null; // out of bounds
        return tiles[q + r * width];
    }

    public Tile get(float x, float y) {
        var hex = Axial.hex(tilesize, x, y);
        return get(hex.x, hex.y);
    }

    public Tile get(Position position) {
        return get(position.getX(), position.getY());
    }

    // endregion
    // region io

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

            for (short q = 0; q < width; q++)
                for (short r = 0; r < height; r++)
                    set(new Tile(), q, r).read(reads);

            int amount = reads.i();
            for (int i = 0; i < amount; i++)
                Entities.newEntity(reads.b()).read(reads);
        }

        if (Structs.contains(tiles, (Object) null))
            throw new IOException("Corrupted or invalid save file: some tile is null");

        // cache some info about tiles such as x, y and neighbours
        Structs.each(Tile::cache, tiles);
    }

    public void save(OutputStream output) throws IOException {
        output.write(header);

        try (var writes = Writes.of(output)) {
            writes.i(width);
            writes.i(height);

            for (Tile tile : tiles)
                tile.write(writes);

            writes.i(Groups.sync.size);
            for (Entity entity : Groups.sync) {
                writes.b(entity.typeId);
                entity.write(writes);
            }
        }
    }

    // endregion
}
