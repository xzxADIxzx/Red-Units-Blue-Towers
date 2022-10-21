package rubt.desktop;

import arc.ApplicationListener;
import arc.backend.sdl.SdlApplication;
import arc.backend.sdl.SdlConfig;
import arc.backend.sdl.jni.SDL;
import arc.util.Log;

import static rubt.Vars.*;

public class DesktopLauncher implements ApplicationListener {

    public static void main(String[] args) {
        try {
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
}