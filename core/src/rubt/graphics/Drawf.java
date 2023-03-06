package rubt.graphics;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.math.geom.Vec3;
import arc.util.Time;
import arc.util.Tmp;
import rubt.world.Unit;

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

    public static void drawEngine(float x, float y, float rotation, float scl) {
        scl += Mathf.absin(8f, 1.5f);
        Draw.rect(Textures.engine, x, y - scl / 2f, scl, scl, scl / 2f, scl, rotation);
    }

    public static void drawEngine(float x, float y, float rotation) {
        Tmp.c1.set(Draw.getColor());
        Tmp.c2.set(Draw.getColor()).mul(1.8f);

        drawEngine(x, y, rotation, 6f);
        Draw.color(Tmp.c2);

        drawEngine(x, y, rotation, 4.5f);
        Draw.color(Tmp.c1);
    }

    public static void drawEngine(Vec3 engine, Unit unit) {
        Tmp.v1.set(engine).rotate(unit.rot());
        drawEngine(unit.x + Tmp.v1.x, unit.y + Tmp.v1.y, unit.rot() + engine.z);
    }
}
