package rubt.server;

import arc.backend.headless.HeadlessApplication;
import arc.util.Log;
import arc.util.Threads;
import rubt.client.ClientLauncher;

import static rubt.Vars.*;

public class ServerLauncher extends ClientLauncher {

    public final int port;

    public Server server;
    public Thread thread;

    public static void main(String[] args) {
        try {
            loadLogger(); // pretty print
            new HeadlessApplication(new ServerLauncher(args), Log::err);
        } catch (Throwable error) {
            Log.err("Could not to startup server application", error);
        }
    }

    public ServerLauncher(String[] args) {
        if (args.length == 0) throw new RuntimeException("Need a port as an argument!");
        port = Integer.parseInt(args[0]);
    }

    @Override
    public void init() {
        super.init();

        try {
            server = new Server();
            server.bind(port, port);
            thread = Threads.daemon("Server", server::run);
        } catch (Throwable error) {
            Log.err("Could not to startup server", error);
            exit();
            return;
        }
    }

    @Override
    public void update() {
        server.sync();
    }
}