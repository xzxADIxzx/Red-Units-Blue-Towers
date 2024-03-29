package rubt;

import arc.net.ArcNet;
import arc.util.Log;
import arc.util.Strings;
import rubt.editor.MapEditor;
import rubt.graphics.Renderer;
import rubt.input.InputHandler;
import rubt.logic.*;
import rubt.ui.UI;
import rubt.world.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Vars {

    public static final String[] tags = { "&lc&fb[D]&fr", "&lb&fb[I]&fr", "&ly&fb[W]&fr", "&lr&fb[E]", "" };
    public static final DateTimeFormatter dateTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static final float tilesize = 12f;
    public static final int maxSnapshotSize = 1200;
    public static final int maxNameLength = 80;
    public static final int maxMessageLength = 128;

    public static final String multicast = "227.2.2.7";
    public static final int multicastPort = 2727;
    public static int port = 4755;

    public static boolean headless;
    public static boolean mobile;

    public static State state = State.menu;
    public static Rules rules = new Rules();
    public static World world = new World();

    public static Pathfinder pathfinder = new Pathfinder();
    public static Collisions collisions = new Collisions();

    public static Renderer renderer;
    public static UI ui;
    public static InputHandler handler;
    public static MapEditor editor = new MapEditor();

    public static Thread thread;
    public static Player player;

    public static void loadLogger() {
        ArcNet.errorHandler = ex -> Log.debug(Strings.getStackTrace(ex));
        Log.logger = (level, text) -> { // this is how fashionable I am
            String result = Log.format("&lk&fb[" + dateTime.format(LocalDateTime.now()) + "]&fr " + tags[level.ordinal()] + " " + text + "&fr");
            System.out.println(result);
        };
    }
}
