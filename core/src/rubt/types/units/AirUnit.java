package rubt.types.units;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.util.Tmp;
import rubt.types.UnitType;
import rubt.world.Unit;

public class AirUnit extends UnitType {

    public float speed;

    public AirUnit(String name) {
        super(name);
    }

    public void update(Unit unit) {
        Tmp.v1.set(unit.target).sub(unit).limit(speed);
        unit.move(Tmp.v1);
    }

    public void draw(Unit unit) {
        Draw.color(Color.red);
        Fill.circle(unit.x, unit.y, 10f);

        Draw.color(Color.blue);
        Fill.circle(unit.target.getX(), unit.target.getY(), 10f);
    }
}
