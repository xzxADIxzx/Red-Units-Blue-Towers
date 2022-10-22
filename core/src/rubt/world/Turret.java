package rubt.world;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.geom.Vec2;
import rubt.Groups;
import rubt.Groups.GroupObject;
import rubt.net.Packet;
import rubt.net.Packet.*;
import rubt.net.PacketProvider;

import static rubt.Vars.*;

public class Turret extends GroupObject implements PacketProvider {

    public Vec2 position;
    public float angel;

    public Turret(Vec2 position) {
        super(Groups.turrets);
        this.position = position;
        this.angel = 90f; // top direction
    }

    public Turret(int x, int y) {
        this(new Vec2(x * tilesize, y * tilesize));
    }

    public void draw() {
        Draw.reset();

        Draw.color(Color.green);
        Fill.square(position.x, position.y, 12f, angel);
    }

    @Override
    public Packet pack() {
        return new TurretUpdate(this);
    }
}
