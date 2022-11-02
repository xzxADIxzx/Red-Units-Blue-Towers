package rubt.world;

import arc.graphics.g2d.Draw;
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
        type.draw(this);
    }

    @Override
    public Packet pack() {
        return new UnitUpdate(this);
    }
}
