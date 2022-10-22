package rubt.world;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import rubt.Groups;
import rubt.Groups.GroupObject;
import rubt.net.Packet;
import rubt.net.Packet.*;
import rubt.net.PacketProvider;

public class Unit extends GroupObject implements PacketProvider {

    public Vec2 position;
    public Position target;

    public Unit(Vec2 position) {
        super(Groups.units);
        this.position = position;
        this.target = new Vec2();
    }

    public void draw() {
        Draw.reset();

        Draw.color(Color.red);
        Fill.circle(position.x, position.y, 10f);

        Draw.color(Color.blue);
        Fill.circle(target.getX(), target.getY(), 10f);
    }

    @Override
    public Packet pack() {
        return new UnitUpdate(this);
    }
}
