package rubt.world;

import arc.util.Tmp;
import rubt.Groups;

import static rubt.Vars.*;

public class Collisions {

    public void checkTile(Unit unit) {
        Groups.tiles.each(tile -> tile.within(unit, tilesize + unit.type.size) && tile.type.solid, tile -> {

            // check whether unit collides with tile
            float x = Math.max(tile.getX() - 8f, Math.min(unit.x, tile.getX() + 8f));
            float y = Math.max(tile.getY() - 8f, Math.min(unit.y, tile.getY() + 8f));

            float dst = unit.dst(x, y);
            if (dst > unit.type.size) return;

            // push unit back
            unit.move(Tmp.v1.set(unit).sub(x, y).setLength(unit.type.size - dst));
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
