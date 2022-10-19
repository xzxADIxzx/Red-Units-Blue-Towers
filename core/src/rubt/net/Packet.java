package rubt.net;

import arc.math.geom.Point2;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.net.Connection;
import arc.util.Strings;
import rubt.world.Turret;
import rubt.world.Unit;

public abstract class Packet extends NetObject {

    public void send(Connection connection) {
        connection.sendTCP(this);
    }

    @Override
    public String toString() {
        return Strings.format("[@] @", id, super.toString());
    }

    /** Unit data packet used to update unit state on clients. */
    public static class UnitUpdate extends Packet {

        public int unitID;

        public Vec2 position;
        public Position target;

        public UnitUpdate() {}

        public UnitUpdate(Unit unit) {
            this.unitID = unit.id;
            this.position = unit.position;
            this.target = unit.target;
        }
    }

    /** Turret data packet used to update turret state on clients. */
    public static class TurretUpdate extends Packet {

        public int turretID;

        public Point2 position;
        public float angel;

        public TurretUpdate() {}

        public TurretUpdate(Turret turret) {
            this.turretID = turret.id;
            this.position = turret.position;
            this.angel = turret.angel;
        }
    }
}
