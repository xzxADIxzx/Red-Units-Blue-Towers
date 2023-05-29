package rubt.world;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.geom.*;
import arc.struct.Seq;
import rubt.Groups;
import rubt.Groups.Entity;
import rubt.net.PacketSerializer.Reads;
import rubt.net.PacketSerializer.Writes;

import static rubt.Vars.*;

public class Tile extends Entity {

    public short x, y;

    public Tile() {
        super(Groups.tiles);
    }

    // region position

    public float getX() {
        return x * tilesize;
    }

    public float getY() {
        return y * tilesize;
    }

    public int pack() {
        return Point2.pack(x, y);
    }

    // endregion

    public void draw() {
        Draw.reset();

        Draw.color(Color.gray);
        Fill.rect(getX(), getY(), tilesize, tilesize);
    }

    public Seq<Tile> neightbours() {
        Seq<Tile> neightbours = new Seq<>();

        for (var offset : Geometry.d4) {
            Tile neightbour = world.get(x + offset.x, y + offset.y);
            if (neightbour != null) neightbours.add(neightbour);
        }

        return neightbours;
    }

    // region serialization

    public void write(Writes w) {
        w.writeShort(x);
        w.writeShort(y);
    }

    public void read(Reads r) {
        x = r.readShort();
        y = r.readShort();
    }

    // endregion
}
