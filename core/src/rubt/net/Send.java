package rubt.net;

import arc.math.geom.Position;
import arc.net.Connection;
import rubt.logic.Player;
import rubt.net.Packets.*;
import rubt.types.*;
import rubt.world.*;

import static rubt.Vars.*;

public class Send {

    // region server

    public static void snapshot(short amount, byte[] data) {
        new Snapshot(amount, data).sendUDP();
    }

    public static void worldDataBegin(Connection connection, int amount) {
        new WorldDataBegin(amount).sendTCP(connection);
    }

    public static void worldData(Connection connection, short amount, byte[] data) {
        new WorldData(amount, data).sendTCP(connection);
    }

    public static void updateState(Connection connection) {
        new UpdateState(state).sendTCP(connection);
    }

    public static void createPlayer(Player player) {
        new CreatePlayer(player).sendTCP();
    }

    public static void createPlayer(Connection connection, Player player) {
        new CreatePlayer(player).sendTCP(connection);
    }

    public static void chatMessage(Player author, String message) {
        new ChatMessage(author.name + "[coral]:[white] " + message).sendTCP();
    }

    // endregion
    // region client

    public static void player() {
        player.sendTCP(clientCon);
    }

    public static void createUnit(UnitType unit, Position pos) {/*
        var packet = new CreateUnit();
        packet.type = unit;
        packet.position = pos;
        packet.sendTCP(clientCon);
    */}

    public static void createTurret(TurretType turret, Position pos) {/*
        var packet = new CreateTurret();
        packet.type = turret;
        packet.position = pos;
        packet.sendTCP(clientCon);
    */}

    public static void commandUnit(Unit unit, Position target) {
        new CommandUnit(unit, target).sendTCP(clientCon);
    }

    public static void chatMessage(String message) {
        new ChatMessage(message).sendTCP(clientCon);
    }

    // endregion
}
