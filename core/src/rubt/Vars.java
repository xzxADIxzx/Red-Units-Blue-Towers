package rubt;

import arc.net.ArcNet;
import arc.util.Log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Vars {

    public static final String[] tags = { "&lc&fb[D]&fr", "&lb&fb[I]&fr", "&ly&fb[W]&fr", "&lr&fb[E]", "" };
    public static final DateTimeFormatter dateTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static float tilesize = 16f;

    public static void loadLogger() {
        ArcNet.errorHandler = Log::err;
        Log.logger = (level, text) -> { // this is how fashionable i am
            String result = Log.format("&lk&fb[" + dateTime.format(LocalDateTime.now()) + "]&fr " + tags[level.ordinal()] + " " + text + "&fr");
            System.out.println(result);
        };
    }
}
