package rubt.content;

import arc.struct.Seq;
import rubt.types.TurretType;
import rubt.types.turrets.BulletTurret;

public class TurretTypes {

    public static Seq<TurretType> all = new Seq<>();

    public static BulletTurret imat;

    /** Create turret types. */
    public static void load() {
        imat = new BulletTurret("imat") {{
            health = 100;
            damage = 20;

            reload = 0.8f;
            inaccuracy = 5f;
        }};
    }

    /** Load turret's textures. */
    public static void loadui() {
        all.each(TurretType::loadui);
    }
}
