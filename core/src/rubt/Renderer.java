package rubt;

import arc.graphics.Color;
import rubt.world.*;

import static arc.Core.*;
import static rubt.Vars.*;

public class Renderer {

    public static void draw() {
        graphics.clear(Color.sky);

        Groups.tiles.each(Tile::draw);
        Groups.turrets.each(Turret::draw);
        Groups.units.each(Unit::draw);

        handler.draw();
    }
}
