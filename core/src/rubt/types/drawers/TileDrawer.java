package rubt.types.drawers;

import arc.graphics.Texture;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import rubt.Axial;
import rubt.world.Tile;

public class TileDrawer {

    /** All possible configurations of the presence or absence of neighbors. */
    public static final byte[] configs = new byte[] { 0, 1, 3, 5, 9, 7, 11, 19, 15, 23, 31, 63 };

    /** Tile textures: normal in the first row, luminous in the second. */
    public TextureRegion[][] textures;

    public TileDrawer(Texture grid) {
        textures = TextureRegion.split(grid, 64, 64);
    }

    // region draw

    /** Rendering of non-luminous texture. */
    public void draw(Tile tile) {
        Draw.rect(textures[0][tile.textureId], tile, 24f, 24f, tile.textureRotation);
    }

    /** Rendering of luminous texture. */
    public void drawGlow(Tile tile) {
        if (textures.length == 1) return;
        Draw.rect(textures[1][tile.textureId], tile, 24f, 24f, tile.textureRotation);
    }

    // endregion
    // region cache

    /** Returns a neighbor configuration byte, where 0 is presence and 1 is absence. */
    public static byte config(Tile tile) {
        byte[] cfg = { 0 }; // java sucks
        int[] index = { 0 };

        // mark missing neighbors as 1
        Axial.neighbours(tile, other -> cfg[0] |= Mathf.num(other == null || other.type != tile.type) << index[0]++);

        return cfg[0];
    }

    /** Caches a texture id and its rotation. */
    public static void cache(Tile tile) {
        byte cfg = config(tile);

        for (int i = 0; i < 6; i++) {
            byte rotated = (byte) (cfg << i);

            for (int j = 0; j < configs.length; j++) {
                if (configs[j] != rotated) continue;

                tile.textureId = j;
                tile.textureRotation = i * 60;

                return;
            }
        }
    }

    // endregion
}
