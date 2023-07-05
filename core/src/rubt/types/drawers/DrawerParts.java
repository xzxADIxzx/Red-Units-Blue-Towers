package rubt.types.drawers;

import arc.func.Floatf;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Interp;
import arc.util.Time;
import arc.util.Tmp;
import rubt.graphics.Shaders;
import rubt.graphics.Textures;
import rubt.world.*;

public class DrawerParts {

    public interface DrawerPart<T extends Body> {

        /** Rendering of non-luminous part. */
        void draw(T body);

        /** Rendering of luminous part. */
        void drawGlow(T body);
    }

    public static class TexturePart<T extends Body> implements DrawerPart<T> { // TODO only (non-)luminous part?

        public TextureRegion base, glow;

        public TexturePart(String name) {
            base = Textures.load("parts/", name + "-base");
            glow = Textures.load("parts/", name + "-glow");
        }

        @Override
        public void draw(T body) {
            Shaders.normal.draw(body.rotation, () -> Draw.rect(base, body, body.rot()));
        }

        @Override
        public void drawGlow(T body) {
            Draw.rect(glow, body, body.rot());
        }
    }

    public static class MovingPart<T extends Body> extends TexturePart<T> {

        public float x, y, rotation;
        public PartProgress<T> progress;
        public Interp interp;

        public MovingPart(String name, float x, float y, float rotation, PartProgress<T> progress, Interp interp) {
            super(name);

            this.x = x; // well lombok is not omnipotent
            this.y = y;
            this.rotation = rotation;
            this.progress = progress;
            this.interp = interp;
        }

        public void draw(T body, TextureRegion region) {
            float p = interp.apply(progress.get(body));
            float r = body.rotation + rotation * p;

            Draw.rect(region, Tmp.v1.set(x * p, y * p).rotate(rotation).add(body), r);
        }

        @Override
        public void draw(T body) {
            Shaders.normal.draw(body.rotation, () -> draw(body, base));
        }

        @Override
        public void drawGlow(T body) {
            draw(body, glow);
        }

    }

    public interface PartProgress<T> extends Floatf<T> {

        PartProgress<Turret> reload = t -> Math.min(1f, (Time.time - t.shotCooldown) / t.type.reload);

        PartProgress<Unit> speed = u -> u.vel.len2() / u.type.speed * u.type.speed;
    }
}
