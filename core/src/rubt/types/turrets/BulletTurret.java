package rubt.types.turrets;

import rubt.types.TurretType;

public class BulletTurret extends TurretType {

    public float reload = 1f;
    public float inaccuracy = 0f;

    public BulletTurret(String name) {
        super(name);
    }
}
