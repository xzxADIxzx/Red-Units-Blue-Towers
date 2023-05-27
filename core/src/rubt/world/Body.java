package rubt.world;

import arc.math.geom.Position;
import arc.struct.Seq;
import rubt.Groups.NetObject;

import static rubt.Vars.*;

/** 2D vector but also a NetObject. */
public abstract class Body extends NetObject implements Position {

    public float x, y;
    public float rotation;

    public Body(Seq<? extends NetObject> group, float x, float y) {
        super(group);
        set(x, y);
    }

    public Body(Seq<? extends NetObject> group, Position position) {
        this(group, position.getX(), position.getY());
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    public float rot() {
        return rotation - 90f;
    }

    public Tile tileOn() {
        return world.get(x, y);
    }

    public void set(Position position) {
        set(position.getX(), position.getY());
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void move(Position position) {
        move(position.getX(), position.getY());
    }

    public void move(float x, float y) {
        this.x += x;
        this.y += y;
    }
}
