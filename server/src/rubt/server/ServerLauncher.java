package rubt.server;

import arc.backend.headless.HeadlessApplication;
import arc.util.Log;
import rubt.client.ClientLauncher;

import static rubt.Vars.*;

public class ServerLauncher extends ClientLauncher {

    public final int port;

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
}