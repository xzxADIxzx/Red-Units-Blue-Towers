package rubt.world;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import rubt.Groups;
import rubt.Groups.GroupObject;

import static rubt.Vars.*;

public class Tile extends GroupObject {

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

    public void draw() {
        Draw.reset();

        Draw.color(Color.gray);
        Fill.rect(drawX(), drawY(), tilesize, tilesize);
    }
}
