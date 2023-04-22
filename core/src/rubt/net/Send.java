package rubt.net;

import arc.math.geom.Position;
import arc.net.Connection;
import rubt.Groups;
import rubt.logic.Player;
import rubt.net.Packets.*;
import rubt.types.*;
import rubt.world.*;

import static rubt.Vars.*;

public class Send {

    // region server

    public static void snapshot(short amount, byte[] data) {
        var packet = new Snapshot(amount, data);
        Groups.players.each(packet::sendUDP);
    }

    public static void updateState(Connection connection) {
        new UpdateState(state).sendTCP(connection);
    }

    public static void createPlayer(Player player) {
        var packet = new CreatePlayer(player);
        Groups.connections.each(packet::sendTCP);
    }

    public static void createPlayer(Connection connection, Player player) {
        new CreatePlayer(player).sendTCP(connection);
    }

    public static void createTile(Connection connection, Tile tile) {
        new CreateTile(tile).sendTCP(connection);
    }

    public static void createUnit(Unit unit) {
        var packet = new CreateUnit(unit);
        Groups.connections.each(packet::sendTCP);
    }

    public static void createUnit(Connection connection, Unit unit) {
        new CreateUnit(unit).sendTCP(connection);
    }

    public static void createTurret(Turret Turret) {
        var packet = new CreateTurret(Turret);
        Groups.connections.each(packet::sendTCP);
    }

    public static void createTurret(Connection connection, Turret Turret) {
        new CreateTurret(Turret).sendTCP(connection);
    }

    // endregion
    // region client

    public static void player() {
        player.sendTCP(clientCon);
    }

    public static void createUnit(UnitType unit, Position pos) {
        var packet = new CreateUnit();
        packet.type = unit;
        packet.position = pos;
        packet.sendTCP(clientCon);
    }

    public static void createTurret(TurretType turret, Position pos) {
        var packet = new CreateTurret();
        packet.type = turret;
        packet.position = pos;
        packet.sendTCP(clientCon);
    }

    public static void commandUnit(Unit unit, Position target) {
        new CommandUnit(unit, target).sendTCP(clientCon);
    }

    // endregion
}
