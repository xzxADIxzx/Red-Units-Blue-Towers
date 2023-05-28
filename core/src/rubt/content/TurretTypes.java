package rubt.content;

import arc.struct.Seq;
import rubt.types.TurretType;
import rubt.types.turrets.HeadTurret;

public class TurretTypes {

    public static Seq<TurretType> all = new Seq<>();

    public static HeadTurret imat;

    /** Create turret types. */
    public static void load() {
        imat = new HeadTurret("imat") {{
            health = 100;
            damage = 20;

            reload = 0.8f;
            inaccuracy = 5f;
        }};
    }

    /** Load turrets textures. */
    public static void loadui() {
        all.each(TurretType::loadui);
    }
}
