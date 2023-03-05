package rubt.types.units;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import rubt.types.UnitType;
import rubt.world.Unit;

public class AirUnit extends UnitType {

    public AirUnit(String name) {
        super(name);
    }

    public void update(Unit unit) {
        unit.moveVel(unit.target);
    }

    public void draw(Unit unit) {
        Draw.color(Color.red);
        Fill.circle(unit.x, unit.y, 10f);
    }

    public void drawGlow(Unit unit) {}
}
