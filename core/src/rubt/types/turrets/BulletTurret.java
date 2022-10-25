package rubt.types.turrets;

import rubt.Groups;
import rubt.types.TurretType;
import rubt.world.*;

public class BulletTurret extends TurretType {

    public float reload = 1f;
    public float inaccuracy = 0f;

    public BulletTurret(String name) {
        super(name);
    }

    public void update(Turret turret) {
        Unit target = Groups.units.min(unit -> unit.dst(turret));
        turret.rotation = turret.angleTo(target);
    }
}
