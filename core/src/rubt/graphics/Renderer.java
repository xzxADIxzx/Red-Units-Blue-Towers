package rubt.graphics;

import arc.graphics.Color;
import arc.graphics.g2d.Bloom;
import arc.graphics.g2d.Draw;
import rubt.Groups;
import rubt.world.*;

import static arc.Core.*;
import static rubt.Vars.*;

public class Renderer {

    public Bloom bloom = new Bloom(true);

    public void draw() {
        camera.resize(graphics.getWidth() / 4f, graphics.getHeight() / 4f);
        bloom.resize(graphics.getWidth(), graphics.getHeight());

        bloom.setBloomIntesity(10f);
        bloom.blurPasses = 6;

        Draw.proj(camera);
        Draw.sort(true);

        Draw.draw(Layers.bg, () -> {
            graphics.clear(Color.sky);
        });

        Draw.draw(Layers.tiles, () -> Groups.tiles.each(Tile::draw));
        Draw.draw(Layers.units, () -> Groups.units.each(Unit::draw));
        Draw.draw(Layers.turrets, () -> Groups.turrets.each(Turret::draw));

        Draw.draw(Layers.overlay - .02f, bloom::capture);
        Draw.draw(Layers.overlay + .02f, bloom::render);
        Draw.draw(Layers.overlay, handler::draw);

        Draw.flush();
    }
}
