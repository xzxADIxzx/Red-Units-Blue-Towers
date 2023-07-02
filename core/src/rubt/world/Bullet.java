package rubt.world;

import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.util.Time;
import rubt.Groups;
import rubt.Groups.GroupObject;
import rubt.types.*;
import rubt.world.Collisions.Hitbox;

public class Bullet extends GroupObject implements ContentType.Provider<Bullet>, Hitbox {

    public BulletType type;

    public float x, y;
    public Vec2 vel;

    /** For homing. */
    public Unit target;
    /** How many opponents were hit by the bullet. */
    public int hits;

    public Bullet(BulletType type, Position position, Vec2 velocity, Unit target) {
        super(Groups.bullets);

        this.type = type;
        this.x = position.getX();
        this.y = position.getY();
        this.vel = velocity;
        this.target = target;
    }

    public Bullet(BulletType type, Position position, Unit target) {
        this(type, position, new Vec2(), target);
    }

    public ContentType<Bullet> type() {
        return type;
    }

    public float size() {
        return type.size;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void moveDelta() {
        x += vel.x * Time.delta;
        y += vel.y * Time.delta;
    }
}
