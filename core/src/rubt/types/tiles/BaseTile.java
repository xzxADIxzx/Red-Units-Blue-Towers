package rubt.types.tiles;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import rubt.types.TileType;
import rubt.world.Tile;

import static rubt.Vars.*;

public class BaseTile extends TileType {

    public BaseTile(String name) {
        super(name);
    }

    @Override
    public void loadui() {/* TODO add textures for tiles */}

    public void update(Tile tile) {}

    public void draw(Tile tile) { // temp
        if (!solid) return;

        Draw.color(Color.gray);
        Fill.poly(tile.getX(), tile.getY(), 6, tilesize);
    }

    public void drawGlow(Tile tile) { // temp
        if (solid) return;

        Draw.color();
        Fill.poly(tile.getX(), tile.getY(), 6, tilesize);
    }
}
