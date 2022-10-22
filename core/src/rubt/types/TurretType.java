package rubt.types;

import rubt.content.TurretTypes;

public abstract class TurretType extends ContentType {

    public int health;
    public int damage;

    public TurretType(String name) {
        super(TurretTypes.all, name);
    }
}
