package rubt.editor;

import arc.struct.Seq;
import rubt.types.TileType;
import rubt.world.Tile;

public class MapEditor {

    public EditHistory history = new EditHistory();
    public EditOperation operation;

    public void begin() {
        if (operation != null) end(); // flush changes before writing new ones
        operation = new EditOperation();
    }

    public void end() {
        if (operation.any()) history.add(operation);
    }

    public void draw(Tile tile, TileType type) {
        if (tile.type == type) return;
        if (operation != null) operation.add(tile, type);

        tile.type = type;
        tile.cache();
        tile.neighbours.each(Tile::cache);
    }

    public class EditHistory extends Seq<EditOperation> {

        public static final int max = 64;
        public int index;

        @Override
        public Seq<EditOperation> add(EditOperation op) {
            if (index != size) truncate(index); // new element is added not to the end

            super.addUnique(op);

            if (size > max) remove(0);
            else index++;

            return this;
        }

        @Override
        public Seq<EditOperation> clear() {
            super.clear();
            index = 0;

            return this;
        }

        public void undo() {
            get(--index).undo();
        }

        public void redo() {
            get(index++).redo();
        }

        public boolean hasUndo() {
            return index >= 1;
        }

        public boolean hasRedo() {
            return index < size;
        }
    }
}
