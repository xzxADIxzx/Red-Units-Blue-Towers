package rubt.net;

import arc.net.Connection;
import rubt.Groups;
import rubt.net.Packet.*;
import rubt.world.Turret;
import rubt.world.Unit;

public class Send {

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
}
