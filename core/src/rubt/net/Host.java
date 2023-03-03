package rubt.net;

import rubt.Vars;

import java.nio.ByteBuffer;

/** Represents a remote server. */
public class Host {

    public final String ip;
    public final int port;

    public String name, desc;

    public Host(String ip, int port) {
        if (ip.isBlank()) throw new RuntimeException("IP address cannot be blank!");

        this.ip = ip;
        this.port = port;
    }

    // region info

    public void fetchServerInfo() {}

    public String address() {
        return ip + ":" + port;
    }

    public String name() {
        return name == null ? "Name not provided" : name;
    }

    public String desc() {
        return desc == null ? "Description not provided." : desc;
    }

    public String enemy() {
        return "[gray]No one is on the server right now.";
    }

    // endregion
    // region serialization

    public void write(ByteBuffer buffer) {
        PacketSerializer.writeString(buffer, name);
        PacketSerializer.writeString(buffer, desc);
    }

    public static Host read(String ip, ByteBuffer buffer) {
        return new Host(ip, Vars.port) {{
            name = PacketSerializer.readString(buffer);
            desc = PacketSerializer.readString(buffer);
        }};
    }

    // endregion
}
