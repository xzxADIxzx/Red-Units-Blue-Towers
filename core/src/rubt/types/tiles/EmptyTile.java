package rubt.types.tiles;

import rubt.types.TileType;
import rubt.world.Tile;

public class EmptyTile extends TileType {

    public EmptyTile(String name) {
        super(name);
    }

    @Override
    public void loadui() {}

    public void update(Tile tile) {}

    public void draw(Tile tile) {}

    public void drawGlow(Tile tile) {}
}
