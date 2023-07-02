package rubt.world;

import arc.func.Floatc2;
import arc.math.geom.Position;
import arc.util.Tmp;
import rubt.Groups;

public class Collisions {

    public void check(Hitbox one, Hitbox two, Floatc2 cons) {
        float size = one.size() + two.size();
        float dst = one.dst(two);

        if (dst < size) cons.get(size, dst);
    }

    public void checkTile(Unit unit) {
        Groups.tiles.each(tile -> tile.type.solid, tile -> {
            // check whether the unit collides with the tile
            check(tile, unit, (size, dst) -> {
                // push the unit back
                unit.move(Tmp.v1.set(unit).sub(tile).setLength(size - dst));
            });
        });
    }

    public void checkUnit(Unit unit) {
        Groups.units.each(other -> other.type.flying == unit.type.flying, other -> {
            // check whether units collides with each other
            check(other, unit, (size, dst) -> {
                // push both units back
                Tmp.v1.set(unit).sub(other).setLength((size - dst) / 2f);

                unit.move(Tmp.v1);
                other.move(Tmp.v1.inv());
            });
        });
    }

    public interface Hitbox extends Position {
        float size();
    }
}
