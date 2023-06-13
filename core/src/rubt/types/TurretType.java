package rubt.types;

import rubt.content.TurretTypes;
import rubt.world.Turret;

public abstract class TurretType extends ContentType<Turret> {

    public int health;
    public int damage;

    public TurretType(String name) {
        super(TurretTypes.all, name);
    }
}
