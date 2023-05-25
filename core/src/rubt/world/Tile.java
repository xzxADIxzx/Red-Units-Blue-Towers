package rubt.world;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.geom.*;
import arc.struct.Seq;
import rubt.Groups;
import rubt.Groups.GroupObject;

import static rubt.Vars.*;

public class Tile extends GroupObject implements Position {

    public final int x, y;

    public Tile(int x, int y) {
        super(Groups.tiles);
        this.x = x;
        this.y = y;
    }

    public Tile(int pos) {
        this(Point2.x(pos), Point2.y(pos));
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
}
