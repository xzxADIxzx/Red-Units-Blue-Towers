package rubt.types;

import rubt.content.TurretTypes;
import rubt.world.Turret;

public abstract class TurretType extends ContentType<Turret> {

    /** Distance the turret can reach. */
    public float range;
    /** How many tiles does this turret take up. */
    public int size = 1;

    public TurretType(String name) {
        super(TurretTypes.all, name);
    }

    /** Turret firing update. */
    public abstract void shoot(Turret turret);
}
