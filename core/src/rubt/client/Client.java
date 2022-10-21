package rubt.client;

import arc.net.Connection;
import arc.net.DcReason;
import arc.net.NetListener;
import rubt.Groups;
import rubt.net.PacketSerializer;
import rubt.net.Packet.*;
import rubt.world.*;

public class Client extends arc.net.Client implements NetListener {

    public Client() {
        super(8192, 8192, new PacketSerializer());
        addListener(this);
    }

    // region listeners

    public void connected(Connection connection) {}

    public void disconnected(Connection connection, DcReason reason) {}

    public void received(Connection connection, Object object) {
        if (object instanceof TileCreate create)
            Groups.tiles.add(new Tile(create.tileID, create.x, create.y));
        else if (object instanceof TileUpdate update) {} // nothing, because tile update class is empty

        if (object instanceof UnitCreate create)
            Groups.units.add(new Unit(create.unitID, create.position));
        else if (object instanceof UnitUpdate update) {
            Unit unit = Groups.units.find(u -> u.id == update.unitID);
            if (unit == null) return;
            unit.position = update.position;
            unit.target = update.target;
        }

        if (object instanceof TurretCreate create)
            Groups.turrets.add(new Turret(create.turretID, create.position));
        else if (object instanceof TurretUpdate update) {
            Turret turret = Groups.turrets.find(t -> t.id == update.turretID);
            if (turret == null) return;
            turret.angel = update.angel;
        }
    }

    public void idle(Connection connection) {}

    // endregion
}