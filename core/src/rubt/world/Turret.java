package rubt.world;

import rubt.Groups;
import rubt.content.TurretTypes;
import rubt.io.Reads;
import rubt.io.Writes;
import rubt.types.TurretType;

public class Turret extends Body {

    public TurretType type;

    public Turret() {
        super(Groups.turrets);
    }

    public void update() {
        type.update(this);
    }

    public void draw() {
        type.draw(this);
    }

    public void drawGlow() {
        type.drawGlow(this);
    }

    // region serialization

    public void write(Writes w) {
        w.b(type.id);
        w.p(this);
    }

    public void read(Reads r) {
        type = TurretTypes.all.get(r.b());
        set(r.p());
    }

    public void writeSnapshot(Writes w) {
        w.f(rotation);
    }

    public void readSnapshot(Reads r) {
        rotation = r.f();
    }

    // endregion
}
