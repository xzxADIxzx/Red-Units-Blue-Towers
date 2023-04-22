package rubt.net;

import arc.math.geom.Position;
import arc.net.Connection;
import rubt.Groups;
import rubt.logic.Player;
import rubt.net.Packets.*;
import rubt.types.*;
import rubt.ui.fragments.JoinFragment;
import rubt.world.*;

import static rubt.Vars.*;

public class Send {

    // region server

    public static void snapshot(short amount, byte[] data) {
        var packet = new Snapshot(amount, data);
        Groups.players.each(packet::sendUDP);
    }

    public static void updateState(Connection connection) {
        new StateUpdate(state).sendTCP(connection);
    }

    public static void createPlayer(Player player) {
        var packet = new PlayerCreate(player);
        Groups.connections.each(packet::sendTCP);
    }

    public static void createPlayer(Connection connection, Player player) {
        new PlayerCreate(player).sendTCP(connection);
    }

    public static void createTile(Connection connection, Tile tile) {
        new TileCreate(tile).sendTCP(connection);
    }

    public static void createUnit(Unit unit) {
        var packet = new UnitCreate(unit);
        Groups.connections.each(packet::sendTCP);
    }

    public static void createUnit(Connection connection, Unit unit) {
        new UnitCreate(unit).sendTCP(connection);
    }

    public static void createTurret(Turret Turret) {
        var packet = new TurretCreate(Turret);
        Groups.connections.each(packet::sendTCP);
    }

    public static void createTurret(Connection connection, Turret Turret) {
        new TurretCreate(Turret).sendTCP(connection);
    }

    // endregion
    // region client

    public static void player() {
        JoinFragment.data.sendTCP(clientCon);
    }

    public static void createUnit(UnitType unit, Position pos) {
        var packet = new UnitCreate();
        packet.type = unit;
        packet.position = pos;
        packet.sendTCP(clientCon);
    }

    public static void createTurret(TurretType turret, Position pos) {
        var packet = new TurretCreate();
        packet.type = turret;
        packet.position = pos;
        packet.sendTCP(clientCon);
    }

    public static void commandUnit(Unit unit, Position target) {
        new CommandUnit(unit, target).sendTCP(clientCon);
    }

    // endregion
}
