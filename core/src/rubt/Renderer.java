package rubt;

import static arc.Core.*;

import arc.graphics.Color;
import rubt.world.*;

public class Renderer {
    
    public static void draw() {
        graphics.clear(Color.sky);

        Groups.tiles.each(Tile::draw);
        Groups.turrets.each(Turret::draw);
        Groups.units.each(Unit::draw);
    }
}
