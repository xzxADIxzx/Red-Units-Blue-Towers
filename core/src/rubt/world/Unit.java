package rubt.world;

import arc.math.Angles;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.util.Time;
import arc.util.Tmp;
import rubt.Groups;
import rubt.net.PacketSerializer.Reads;
import rubt.net.PacketSerializer.Writes;
import rubt.types.UnitType;
import rubt.world.Pathfinder.Path;

import static rubt.Vars.*;

public class Unit extends Body {

    public final UnitType type;

    public Position target;
    public Path path;

    public Vec2 vel = new Vec2();

    public Unit(UnitType type, Position position) {
        super(Groups.units, position);
        this.type = type;

        this.target = position;
    }

    public void update() {
        type.update(this);

        move(vel);
        collisions.checkTile(this); // TODO move to GroundUnit
        collisions.checkUnit(this);
    }

    public void draw() {
        type.draw(this);
    }

    public void drawGlow() {
        type.drawGlow(this);
    }

    public void moveVel(Position to) {
        Tmp.v1.set(to).sub(this);
        Tmp.v1.sub(vel).limit(type.accel);
        vel.add(Tmp.v1).limit(type.speed);
    }

    public void movePath(Position to) {
        Tile target = world.get(to);
        if (target == null) return;

        if (path == null || path.tiles().first() != target)
            path = pathfinder.findPath(tileOn(), target);

        if (path == null) return;
        moveVel(path.nextOnPath(tileOn()));
    }

    public void faceMovement() {
        rotation = Angles.moveToward(rotation, vel.angle(), type.rotateSpeed * Time.delta);
    }

    // region serialization

    public void write(Writes w) {
        w.writePos(this);
        w.writePos(target);
        w.writeFloat(rotation);
    }

    public void read(Reads r) {
        moveTo(r.readPos());
        target = r.readPos();
        rotation = r.readFloat();
    }

    // endregion
}
