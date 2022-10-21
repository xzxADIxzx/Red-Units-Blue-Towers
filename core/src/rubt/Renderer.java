package rubt;

import static arc.Core.*;

import arc.graphics.Color;
import rubt.world.Turret;
import rubt.world.Unit;

public class Renderer {
    
    public static void draw() {
        graphics.clear(Color.sky);

        Groups.turrets.each(Turret::draw);
        Groups.units.each(Unit::draw);
    }
}
