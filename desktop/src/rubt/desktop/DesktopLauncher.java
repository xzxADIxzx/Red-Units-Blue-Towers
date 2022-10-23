package rubt.desktop;

import arc.backend.sdl.SdlApplication;
import arc.backend.sdl.SdlConfig;
import arc.backend.sdl.jni.SDL;
import arc.util.Log;
import arc.util.Threads;
import rubt.Renderer;
import rubt.client.Client;
import rubt.client.ClientLauncher;

import static rubt.Vars.*;

public class DesktopLauncher extends ClientLauncher {

    public Client client;
    public Thread thread;

    public static void main(String[] args) {
        try {
            headless = false;
            loadLogger(); // needed only for debug
            new SdlApplication(new DesktopLauncher(args), new SdlConfig() {{
                title = "Red Units Blue Towers";
                maximized = true;
            }});
        } catch (Throwable error) {
            crushed("Oh no, critical error", error);
        }
    }

    public static void crushed(String title,Throwable error) {
        Log.err(title, error);
        SDL.SDL_ShowSimpleMessageBox(SDL.SDL_MESSAGEBOX_ERROR, title, error.getMessage());
    }

    public DesktopLauncher(String[] args) {}

    @Override
    public void init() {
        super.init();

        client = new Client();

        Log.infoTag("APP", "Client loaded successfully.");
    }

    @Override
    public void update() {
        Renderer.draw();
    }
}