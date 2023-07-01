package rubt.types;

import rubt.content.ContentTypes;
import rubt.world.Bullet;

public abstract class BulletType extends ContentType<Bullet> {

    /** Damage the bullet does to units. */
    public float damage;
    /** Bullet collision radius. */
    public float size;
    
    public BulletType(String name) {
        super(ContentTypes.bullets, name);
    }
}
