package rubt.server;

import arc.backend.headless.HeadlessApplication;
import arc.util.Log;
import arc.util.Threads;
import rubt.client.ClientLauncher;
import rubt.logic.Logic;
import rubt.logic.State;
import rubt.world.World;

import static rubt.Vars.*;

public class ServerLauncher extends ClientLauncher {

    public Server server;

    public static void main(String[] args) {
        try {
            headless = true;
            loadLogger(); // pretty print
            new HeadlessApplication(new ServerLauncher(args), Log::err);
        } catch (Throwable error) {
            Log.err("Could not to startup server application", error);
        }
    }

    public ServerLauncher(String[] args) {
        if (args.length != 0) port = Integer.parseInt(args[0]);
    }

    @Override
    public void init() {
        super.init();

        try {
            server = new Server();
            server.bind(port, port);
            thread = Threads.daemon("Server", server::run);

            world.load(World.random());
            state = State.lobby;
        } catch (Throwable error) {
            Log.err("Could not to startup server", error);
            exit();
            return;
        }

        Log.infoTag("APP", "Server started successfully.");
    }

    @Override
    public void update() {
        Logic.update();
        server.sync();

        limitFPS(60);
    }
}