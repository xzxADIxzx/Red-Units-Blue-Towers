package rubt.world;

import arc.util.Time;
import rubt.Groups;
import rubt.content.TurretTypes;
import rubt.io.Reads;
import rubt.io.Writes;
import rubt.types.ContentType;
import rubt.types.TurretType;

public class Turret extends Body implements ContentType.Provider<Turret> {

    public TurretType type;

    public Unit target;
    public boolean shooting;

    public transient float burstCooldown, shotCooldown;
    public transient int shots;

    public Turret() {
        super(Groups.turrets);
    }

    public ContentType<Turret> type() {
        return type;
    }

    public void update() {
        type.update(this);
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
        lastUpdate = Time.millis();
        rLerp.read(r);
    }

    public void interpolate() {
        rotation = rLerp.getAngel(lastUpdate);
    }

    // endregion
}
