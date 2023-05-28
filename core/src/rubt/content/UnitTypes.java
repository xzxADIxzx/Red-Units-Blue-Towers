package rubt.content;

import arc.struct.Seq;
import rubt.types.UnitType;
import rubt.types.units.AirUnit;
import rubt.types.units.GroundUnit;

public class UnitTypes {

    public static Seq<UnitType> all = new Seq<>();

    public static AirUnit sunbeam, sunrise, coronalEjection;
    public static GroundUnit furbo;

    /** Create unit types. */
    public static void load() {
        sunbeam = new AirUnit("sunbeam") {{
            health = 100;
            damage = 10;

            speed = 1.7f;
            rotateSpeed = 7f;
            accel = .12f;
            size = 7f;

            addEngine(0f, -5f, 0f);
        }};

        furbo = new GroundUnit("furbo") {{
            health = 200;
            damage = 30;

            speed = 1.2f;
            accel = .1f;
            size = 12f;
        }};
    }

    /** Load units textures. */
    public static void loadui() {
        all.each(UnitType::loadui);
    }
}
