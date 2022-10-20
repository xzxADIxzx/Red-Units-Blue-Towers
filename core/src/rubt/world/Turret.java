package rubt.world;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.geom.Point2;
import rubt.net.NetObject;
import rubt.net.Packet;
import rubt.net.Packet.*;
import rubt.net.PacketProvider;

import static rubt.Vars.*;

public class Turret extends NetObject implements PacketProvider {

    public Point2 position;
    public float angel;

    public Turret(Point2 position) {
        this.position = position;
        this.angel = 90f; // top direction
    }

    public void draw() {
        Draw.reset();

        Draw.color(Color.green);
        Fill.square(position.x * tilesize, position.y * tilesize, 20f, angel);
    }

    public Packet pack() {
        return new TurretUpdate(this);
    }
}
