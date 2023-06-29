package rubt.editor;

import arc.struct.ShortSeq;
import rubt.content.TileTypes;
import rubt.types.TileType;
import rubt.world.Tile;

import static rubt.Vars.*;

public class EditOperation {

    public ShortSeq data = new ShortSeq();

    public void add(Tile tile, TileType type) {
        data.add(tile.q, tile.r, (short) (tile.type.id - type.id));
    }

    public boolean any() {
        return !data.isEmpty();
    }

    public void undo() {
        for (int i = 0; i < data.size;) {
            Tile tile = world.get(data.get(i++), data.get(i++));
            editor.set(tile, TileTypes.all.get(tile.type.id + data.get(i++)));
        }
    }

    public void redo() {
        for (int i = 0; i < data.size;) {
            Tile tile = world.get(data.get(i++), data.get(i++));
            editor.set(tile, TileTypes.all.get(tile.type.id - data.get(i++)));
        }
    }
}
