package rubt.client;

import arc.net.Connection;
import arc.net.DcReason;
import arc.net.NetListener;
import rubt.Groups;
import rubt.content.TurretTypes;
import rubt.content.UnitTypes;
import rubt.logic.State;
import rubt.net.PacketSerializer;
import rubt.net.Packet.*;
import rubt.world.*;

import static rubt.Vars.*;

public class Client extends arc.net.Client implements NetListener {

    public Client() {
        super(8192, 8192, new PacketSerializer());
        addListener(this);
    }

    // region listeners

    public void connected(Connection connection) {}

    public void disconnected(Connection connection, DcReason reason) {}

    public void received(Connection connection, Object object) {
        if (object instanceof StateUpdate update) {
            state = State.values()[update.state];
        }

        else if (object instanceof TileCreate create)
            new Tile(create.x, create.y);
        else if (object instanceof TileUpdate update) {
            // nothing, because tile update class is empty
        }

        else if (object instanceof UnitCreate create)
            new Unit(UnitTypes.all.get(create.type), create.position);
        else if (object instanceof UnitUpdate update) {
            Unit unit = Groups.units.get(update.unitID);
            unit.position = update.position;
            unit.target = update.target;
        }

        else if (object instanceof TurretCreate create)
            new Turret(TurretTypes.all.get(create.type), create.position);
        else if (object instanceof TurretUpdate update) {
            Turret turret = Groups.turrets.get(update.turretID);
            turret.angel = update.angel;
        }
    }

    public void idle(Connection connection) {}

    // endregion
}