package rubt.net;

import java.util.NoSuchElementException;

import arc.func.Cons2;
import arc.net.Connection;
import arc.struct.Seq;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class PacketHandler {

    public Seq<Handle> handles = new Seq<>();

    public <T extends Packet> void handle(Connection connection, T packet) throws NoSuchElementException {
        handles.each(
                handle -> handle.type.isAssignableFrom(packet.getClass()),
                handle -> handle.cons.get(connection, packet) // class cast exception not possible
        );
    }

    public <T extends Packet> void register(Class<T> type, Cons2<Connection, T> cons) {
        handles.add(new Handle(type, cons));
    }

    public record Handle<T extends Packet> (Class<T> type, Cons2<Connection, T> cons) {}
}
