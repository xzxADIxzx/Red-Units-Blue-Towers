package rubt.world;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.geom.Vec2;
import rubt.net.NetObject;
import rubt.net.Packet;
import rubt.net.Packet.*;
import rubt.net.PacketProvider;

import static rubt.Vars.*;

public class Turret extends NetObject implements PacketProvider {

    public Vec2 position;
    public float angel;

    public Turret(Vec2 position) {
        this.position = position;
        this.angel = 90f; // top direction
    }

    public Turret(int x, int y) {
        this(new Vec2(x * tilesize, y * tilesize));
    }

    public void draw() {
        Draw.reset();

        Draw.color(Color.green);
        Fill.square(position.x, position.y, 20f, angel);
    }

    public Packet pack() {
        return new TurretUpdate(this);
    }
}
