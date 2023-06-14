package rubt;

import arc.func.Cons;
import arc.math.Mathf;
import arc.math.geom.*;
import arc.struct.Seq;
import rubt.world.Tile;

import static rubt.Vars.*;

/**
 * Implementation of some algorithms in the axial hexagonal coordinate system.
 * Algorithms are taken from: https://www.redblobgames.com/grids/hexagons
 *
 * @author xzxADIxzx
 */
public class Axial {

    /** Array of offsets along which the tiles neighbours are located. */
    public static final Point2[] neighbours = {
            new Point2(+1, 0), new Point2(+1, -1), new Point2(0, -1),
            new Point2(-1, 0), new Point2(-1, +1), new Point2(0, +1) };

    // region world/hex coordinate conversion

    public static Position world(float size, int q, int r) {
        return new Vec2(worldX(size, q), worldY(size, q, r));
    }

    public static float worldX(float size, int q) {
        return size * (3f / 2f * q);
    }

    public static float worldY(float size, int q, int r) {
        return size * (Mathf.sqrt3 / 2f * q + Mathf.sqrt3 * r);
    }

    public static Point2 hex(float size, float x, float y) {
        return round((2f / 3f * x) / size, (-x + Mathf.sqrt3 * y) / 3f / size);
    }

    public static int hexQ(float size, float x, float y) {
        return hex(size, x, y).x;
    }

    public static int hexR(float size, float x, float y) {
        return hex(size, x, y).y;
    }

    public static Point2 round(float q, float r) {
        int roundQ = Mathf.round(q), roundR = Mathf.round(r);

        q -= roundQ;
        r -= roundR;

        if (Math.abs(q) >= Math.abs(r))
            return new Point2(roundQ + Mathf.round(q + r / 2f), roundR);
        else
            return new Point2(roundQ, roundR + Mathf.round(r + q / 2f));
    }

    // endregion
    // region neighbours

    public static void neighbours(Tile tile, Cons<Tile> cons) {
        for (var neighbour : neighbours)
            cons.get(world.get(tile.q + neighbour.x, tile.r + neighbour.y));
    }

    public static Seq<Tile> neighbours(Tile tile) {
        var neighbours = new Seq<Tile>(6);
        neighbours(tile, neighbours::add);

        return neighbours;
    }

    // endregion
}
