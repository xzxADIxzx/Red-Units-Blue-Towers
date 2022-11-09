package rubt.world;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static arc.Core.*;

public class World {

    public static byte[] header = { 'R', 'B', 'W', 'L', 'D' };

    public Tile[] tiles;
    public int width, height;

    public void set(Tile tile) {
        tiles[tile.x + tile.y * width] = tile;
    }

    public Tile get(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) return null; // out of bounds
        return tiles[x + y * width];
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

        try (var stream = new DataInputStream(input)) {
            width = stream.readInt();
            height = stream.readInt();
            tiles = new Tile[width * height];

            int amount = stream.readInt();
            for (int i = 0; i < amount; i++)
                set(new Tile(stream.readInt(), stream.readInt()));
        }
    }

    public void save(OutputStream output) throws IOException {
        output.write(header);

        try (var stream = new DataOutputStream(output)) {
            stream.writeInt(width);
            stream.writeInt(height);

            stream.writeInt(tiles.length);
            for (Tile tile : tiles) {
                stream.writeInt(tile.x);
                stream.writeInt(tile.y);
            }
        }
    }
}
