package rubt.types.turrets;

import arc.math.Angles;
import arc.util.Time;
import rubt.Groups;
import rubt.types.TurretType;
import rubt.world.Turret;

public abstract class BaseTurret extends TurretType {

    /** How fast the turret turns towards the enemy. Degrees per second. */
    public float rotationSpeed;
    /** Time in seconds between shots. */
    public float reload;
    /** Time in seconds between bursts of shots. */
    public float burstCooldown = 0f;
    /** Projectile spread in degrees. */
    public float inaccuracy = 0f;

    public BaseTurret(String name) {
        super(name);
    }

    public void update(Turret turret) {
        var target = Groups.units.min(unit -> unit.dst(turret)); // TODO better algorithm, for example, look for targets with the lowest hp
        if (target == null) return;

        turret.target = target;
        turret.shooting = target != null && target.within(turret, range);

        turret.rotation = Angles.moveToward(turret.rotation, turret.angleTo(target), rotationSpeed * Time.delta);
    }
}
