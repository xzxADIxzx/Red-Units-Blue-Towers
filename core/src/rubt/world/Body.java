package rubt.world;

import arc.math.geom.Position;
import arc.struct.Seq;
import rubt.Groups.NetObject;

import static rubt.Vars.*;

/** Implementation of position and rotation over NetObject. */
public abstract class Body extends NetObject {

    public float x, y;
    public float rotation;

    public Body(Seq<? extends NetObject> group) {
        super(group);
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
