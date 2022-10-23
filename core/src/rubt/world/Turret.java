package rubt.world;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.geom.Vec2;
import rubt.Groups;
import rubt.Groups.GroupObject;
import rubt.net.Packet;
import rubt.net.Packet.*;
import rubt.types.TurretType;
import rubt.net.PacketProvider;

import static rubt.Vars.*;

public class Turret extends GroupObject implements PacketProvider {

    public final TurretType type;

    public Vec2 position;
    public float angel;

    public Turret(TurretType type, Vec2 position) {
        super(Groups.turrets);
        this.type = type;

        this.position = position;
        this.angel = 90f; // top direction
    }

    public Turret(TurretType type, int x, int y) {
        this(type, new Vec2(x * tilesize, y * tilesize));
    }

    public void draw() {
        Draw.reset();

        Draw.color(Color.green);
        Fill.rect(position.x, position.y, tilesize - 4f, tilesize - 4f, angel());
    }

    public float angel() {
        return angel - 90f;
    }

    @Override
    public Packet pack() {
        return new TurretUpdate(this);
    }
}
