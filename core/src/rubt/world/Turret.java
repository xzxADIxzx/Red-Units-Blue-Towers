package rubt.world;

import arc.graphics.g2d.Draw;
import arc.math.geom.Position;
import rubt.Groups;
import rubt.types.TurretType;

import static rubt.Vars.*;

public class Turret extends Body {

    public final TurretType type;

    public Turret(TurretType type, Position position) {
        super(Groups.turrets, position);
        this.type = type;
    }

    public Turret(TurretType type, int x, int y) {
        super(Groups.turrets, x * tilesize, y * tilesize);
        this.type = type;
    }

    public void update() {
        type.update(this);
    }

    public void draw() {
        Draw.reset();
        type.draw(this);
    }
}
