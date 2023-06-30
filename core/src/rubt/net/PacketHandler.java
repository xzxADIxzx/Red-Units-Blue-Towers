package rubt.net;

import arc.func.*;
import arc.net.Connection;
import arc.struct.Seq;
import rubt.Groups;
import rubt.logic.Player;
import rubt.net.Packets.Packet;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class PacketHandler {

    public Seq<Handle> handles = new Seq<>();

    public <T extends Packet> void handle(Connection connection, T packet) {
        handles.each(
                handle -> handle.type.isAssignableFrom(packet.getClass()),
                handle -> handle.cons.get(connection, packet) // class cast exception not possible
        );
    }

    public <T extends Packet> void register(Class<T> type, Cons2<Connection, T> cons) {
        handles.add(new Handle(type, cons));
    }

    public <T extends Packet> void register(Class<T> type, Cons<T> cons) {
        register(type, (connection, packet) -> cons.get(packet));
    }

    public <T extends Packet> void registerPlayer(Class<T> type, Cons2<Player, T> cons) {
        register(type, (connection, packet) -> {
            var player = Groups.players.find(p -> p.con == connection);
            if (player != null) cons.get(player, packet);
        });
    }

    public record Handle<T extends Packet> (Class<T> type, Cons2<Connection, T> cons) {}
}
