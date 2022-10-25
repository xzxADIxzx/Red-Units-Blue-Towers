package rubt.types.units;

import arc.util.Tmp;
import rubt.types.UnitType;
import rubt.world.Unit;

public class AirUnit extends UnitType {

    public float speed;

    public AirUnit(String name) {
        super(name);
    }

    public void update(Unit unit) {
        Tmp.v1.set(unit).sub(unit.target).limit(speed);
        unit.move(Tmp.v1);
    }
}
