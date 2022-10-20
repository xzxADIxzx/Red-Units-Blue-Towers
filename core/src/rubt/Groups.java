package rubt;

import arc.struct.Seq;
import rubt.world.Turret;
import rubt.world.Unit;

public class Groups {

    public static Seq<Unit> units = new Seq<>();
    public static Seq<Turret> turrets = new Seq<>();

    public static void clear() {
        units.clear();
        turrets.clear();
    }
}
