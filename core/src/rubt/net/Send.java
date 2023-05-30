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

    public static void worldDataBegin(Connection connection, int amount) {
        new WorldDataBegin(amount).sendTCP(connection);
    }

    public static void worldData(Connection connection, short amount, byte[] data) {
        new WorldData(amount, data).sendTCP(connection);
    }

    public static void state(Connection connection) {
        new UpdateState(state).sendTCP(connection);
    }

    public static void snapshot(short amount, byte[] data) {
        new Snapshot(amount, data).sendUDP();
    }

    public static void player(Player player) {
        new CreatePlayer(player).sendTCP();
    }

    public static void entity(short amount, byte[] data) {
        new CreateEntity(amount, data).sendTCP();
    }

    public static void chatMessage(Player author, String message) {
        new ChatMessage(author.name + "[coral]:[white] " + message).sendTCP();
    }

    // endregion
    // region client

    public static void player() {
        player.sendTCP(clientCon);
    }

    public static void spawnUnit(UnitType type, Position position) {
        new SpawnUnit(type, position).sendTCP(clientCon);
    }

    public static void buildTurret(TurretType type, Tile tile) {
        new BuildTurret(type, tile).sendTCP(clientCon);
    }

    public static void commandUnit(Unit unit, Position target) {
        new CommandUnit(unit, target).sendTCP(clientCon);
    }

    public static void chatMessage(String message) {
        new ChatMessage(message).sendTCP(clientCon);
    }

    // endregion
}
