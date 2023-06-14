package rubt.world;

import arc.math.geom.*;
import arc.struct.Seq;
import rubt.Axial;
import rubt.Groups;
import rubt.Groups.Entity;
import rubt.content.TileTypes;
import rubt.io.Reads;
import rubt.io.Writes;
import rubt.types.ContentType;
import rubt.types.TileType;

import static rubt.Vars.*;

public class Tile extends Entity implements ContentType.Provider<Tile> {

    public TileType type;

    public short q, r;
    public float x, y;

    public Tile() {
        super(Groups.tiles);
    }

    public ContentType<Tile> type() {
        return type;
    }

    public void cache() {
        x = Axial.worldX(tilesize, q);
        y = Axial.worldY(tilesize, q, r);
    }

    // region position

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    // endregion

    public Seq<Tile> neightbours() { // TODO cache
        Seq<Tile> neightbours = new Seq<>();

        for (var offset : Geometry.d4) {
            Tile neightbour = world.get(q + offset.x, r + offset.y);
            if (neightbour != null) neightbours.add(neightbour);
        }

        return neightbours;
    }

    // region serialization

    public void write(Writes w) {
        w.b(type.id);
    }

    public void read(Reads r) {
        type = TileTypes.all.get(r.b());
    }

    // endregion
}
