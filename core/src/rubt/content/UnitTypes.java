package rubt.content;

import arc.struct.Seq;
import rubt.types.UnitType;
import rubt.types.units.AirUnit;
import rubt.types.units.GroundUnit;

public class UnitTypes {

    public static Seq<UnitType> all = new Seq<>();

    public static AirUnit imau;
    public static GroundUnit furbo;

    /** Create unit types. */
    public static void load() {
        imau = new AirUnit("imau") {{
            health = 100;
            damage = 10;

            speed = 2f;
            accel = .12f;
        }};

        furbo = new GroundUnit("furbo") {{
            health = 200;
            damage = 30;

            speed = 1.2f;
            accel = .1f;
        }};
    }

    /** Load unit's textures. */
    public static void loadui() {
        all.each(UnitType::loadui);
    }
}
