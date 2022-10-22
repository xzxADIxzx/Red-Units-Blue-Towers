package rubt.world;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import rubt.net.NetObject;
import rubt.net.Packet;
import rubt.net.Packet.*;
import rubt.net.PacketProvider;

import static rubt.Vars.*;

public class Tile extends NetObject implements PacketProvider {

    public final int x, y;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Tile(int id, int x, int y) {
        this(x, y);
        this.id = id;
    }

    public float drawX() {
        return x * tilesize;
    }

    public float drawY() {
        return y * tilesize;
    }

    public void draw() {
        Draw.reset();

        Draw.color(Color.gray);
        Fill.square(drawX(), drawY(), tilesize);
    }

    @Override
    public Packet pack() {
        return new TileUpdate(this);
    }
}
