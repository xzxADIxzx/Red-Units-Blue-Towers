package rubt.types;

import rubt.content.TileTypes;
import rubt.world.Tile;

public abstract class TileType extends ContentType {

    /** Whether ground units will collide with the tile or be able to walk on it. */
    public boolean solid;

    public TileType(String name) {
        super(TileTypes.all, name);
    }

    public abstract void draw(Tile turret);

    public abstract void drawGlow(Tile turret);
}
