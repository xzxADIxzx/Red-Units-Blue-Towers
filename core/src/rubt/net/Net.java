package rubt.net;

import java.io.IOException;

public class Net {

    /** The endpoint used to communicate with the network. Usually it is {@link rubt.client.Client}. */
    public static NetProvider provider;

    /** Any endpoint that allows you to connect to and discover servers. */
    public interface NetProvider {

        /** Connect to a server. */
        public void connect(String ip, int port) throws IOException;

        /** Disconnect from the server. */
        public void disconnect();
    }
}
