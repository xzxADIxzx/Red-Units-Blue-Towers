package rubt.types.bullets;

import arc.graphics.g2d.Fill;
import rubt.Groups;
import rubt.types.BulletType;
import rubt.world.Bullet;
import rubt.world.Unit;

public class BaseBullet extends BulletType {

    /** How many units can one bullet pierce. */
    public int pierce = 1;

    public BaseBullet(String name) {
        super(name);
    }

    public void update(Bullet bullet) {
        bullet.moveDelta();
        // TODO collision
    }

    public void draw(Bullet bullet) {}

    public void drawGlow(Bullet item) {
        Fill.circle(item.x, item.y, size); // TODO bullet drawer
    }

    public void hit(Bullet bullet, Unit unit) {
        unit.health -= bullet.type.damage;
        bullet.hits++;

        if (bullet.hits >= pierce) Groups.bullets.remove(bullet); // TODO effects
    }
}
