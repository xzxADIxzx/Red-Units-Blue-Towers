package rubt.world;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.geom.Point2;

public class Turret {

    public Point2 position;
    public float angel;

    public Turret(Point2 position) {
        this.position = position;
        this.angel = 90f; // top direction
    }

    public void draw() {
        Draw.reset();

        Draw.color(Color.green);
        Fill.square(position.x, position.y, 20f, angel);
    }
}
