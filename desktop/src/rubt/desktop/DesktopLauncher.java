package rubt.desktop;

import arc.backend.sdl.SdlApplication;
import arc.backend.sdl.SdlConfig;
import arc.backend.sdl.jni.SDL;
import arc.util.Log;
import arc.util.Log.LogLevel;
import rubt.client.Client;
import rubt.client.ClientLauncher;
import rubt.logic.Logic;
import rubt.net.Net;

import static rubt.Vars.*;

public class DesktopLauncher extends ClientLauncher {

    public Client client;

    public static void main(String[] args) {
        try {
            headless = false;
            loadLogger(); // needed only for debug
            new SdlApplication(new DesktopLauncher(args), new SdlConfig() {{
                title = "Red Units Blue Towers";
                maximized = true;
            }});
        } catch (Throwable error) {
            crashed("Oh no, critical error", error);
        }
    }

    public DesktopLauncher(String[] args) {
        if (args.length != 0 && args[0].equals("-debug")) Log.level = LogLevel.debug;
    }

    public static void crashed(String title, Throwable error) {
        Log.err(title, error);
        SDL.SDL_ShowSimpleMessageBox(SDL.SDL_MESSAGEBOX_ERROR, title, error.getMessage());
    }

    @Override
    public void init() {
        super.init();

        clientCon = client = new Client();
        Net.provider = client;

        Log.infoTag("App", "Client loaded successfully.");
    }

    @Override
    public void update() {
        Logic.update();
        handler.update();
        renderer.draw();

        limitFPS(60);
    }

    @Override
    public void resize(int width, int height) {
        ui.resize(width, height);
    }
}