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
import rubt.types.UnitType;
import rubt.world.Pathfinder.Path;

import static rubt.Vars.*;

public class Unit extends Body {

    public UnitType type;

    public Position target;
    public Path path;

    public Vec2 vel = new Vec2();

    public Unit() {
        super(Groups.units);
    }

    public void update() {
        // logical update
        type.update(this);
        faceMovement();

        // physical update
        move(vel);
        collisions.checkUnit(this);
    }

    public void draw() {
        type.draw(this);
    }

    public void drawGlow() {
        type.drawGlow(this);
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
        if (target == null) return;

        if (path == null || path.tiles().first() != target)
            path = pathfinder.findPath(tileOn(), target);

        if (path == null) return;
        moveTo(path.nextOnPath(tileOn()));
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
        w.p(target);
        w.f(rotation);
    }

    public void readSnapshot(Reads r) {
        set(r.p());
        target = r.p();
        rotation = r.f();
    }

    // endregion
}
