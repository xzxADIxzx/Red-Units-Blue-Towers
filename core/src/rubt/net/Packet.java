package rubt.net;

import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.net.Connection;
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

        public int state;

        public StateUpdate() {}

        public StateUpdate(State state) {
            this.state = state.ordinal();
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

        public Vec2 position;

        public UnitCreate() {}

        public UnitCreate(Unit unit) {
            this.position = unit.position;
        }
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

    /** Unit data packet used to create new unit on clients. */
    public static class TurretCreate extends Packet {

        public Vec2 position;

        public TurretCreate() {}

        public TurretCreate(Turret turret) {
            this.position = turret.position;
        }
    }

    /** Turret data packet used to update turret state on clients. */
    public static class TurretUpdate extends Packet {

        public int turretID;

        public float angel;

        public TurretUpdate() {}

        public TurretUpdate(Turret turret) {
            this.turretID = turret.id;
            this.angel = turret.angel;
        }
    }
}
