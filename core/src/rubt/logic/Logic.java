package rubt.logic;

import arc.util.Time;

import static rubt.Vars.*;

public class Logic {

    public static void load() {
        state = State.menu;
    }

    public static void update() {
        Time.update();
    }
}
