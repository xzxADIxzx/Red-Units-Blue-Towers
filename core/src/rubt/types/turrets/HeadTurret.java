package rubt.types.turrets;

import arc.graphics.g2d.Draw;
import rubt.Groups;
import rubt.graphics.Textures;
import rubt.types.TurretType;
import rubt.types.textures.NormalTexture;
import rubt.world.*;

public class HeadTurret extends TurretType {

    public float reload = 1f;
    public float inaccuracy = 0f;

    public NormalTexture head;

    public HeadTurret(String name) {
        super(name);
    }

    @Override
    public void loadui() {
        head = Textures.loadNormal("sprites/turrets/" + name + "-head");
    }

    public void update(Turret turret) {
        Unit target = Groups.units.min(unit -> unit.dst(turret));
        if (target == null) return;

        turret.rotation = turret.angleTo(target);
    }

    public void draw(Turret turret) {
        Draw.rect(head.region(turret.rotation), turret, 16f, 16f, turret.rot());
    }
}
