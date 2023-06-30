package rubt.types.turrets;

import arc.math.Angles;
import arc.util.Time;
import rubt.Groups;
import rubt.types.TurretType;
import rubt.world.Turret;

public abstract class BaseTurret extends TurretType {

    /** How fast the turret turns towards the enemy. Degrees per tick. */
    public float rotationSpeed;
    /** Time in ticks between shots. */
    public float reload;
    /** Number of shots in the burst. 0 if fire rate is constant. */
    public int burstShoots;
    /** Time in ticks between bursts of shots. */
    public float burstCooldown;
    /** Projectile spread in degrees. */
    public float inaccuracy;

    public BaseTurret(String name) {
        super(name);
    }

    public void update(Turret turret) {
        var target = Groups.units.min(unit -> unit.dst(turret)); // TODO better algorithm, for example, look for targets with the lowest hp
        if (target == null) return;

        turret.target = target;
        turret.shooting = target != null && target.within(turret, range);

        // TODO take into account the speed of the bullet and the unit
        turret.rotation = Angles.moveToward(turret.rotation, turret.angleTo(target), rotationSpeed * Time.delta);
    }

    public void shoot(Turret turret) {
        // burst cooldown
        if (burstShoots > 0 && Time.time - turret.burstCooldown < burstCooldown) return;

        // shot cooldown
        if (Time.time - turret.shotCooldown < reload) return;

        spawnBullet(turret);
        turret.shotCooldown = Time.time;

        if (burstShoots > 0 && turret.shots++ % burstShoots == 0) burstCooldown = Time.time;
    }

    public abstract void spawnBullet(Turret turret);
}
