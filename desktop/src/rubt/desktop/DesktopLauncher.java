package rubt.desktop;

import arc.backend.sdl.SdlApplication;
import arc.backend.sdl.SdlConfig;
import arc.backend.sdl.jni.SDL;
import arc.util.Log;
import rubt.client.Client;
import rubt.client.ClientLauncher;
import rubt.logic.Logic;
import rubt.net.Host;

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

    public DesktopLauncher(String[] args) {}

    public static void crashed(String title, Throwable error) {
        Log.err(title, error);
        SDL.SDL_ShowSimpleMessageBox(SDL.SDL_MESSAGEBOX_ERROR, title, error.getMessage());
    }

    @Override
    public void init() {
        super.init();

        clientCon = client = new Client();
        Host.connector = client;

        Log.infoTag("APP", "Client loaded successfully.");
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