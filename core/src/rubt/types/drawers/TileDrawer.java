package rubt.types.drawers;

import arc.graphics.Texture;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import rubt.Axial;
import rubt.world.Tile;

public class TileDrawer {

    /** All possible configurations of the presence or absence of neighbors. */
    public static final byte[] configs = new byte[] { 0, 1, 3, 5, 9, 7, 11, 19, 15, 23, 27, 31, 63 };

    /** Tile textures: normal in the first column, luminous in the second. */
    public TextureRegion[][] textures;

    public TileDrawer(Texture grid) {
        textures = TextureRegion.split(grid, 64, 64); // someone mixed up a row with a column, but it's not critical
    }

    // region draw

    /** Rendering of non-luminous texture. */
    public void draw(Tile tile) {
        Draw.rect(textures[tile.textureId][0], tile, 24f, 24f, tile.textureRotation);
    }

    /** Rendering of luminous texture. */
    public void drawGlow(Tile tile) {
        if (textures[0].length == 1) return; // luminous texture may be missing
        Draw.rect(textures[tile.textureId][1], tile, 24f, 24f, tile.textureRotation);
    }

    // endregion
    // region cache

    /** Returns a neighbor configuration byte, where 0 is presence and 1 is absence. */
    private static byte config(Tile tile) {
        byte[] cfg = { 0 }; // java sucks
        int[] index = { 0 };

        // mark missing neighbors as 1
        Axial.neighbours(tile, other -> cfg[0] |= Mathf.num(other == null || other.type != tile.type) << index[0]++);

        return cfg[0];
    }

    /** Circular bit shift using only 6 out of 8 bits. */
    private static byte shift(byte cfg) {
        return (byte) ((cfg >>> 1) | (cfg << 5) & 0x3F);
    }

    /** Caches a texture id and its rotation. */
    public static void cache(Tile tile) {
        byte cfg = config(tile);

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < configs.length; j++) {
                if (configs[j] != cfg) continue;

                tile.textureId = j;
                tile.textureRotation = i * 60;

                return;
            }

            cfg = shift(cfg);
        }
    }

    // endregion
}
