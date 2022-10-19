package rubt.net;

import arc.net.Connection;
import arc.util.Strings;
import rubt.world.Turret;
import rubt.world.Unit;

public abstract class Packet {

    private static int idprov;
    public final int id;

    public Packet() {
        this.id = idprov++;
    }

    public void send(Connection connection) {
        connection.sendTCP(this);
    }

    @Override
    public String toString() {
        return Strings.format("[@] @", id, super.toString());
    }

    /** Unit data packet used to update unit state on clients. */
    public static class UnitUpdate extends Packet {

        public final Unit unit;

        public UnitUpdate(Unit unit) {
            this.unit = unit;
        }
    }

    /** Turret data packet used to update turret state on clients. */
    public static class TurretUpdate extends Packet {

        public final Turret turret;

        public TurretUpdate(Turret turret) {
            this.turret = turret;
        }
    }
}
