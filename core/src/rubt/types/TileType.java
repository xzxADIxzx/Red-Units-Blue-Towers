package rubt.types;

import rubt.content.TileTypes;
import rubt.world.Tile;

public abstract class TileType extends ContentType<Tile> {

    /** Whether ground units will collide with the tile or be able to walk on it. */
    public boolean solid;
    /** Whether a player can choose this tile to build something on it. */
    public boolean choosable;

    public TileType(String name) {
        super(TileTypes.all, name);
    }
}
