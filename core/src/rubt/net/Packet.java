package rubt.net;

import arc.math.geom.Position;
import arc.net.Connection;
import rubt.content.TurretTypes;
import rubt.content.UnitTypes;
import rubt.logic.State;
import rubt.world.*;

public abstract class Packet {

    public void sendTCP(Connection connection) {
        connection.sendTCP(this);
    }

    public void sendUPD(Connection connection) {
        connection.sendUDP(this);
    }

    /** Packet used to update game state on clients. */
    public static class StateUpdate extends Packet {

        public int id;

        public StateUpdate() {}

        public StateUpdate(State state) {
            this.id = state.ordinal();
        }
    }

    /** Tile data packet used to upload tile to clients. */
    public static class TileCreate extends Packet {

        public int x, y;

        public TileCreate() {}

        public TileCreate(Tile tile) {
            this.x = tile.x;
            this.y = tile.y;
        }

        public void execute() {
            new Tile(x, y);
        }
    }

    /** Tile data packet used to update tile state on clients. */
    public static class TileUpdate extends Packet {

        public TileUpdate() {}

        public TileUpdate(Tile tile) {
            // there is nothing for now, because tile has not state
        }
    }

    /** Unit data packet used to create new unit on clients. */
    public static class UnitCreate extends Packet {

        public int type;
        public Position position;

        public UnitCreate() {}

        public UnitCreate(Unit unit) {
            this.type = unit.type.id;
            this.position = unit;
        }

        public void execute() {
            new Unit(UnitTypes.all.get(type), position);
        }
    }

    /** Unit data packet used to update unit state on clients. */
    public static class UnitUpdate extends Packet {

        public int unitID;

        public Position position;
        public Position target;

        public UnitUpdate() {}

        public UnitUpdate(Unit unit) {
            this.unitID = unit.id;
            this.position = unit;
            this.target = unit.target;
        }
    }

    /** Unit data packet used to create new unit on clients. */
    public static class TurretCreate extends Packet {

        public int type;
        public Position position;

        public TurretCreate() {}

        public TurretCreate(Turret turret) {
            this.type = turret.type.id;
            this.position = turret;
        }

        public void execute() {
            new Turret(TurretTypes.all.get(type), position);
        }
    }

    /** Turret data packet used to update turret state on clients. */
    public static class TurretUpdate extends Packet {

        public int turretID;

        public float rotation;

        public TurretUpdate() {}

        public TurretUpdate(Turret turret) {
            this.turretID = turret.id;
            this.rotation = turret.rotation;
        }
    }
}
