package rubt.types;

import rubt.content.TurretTypes;
import rubt.world.Turret;

public abstract class TurretType extends ContentType {

    public int health;
    public int damage;

    public TurretType(String name) {
        super(TurretTypes.all, name);
    }

    public abstract void update(Turret turret);

    public abstract void draw(Turret turret);

    public abstract void drawGlow(Turret turret);
}
