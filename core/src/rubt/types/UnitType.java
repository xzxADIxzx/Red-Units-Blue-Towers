package rubt.types;

import rubt.content.UnitTypes;

public abstract class UnitType extends ContentType {

    public int health;
    public int damage;

    public UnitType(String name) {
        super(UnitTypes.all, name);
    }
}
