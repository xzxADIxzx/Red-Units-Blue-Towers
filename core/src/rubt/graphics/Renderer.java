package rubt.graphics;

import arc.graphics.Color;
import arc.graphics.g2d.Bloom;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import rubt.Groups;
import rubt.world.*;

import static arc.Core.*;
import static rubt.Vars.*;

public class Renderer {

    public Bloom bloom = new Bloom(true);

    public float minZoom = 1f, maxZoom = 6f;
    public float current = 4f, target = 4f;

    public void zoom(float amount) {
        target *= (amount / 4) + 1;
        target = Mathf.clamp(target, minZoom, maxZoom);
    }

    public void draw() {
        float dest = Mathf.clamp(Mathf.round(target, 0.5f), minZoom, maxZoom);
        current = Mathf.lerpDelta(current, dest, 0.1f);
        if (Mathf.equal(current, dest, 0.001f)) current = dest;

        camera.resize(graphics.getWidth() / current, graphics.getHeight() / current);
        bloom.resize(graphics.getWidth(), graphics.getHeight());
        bloom.blurPasses = 6;

        Draw.proj(camera);
        Draw.sort(true);

        Draw.draw(Layers.bg, () -> {
            graphics.clear(Palette.background);
            Fill.light(camera.position.x, camera.position.y, 64, graphics.getHeight() / current * .8f, Palette.lightbg, Palette.background);
        });

        Draw.draw(Layers.tiles, () -> Groups.tiles.each(Tile::draw));
        Draw.draw(Layers.units, () -> {
            Draw.color(Color.white);
            Groups.units.each(Unit::draw);

            bloom.setBloomIntesity(2.8f);
            bloom.capture();

            Draw.color(Palette.red);
            Groups.units.each(Unit::drawGlow);

            bloom.render();
        });
        Draw.draw(Layers.turrets, () -> {
            Draw.color(Color.white);
            Groups.turrets.each(Turret::draw);

            bloom.setBloomIntesity(2.8f);
            bloom.capture();

            Draw.color(Palette.blue);
            Groups.turrets.each(Turret::drawGlow);

            bloom.render();
        });

        Draw.draw(Layers.overlay, () -> {
            bloom.setBloomIntesity(1.8f);
            bloom.capture();

            handler.draw();
            bloom.render();
        });

        Draw.flush();

        scene.act();
        scene.draw();
    }
}
