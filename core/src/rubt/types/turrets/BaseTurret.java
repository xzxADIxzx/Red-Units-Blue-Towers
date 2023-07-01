package rubt.types.turrets;

import arc.math.Angles;
import arc.util.Time;
import rubt.Groups;
import rubt.types.BulletType;
import rubt.types.TurretType;
import rubt.world.Turret;
import rubt.world.Unit;

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

    /** Bullet that the turret fires. */
    public BulletType bullet;

    public BaseTurret(String name) {
        super(name);
    }

    /** Searchs for the best target. Logic may differ for different turrets types. */
    public Unit bestTarget(Turret turret) {
        var available = Groups.units.select(target -> target.within(turret, range));
        if (available.isEmpty()) return null;

        available.sort(target -> target.dst(turret));

        // finish off units with low hp
        var lowHP = available.find(target -> target.health * .5f - bullet.damage < 0f);
        if (lowHP != null) return lowHP;

        return available.first();
    }

    public void update(Turret turret) {
        var target = bestTarget(turret);

        boolean within = target != null && target.within(turret, range);
        boolean shooting = within && Angles.within(turret.angleTo(target), turret.rotation, 30f);

        turret.target = target;
        turret.shooting = shooting;

        if (!within) return;

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
