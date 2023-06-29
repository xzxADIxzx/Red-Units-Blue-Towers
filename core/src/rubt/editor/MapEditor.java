package rubt.editor;

import rubt.types.TileType;
import rubt.world.Tile;

public class MapEditor {

    public void draw(Tile tile, TileType type) {
        if (tile.type == type) return;
        if (operation != null) operation.add(tile, type);

        tile.type = type;
        tile.cache();
        tile.neighbours.each(Tile::cache);
    }
}
