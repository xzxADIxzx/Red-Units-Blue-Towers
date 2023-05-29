package rubt.net;

import rubt.Vars;
import rubt.io.Reads;
import rubt.io.Writes;

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

    public static void write(Writes w) { // TODO take this value from settings
        w.i(Vars.port);
        w.str("Test Server");
        w.str("He's testing a game, be quiet...");
    }

    public static Host read(String ip, Reads r) {
        return new Host(ip, r.i()) {{
            name = r.str();
            desc = r.str();
        }};
    }

    // endregion
}
