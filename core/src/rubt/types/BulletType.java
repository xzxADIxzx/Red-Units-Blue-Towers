package rubt.types;

import rubt.content.ContentTypes;
import rubt.world.*;

public abstract class BulletType extends ContentType<Bullet> {

    /** Damage the bullet does to units. */
    public float damage;
    /** Bullet collision radius. */
    public float size;
    /** Whether the bullet hits the target instantly. Needed for turret logic. */
    public boolean immediately;
    /** Bullet speed. Needed for turret logic. */
    public float speed;

    public BulletType(String name) {
        super(ContentTypes.bullets, name);
    }

    /** Target hitting logic. May differ for a base bullet and a rocket. */
    public abstract void hit(Unit unit);
}
