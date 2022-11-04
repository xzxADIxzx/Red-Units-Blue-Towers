package rubt.world;

import arc.graphics.g2d.Draw;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.util.Tmp;
import rubt.Groups;
import rubt.net.Packet;
import rubt.net.Packet.*;
import rubt.types.UnitType;
import rubt.net.PacketProvider;

public class Unit extends Body implements PacketProvider {

    public final UnitType type;

    public Position target;
    public Vec2 vel = new Vec2();

    public Unit(UnitType type, Position position) {
        super(Groups.units, position);
        this.type = type;

        this.target = position;
    }

    public void update() {
        type.update(this);
        move(vel);
    }

    public void draw() {
        Draw.reset();
        type.draw(this);
    }

    public void moveVel(Position to) {
        Tmp.v1.set(to).sub(this);
        Tmp.v1.sub(vel).limit(type.accel);
        vel.add(Tmp.v1).limit(type.speed);
    }

    @Override
    public Packet pack() {
        return new UnitUpdate(this);
    }
}
