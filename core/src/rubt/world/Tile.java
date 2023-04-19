package rubt.world;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.geom.Geometry;
import arc.math.geom.Position;
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

    public float drawX() {
        return x * tilesize;
    }

    public float drawY() {
        return y * tilesize;
    }

    public float getX() {
        return drawX();
    }

    public float getY() {
        return drawY();
    }

    public void draw() {
        Draw.reset();

        Draw.color(Color.gray);
        Fill.rect(drawX(), drawY(), tilesize, tilesize);
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
