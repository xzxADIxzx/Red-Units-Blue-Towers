package rubt.types.tiles;

import rubt.types.TileType;
import rubt.world.Tile;

public class EmptyTile extends TileType {

    public EmptyTile(String name) {
        super(name);
    }

    public void update(Tile unit) {}

    public void draw(Tile unit) {}

    public void drawGlow(Tile unit) {}
}
