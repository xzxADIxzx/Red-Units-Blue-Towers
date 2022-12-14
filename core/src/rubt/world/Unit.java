package rubt.world;

import arc.graphics.g2d.Draw;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.util.Tmp;
import rubt.Groups;
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
    }

    public void draw() {
        Draw.reset();
        type.draw(this);
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
}
