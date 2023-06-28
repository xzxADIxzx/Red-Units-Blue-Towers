package rubt.types.tiles;

import rubt.graphics.Textures;
import rubt.types.TileType;
import rubt.types.drawers.TileDrawer;
import rubt.world.Tile;

public class BaseTile extends TileType {

    public TileDrawer drawer;

    public BaseTile(String name) {
        super(name);
    }

    @Override
    public void loadui() {
        drawer = Textures.tile(name);
        icon = drawer.icon();
    }

    public void update(Tile tile) {}

    public void draw(Tile tile) {
        drawer.draw(tile);
    }

    public void drawGlow(Tile tile) {
        drawer.drawGlow(tile);
    }
}
