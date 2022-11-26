package rubt;

import arc.net.ArcNet;
import arc.net.Connection;
import arc.util.Log;
import rubt.graphics.Renderer;
import rubt.input.InputHandler;
import rubt.logic.State;
import rubt.world.Pathfinder;
import rubt.world.World;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Vars {

    public static final String[] tags = { "&lc&fb[D]&fr", "&lb&fb[I]&fr", "&ly&fb[W]&fr", "&lr&fb[E]", "" };
    public static final DateTimeFormatter dateTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static final float tilesize = 16f;

    public static boolean headless;
    public static boolean mobile;

    public static State state = State.menu;
    public static World world;
    public static Connection clientCon;

    public static Renderer renderer;
    public static InputHandler handler;
    public static Pathfinder pathfinder;

    public static void loadLogger() {
        ArcNet.errorHandler = Log::err;
        Log.logger = (level, text) -> { // this is how fashionable i am
            String result = Log.format("&lk&fb[" + dateTime.format(LocalDateTime.now()) + "]&fr " + tags[level.ordinal()] + " " + text + "&fr");
            System.out.println(result);
        };
    }
}
