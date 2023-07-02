package rubt.world;

import arc.math.Angles;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.util.Time;
import arc.util.Tmp;
import rubt.Groups;
import rubt.content.UnitTypes;
import rubt.io.Reads;
import rubt.io.Writes;
import rubt.types.ContentType;
import rubt.types.UnitType;
import rubt.world.Collisions.Hitbox;
import rubt.world.Pathfinder.Path;

import static rubt.Vars.*;

public class Unit extends Body implements ContentType.Provider<Unit>, Hitbox {

    public UnitType type;

    public Position target;
    public Path path;

    public Vec2 vel = new Vec2();
    public float health;

    public Unit() {
        super(Groups.units);
    }

    public ContentType<Unit> type() {
        return type;
    }

    public float size() {
        return type.size;
    }

    public void update() {
        // logical update
        type.update(this);
        faceMovement();

        // physical update
        move(vel);
        collisions.checkUnit(this);
    }

    // region movement

    public void moveTo(Position to) {
        Tmp.v1.set(to).sub(this);
        Tmp.v1.sub(vel).limit(type.accel);
        vel.add(Tmp.v1).limit(type.speed);
    }

    public void moveTo(Position to, float radius) {
        if (within(to, radius))
            vel.add(Tmp.v1.set(vel).limit(type.accel).inv()); // slow down
        else
            moveTo(to);
    }

    public void movePath(Position to) {
        Tile target = world.get(to);
        if (target == null || target.type.solid) return;

        if (path == null || path.end() != target)
            path = pathfinder.findPath(tileOn(), target);

        if (path == null) return; // no path found
        moveTo(path.nextTile(tileOn()));
    }

    public void faceMovement() {
        if (vel.isZero()) return; // zero vector angle is always zero
        rotation = Angles.moveToward(rotation, vel.angle(), type.rotateSpeed * Time.delta);
    }

    // endregion
    // region serialization

    public void write(Writes w) {
        w.b(type.id);
        w.p(this);
    }

    public void read(Reads r) {
        type = UnitTypes.all.get(r.b());
        set(r.p());
    }

    public void writeSnapshot(Writes w) {
        w.p(this);
        w.f(rotation);
        w.p(target);
    }

    public void readSnapshot(Reads r) {
        lastUpdate = Time.millis();
        xLerp.read(r);
        yLerp.read(r);
        rLerp.read(r);

        target = r.p();
    }

    public void interpolate() {
        x = xLerp.get(lastUpdate);
        y = yLerp.get(lastUpdate);
        rotation = rLerp.getAngel(lastUpdate);
    }

    // endregion
}
