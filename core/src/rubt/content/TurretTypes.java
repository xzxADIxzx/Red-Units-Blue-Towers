package rubt.content;

import arc.struct.Seq;
import rubt.types.TurretType;
import rubt.types.turrets.HeadTurret;

public class TurretTypes {

    public static Seq<TurretType> all = new Seq<>();

    public static HeadTurret dislike, disgust, hatred;
    public static HeadTurret imat;

    /** Create turret types. */
    public static void load() {
        dislike = new HeadTurret("dislike") {{
            range = 36f;

            rotationSpeed = 6f;
            reload = 40f;
        }};

        imat = new HeadTurret("imat") {{
            range = 144f;

            rotationSpeed = 5f;
            reload = 30f;
            burstShoots = 4;
            burstCooldown = 120f;
            inaccuracy = 5f;
        }};
    }

    /** Load turrets textures. */
    public static void loadui() {
        all.each(TurretType::loadui);
    }
}
