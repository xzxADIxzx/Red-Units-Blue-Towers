package rubt.world;

import arc.math.Mathf;
import arc.util.Tmp;
import rubt.Groups;

import static rubt.Vars.*;

public class Collisions {

    public void checkTile(Unit unit) {
        Groups.tiles.each(tile -> tile.within(unit, unit.type.size + tilesize + 1f) && tile.type.solid, tile -> {

            // check whether unit collides with tile
            Tmp.v1.set(unit).sub(tile);

            float size = unit.type.size + tilesize + Mathf.cosDeg(Tmp.v1.angle() * 6f);
            float dst = Tmp.v1.len();

            if (dst > size) return;

            // push unit back
            unit.move(Tmp.v1.setLength(size - dst));
        });
    }

    public void checkUnit(Unit unit) {
        Groups.units.each(other -> other.type.flying == unit.type.flying, other -> {

            // check whether units collides with each other
            float size = unit.type.size + other.type.size;
            float dst = unit.dst(other);

            if (dst > size) return;

            // push both units back
            Tmp.v1.set(unit).sub(other).setLength((size - dst) / 2f);

            unit.move(Tmp.v1);
            other.move(Tmp.v1.inv());
        });
    }
}
