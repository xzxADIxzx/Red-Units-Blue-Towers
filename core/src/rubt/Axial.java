package rubt;

import arc.math.Mathf;
import arc.math.geom.*;

/**
 * Implementation of some algorithms in the axial hexagonal coordinate system.
 * Algorithms are taken from: https://www.redblobgames.com/grids/hexagons
 *
 * @author xzxADIxzx
 */
public class Axial {

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
}
