package rubt.world;

import arc.math.geom.*;
import arc.struct.Seq;
import rubt.Groups;
import rubt.Groups.Entity;
import rubt.io.Reads;
import rubt.io.Writes;
import rubt.types.ContentType;
import rubt.types.TileType;

import static rubt.Vars.*;

public class Tile extends Entity implements ContentType.Provider<Tile> {

    public TileType type;

    public short x, y;

    public Tile() {
        super(Groups.tiles);
    }

    public ContentType<Tile> type() {
        return type;
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
        w.s(x);
        w.s(y);
    }

    public void read(Reads r) {
        x = r.s();
        y = r.s();

        world.set(this);
    }

    // endregion
}
