package rubt.types.drawers;

import arc.graphics.g2d.Draw;
import arc.math.Interp;
import arc.struct.Seq;
import rubt.types.drawers.DrawerParts.*;
import rubt.world.Body;

public class BodyDrawer<T extends Body> {

    public Seq<DrawerPart<T>> parts = new Seq<>();

    public void draw(T body, float baseLayer, float glowLayer) {
        Draw.z(baseLayer);
        parts.each(part -> part.draw(body));

        Draw.z(glowLayer);
        parts.each(part -> part.drawGlow(body));
    }

    public void texture(String name) {
        parts.add(new TexturePart<T>(name));
    }

    public void moving(String name, float x, float y, float rotation, PartProgress<T> progress, Interp interp) {
        parts.add(new MovingPart<T>(name, x, y, rotation, progress, interp));
    }

    public void moving(String name, float x, float y, float rotation, PartProgress<T> progress) {
        moving(name, x, y, rotation, progress, Interp.pow2);
    }
}
