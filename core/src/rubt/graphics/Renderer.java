package rubt.graphics;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import rubt.Groups;
import rubt.world.*;

import static arc.Core.*;
import static rubt.Vars.*;

public class Renderer {

    public void draw() {
        camera.resize(graphics.getWidth() / 4f, graphics.getHeight() / 4f);

        Draw.proj(camera);
        Draw.sort(true);

        Draw.draw(Layers.bg, () -> {
            graphics.clear(Color.sky);
        });

        Draw.draw(Layers.tiles, () -> Groups.tiles.each(Tile::draw));
        Draw.draw(Layers.units, () -> Groups.units.each(Unit::draw));
        Draw.draw(Layers.turrets, () -> Groups.turrets.each(Turret::draw));

        Draw.draw(Layers.overlay, handler::draw);

        Draw.flush();
    }
}
