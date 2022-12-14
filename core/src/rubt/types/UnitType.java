package rubt.types;

import rubt.content.UnitTypes;
import rubt.world.Unit;

public abstract class UnitType extends ContentType {

    public int health;
    public int damage;

    public float speed;
    public float accel;

    public UnitType(String name) {
        super(UnitTypes.all, name);
    }

    public abstract void update(Unit unit);

    public abstract void draw(Unit unit);
}
