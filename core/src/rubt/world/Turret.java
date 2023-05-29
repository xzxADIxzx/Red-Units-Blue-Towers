package rubt.world;

import rubt.Groups;
import rubt.content.TurretTypes;
import rubt.net.PacketSerializer.Reads;
import rubt.net.PacketSerializer.Writes;
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
        w.write(type.id);
        w.writePos(this);
    }

    public void read(Reads r) {
        type = TurretTypes.all.get(r.readByte());
        set(r.readPos());
    }

    public void writeSnapshot(Writes w) {
        w.writeFloat(rotation);
    }

    public void readSnapshot(Reads r) {
        rotation = r.readFloat();
    }

    // endregion
}
