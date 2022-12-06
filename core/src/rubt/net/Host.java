package rubt.net;

import java.io.IOException;

public class Host {

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

    public interface Connector {

        public void connect(String ip, int port) throws IOException;

        public void disconnect();
    }
}
