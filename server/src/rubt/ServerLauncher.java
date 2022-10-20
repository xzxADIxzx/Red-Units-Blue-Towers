package rubt;

import arc.ApplicationListener;
import arc.backend.headless.HeadlessApplication;
import arc.util.Log;

public class ServerLauncher implements ApplicationListener {

    public static void main(String[] args) {
        try {
            Vars.loadLogger();
            new HeadlessApplication(new ServerLauncher(args), Log::err);
        } catch (Throwable error) {
            Log.err("Could not to startup server application", error);
        }
    }

    public ServerLauncher(String[] args) {}
}