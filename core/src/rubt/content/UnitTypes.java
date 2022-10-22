package rubt.content;

import arc.struct.Seq;
import rubt.types.UnitType;
import rubt.types.units.AirUnit;

public class UnitTypes {

    public static Seq<UnitType> all = new Seq<>();

    public static AirUnit imau;

    /** Create unit types. */
    public static void load() {
        imau = new AirUnit("imau") {{
            health = 100;
            damage = 10;

            speed = 10f;
        }};
    }

    /** Load unit's textures. */
    public static void loadui() {
        all.each(UnitType::loadui);
    }
}
