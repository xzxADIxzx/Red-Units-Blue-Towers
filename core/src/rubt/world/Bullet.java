package rubt.world;

import rubt.Groups;
import rubt.Groups.GroupObject;
import rubt.types.*;

public class Bullet extends GroupObject implements ContentType.Provider<Bullet> {

    public BulletType type;

    public Bullet() {
        super(Groups.bullets);
    }

    public ContentType<Bullet> type() {
        return type;
    }
}
