package rubt.net;

import arc.func.Cons;
import arc.struct.Seq;
import arc.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;

public class Net {

    /** The endpoint used to communicate with the network. Usually it is {@link rubt.client.Client}. */
    public static NetProvider provider;

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
                    ByteBuffer buffer = ByteBuffer.wrap(packet.getData());

                    cons.get(Host.read(ip, buffer));
                } catch (Exception ignored) { // don't crash when there is an error parsing data
                    Log.err("Unable to process server datagram packet", ignored);
                }
            }
        }, done);
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
    }
}
