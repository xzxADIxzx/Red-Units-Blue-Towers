package rubt.world;

import arc.math.geom.Position;
import arc.math.geom.Vec2;
import rubt.Groups;
import rubt.Groups.GroupObject;
import rubt.types.*;

public class Bullet extends GroupObject implements ContentType.Provider<Bullet>, Position {

    public BulletType type;

    public float x, y;
    public Vec2 vel = new Vec2();

    /** For homing. */
    public Unit target;

    public Bullet() {
        super(Groups.bullets);
    }

    public ContentType<Bullet> type() {
        return type;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
