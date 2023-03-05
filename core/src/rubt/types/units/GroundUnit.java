package rubt.types.units;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import rubt.types.UnitType;
import rubt.world.Unit;

public class GroundUnit extends UnitType {

    public GroundUnit(String name) {
        super(name);
    }

    public void update(Unit unit) {
        unit.movePath(unit.target);
    }

    public void draw(Unit unit) {
        Draw.color(Color.red);
        Fill.square(unit.x, unit.y, 8f, unit.rot());
    }

    public void drawGlow(Unit unit) {}
}
