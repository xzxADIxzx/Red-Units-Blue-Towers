package rubt.graphics;

import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.util.Time;

public class Drawf {

    public static void drawTarget(Position target, float offset) {
        offset += Time.time;
        Lines.poly(target.getX(), target.getY(), 3, Mathf.absin(offset, 8f, 8f), offset);
    }

    public static void drawTarget(Position target) {
        drawTarget(target, 0f);
        drawTarget(target, 20f);
        drawTarget(target, 40f);
    }
}
