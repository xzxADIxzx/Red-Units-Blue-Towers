package rubt.world;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.geom.Position;
import rubt.Groups;
import rubt.net.Packet;
import rubt.net.Packet.*;
import rubt.types.UnitType;
import rubt.net.PacketProvider;

public class Unit extends Body implements PacketProvider {

    public final UnitType type;

    public Position target;

    public Unit(UnitType type, Position position) {
        super(Groups.units, position);
        this.type = type;

        this.target = position;
    }

    public void update() {
        type.update(this);
    }

    public void draw() {
        Draw.reset();

        Draw.color(Color.red);
        Fill.circle(x, y, 10f);

        Draw.color(Color.blue);
        Fill.circle(target.getX(), target.getY(), 10f);
    }

    @Override
    public Packet pack() {
        return new UnitUpdate(this);
    }
}
