package rubt.graphics;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import rubt.Groups;
import rubt.world.*;

import static arc.Core.*;
import static rubt.Vars.*;

public class Renderer {

    public static void draw() {
        camera.resize(graphics.getWidth() / 4f, graphics.getHeight() / 4f);

        Draw.proj(camera);
        Draw.sort(true);

        graphics.clear(Color.sky);

        Groups.tiles.each(Tile::draw);
        Groups.turrets.each(Turret::draw);
        Groups.units.each(Unit::draw);

        handler.draw();

        Draw.flush();
    }
}
