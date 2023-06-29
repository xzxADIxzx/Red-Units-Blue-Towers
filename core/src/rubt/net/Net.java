package rubt.net;

import arc.func.Cons;
import arc.net.Connection;
import arc.struct.Seq;
import arc.util.Log;
import rubt.io.Reads;

import java.io.IOException;
import java.net.*;

public class Net {

    /** The endpoint used to communicate with the network. Usually it is {@link rubt.client.Client}. */
    public static NetProvider provider;

    public static Connection connection() {
        return provider.connection();
    }

    public static boolean connected() {
        return provider.connection().isConnected();
    }

    public static void connect(Host host) throws IOException {
        provider.connect(host.ip, host.port);
    }

    public static void connect(String ip, int port) throws IOException {
        provider.connect(ip, port);
    }

    public static void disconnect() {
        provider.disconnect();
    }

    public static void discover(Cons<Host> cons, Runnable done) {
        Seq<InetAddress> found = new Seq<>();
        provider.discover(packet -> {
            synchronized (found) {
                if (found.contains(address -> address.equals(packet.getAddress()))) return; // how is that possible?
                found.add(packet.getAddress());

                try {
                    String ip = isLocal(packet.getAddress()) ? "localhost" : packet.getAddress().getHostAddress();

                    cons.get(Host.read(ip, Reads.of(packet.getData())));
                } catch (Exception ignored) { // don't crash when there is an error parsing data
                    Log.err("Unable to process server datagram packet", ignored);
                }
            }
        }, done);
    }

    public static void fetchServerInfo(Host host) {
        provider.fetchServerInfo(host.ip, host.port, packet -> {
            try {
                Reads reads = Reads.of(packet.getData());
                reads.skip(4); // skip ip

                host.name = reads.str();
                host.desc = reads.str();
            } catch (Exception ignored) {
                Log.err("Unable to process server datagram packet", ignored);
            }
        });
    }

    public static boolean isLocal(InetAddress addr) {
        if (addr.isAnyLocalAddress() || addr.isLoopbackAddress()) return true;

        try {
            return NetworkInterface.getByInetAddress(addr) != null;
        } catch (Exception ignored) {
            return false;
        }
    }

    /** Any endpoint that allows you to connect to and discover servers. */
    public interface NetProvider {

        /** Connection used to connect to a server. */
        public Connection connection();

        /** Connect to a server. */
        public void connect(String ip, int port) throws IOException;

        /** Disconnect from the server. */
        public void disconnect();

        /**
         * Discover servers.
         * 
         * @param cons is the callback to be called when the server is discovered.
         * @param done is the callback that should run after discovery.
         */
        public void discover(Cons<DatagramPacket> cons, Runnable done);

        /** Fetch data about a specific server. */
        public void fetchServerInfo(String ip, int port, Cons<DatagramPacket> cons);

        public void read(long bytes);
        public void written(long bytes);

        public int packetsRead();
        public int packetsWritten();

        public long bytesRead();
        public long bytesWritten();
    }
}
