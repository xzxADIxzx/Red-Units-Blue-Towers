package rubt.net;

import arc.math.geom.Position;
import arc.net.Connection;
import rubt.Groups;
import rubt.net.Packet.*;
import rubt.types.*;
import rubt.world.*;

import static rubt.Vars.*;

public class Send {

    // region server

    public static void createTile(Connection connection, Tile tile) {
        new TileCreate(tile).sendTCP(connection);
    }

    public static void updateTile(Tile tile) {
        var packet = new TileCreate(tile);
        Groups.connections.each(packet::sendUPD);
    }

    public static void createUnit(Unit unit) {
        var packet = new UnitCreate(unit);
        Groups.connections.each(packet::sendTCP);
    }

    public static void createUnit(Connection connection, Unit unit) {
        new UnitCreate(unit).sendTCP(connection);
    }

    public static void updateUnit(Unit unit) {
        var packet = new UnitUpdate(unit);
        Groups.connections.each(packet::sendUPD);
    }

    public static void createTurret(Turret Turret) {
        var packet = new TurretCreate(Turret);
        Groups.connections.each(packet::sendTCP);
    }

    public static void createTurret(Connection connection, Turret Turret) {
        new TurretCreate(Turret).sendTCP(connection);
    }

    public static void updateTurret(Turret Turret) {
        var packet = new TurretUpdate(Turret);
        Groups.connections.each(packet::sendUPD);
    }

    // endregion
    // region client

    public static void createUnit(UnitType unit, Position pos) {
        new UnitCreate() {{
            type = unit.id;
            position = pos;
        }}.sendTCP(clientCon);
    }

    public static void commandUnit(Unit unit, Position target) {
        unit.target = target;
        new UnitUpdate(unit) {{
            position = null; // save some bytes
        }}.sendTCP(clientCon);
    }

    public static void createTurret(TurretType turret, Position pos) {
        new TurretCreate() {{
            type = turret.id;
            position = pos;
        }}.sendTCP(clientCon);
    }

    // endregion
}
