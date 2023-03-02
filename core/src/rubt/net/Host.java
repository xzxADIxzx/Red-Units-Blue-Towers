package rubt.net;

import java.io.IOException;

/** Represents a remote server. */
public class Host {

    /** The endpoint used to connect. Usually it is {@link rubt.client.Client}. */
    public static Connector connector;

    public final String ip;
    public final int port;

    public Host(String ip, int port) {
        if (ip.isBlank()) throw new RuntimeException("IP address cannot be blank!");

        this.ip = ip;
        this.port = port;
    }

    public void join() throws IOException {
        connector.connect(ip, port);
    }

    public void ping() {}

    /** Any endpoint capable of connecting. */
    public interface Connector {

        public void connect(String ip, int port) throws IOException;

        public void disconnect();
    }
}
