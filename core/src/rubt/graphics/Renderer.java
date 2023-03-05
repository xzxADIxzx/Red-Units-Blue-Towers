package rubt.graphics;

import arc.graphics.g2d.Bloom;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import rubt.Groups;
import rubt.types.textures.NormalTexture;
import rubt.world.*;

import static arc.Core.*;
import static rubt.Vars.*;

public class Renderer {

    public Bloom bloom = new Bloom(true);

    public void draw() {
        NormalTexture.pointer = -1;

        camera.resize(graphics.getWidth() / 4f, graphics.getHeight() / 4f);
        bloom.resize(graphics.getWidth(), graphics.getHeight());
        bloom.blurPasses = 6;

        Draw.proj(camera);
        Draw.sort(true);

        Draw.draw(Layers.bg, () -> {
            graphics.clear(Palette.background);
            Fill.light(0f, 0f, 64, graphics.getHeight() / 5f, Palette.lightbg, Palette.background);
        });

        Draw.draw(Layers.tiles, () -> Groups.tiles.each(Tile::draw));
        Draw.draw(Layers.units, () -> {
            Groups.units.each(Unit::draw);

            bloom.setBloomIntesity(2.8f);
            bloom.capture();

            Draw.color(Palette.red);
            Groups.units.each(Unit::drawGlow);

            bloom.render();
        });
        Draw.draw(Layers.turrets, () -> {
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
