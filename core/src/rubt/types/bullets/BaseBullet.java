package rubt.types.bullets;

import arc.graphics.g2d.Fill;
import rubt.Groups;
import rubt.types.BulletType;
import rubt.world.Bullet;
import rubt.world.Unit;

import static rubt.Vars.*;

public class BaseBullet extends BulletType {

    /** How many units can one bullet pierce. */
    public int pierce = 1;

    public BaseBullet(String name) {
        super(name);
    }

    public void update(Bullet bullet) {
        // physical update
        bullet.moveDelta();
        collisions.checkBullet(bullet);
    }

    public void draw(Bullet bullet) {}

    public void drawGlow(Bullet item) {
        Fill.circle(item.x, item.y, size); // TODO bullet drawer
    }

    public void hit(Bullet bullet, Unit unit) {
        if (bullet.hits.contains(unit)) return; // bullet cannot hit the same unit twice

        unit.health -= bullet.type.damage;
        bullet.hits.add(unit);

        if (bullet.hits.size >= pierce) Groups.bullets.remove(bullet); // TODO effects
    }
}
