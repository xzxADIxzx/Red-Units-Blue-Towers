package rubt.content;

import arc.struct.Seq;
import rubt.types.TurretType;
import rubt.types.bullets.BaseBullet;
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

            bullet = new BaseBullet("dislike-bullet") {{
                damage = 1f;
                size = 1f;
                speed = 1f;
            }};
        }};

        imat = new HeadTurret("imat") {{
            range = 144f;

            rotationSpeed = 5f;
            reload = 30f;
            burstShoots = 4;
            burstCooldown = 120f;
            inaccuracy = 5f;

            bullet = new BaseBullet("imat-bullet") {{
                damage = 3f;
                size = 1.8f;
                speed = 3f;
                pierce = 2;
            }};
        }};
    }

    /** Load turrets textures. */
    public static void loadui() {
        all.each(TurretType::loadui);
    }
}
