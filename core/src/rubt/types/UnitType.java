package rubt.types;

import arc.math.geom.Vec2;
import arc.math.geom.Vec3;
import arc.struct.Seq;
import rubt.content.UnitTypes;
import rubt.world.Unit;

public abstract class UnitType extends ContentType {

    public int health;
    public int damage;

    public float speed, rotateSpeed;
    public float accel;

    /** Unit collision radius. */
    public float size;
    /** Whether unit is flying. */
    public boolean flying;

    public Seq<Vec3> engines = new Seq<>(0);
    public Seq<Vec2> airbags = new Seq<>(0);

    public UnitType(String name) {
        super(UnitTypes.all, name);
    }

    public abstract void update(Unit unit);

    public abstract void draw(Unit unit);

    public abstract void drawGlow(Unit unit);

    public void addEngine(float x, float y, float rotation) {
        engines.add(new Vec3(x, y, rotation));
    }

    public void addAirbag(float x, float y) {
        airbags.add(new Vec2(x, y));
    }
}
