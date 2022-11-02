package rubt.types.turrets;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
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
        if (target == null) return;

        turret.rotation = turret.angleTo(target);
    }

    public void draw(Turret turret) {
        Draw.color(Color.green);
        Fill.rect(turret.x, turret.y, 12f, 12f, turret.rot());
    }
}
