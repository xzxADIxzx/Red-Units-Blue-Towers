package rubt.logic;

import arc.util.Time;
import rubt.Groups;
import rubt.world.*;

import static rubt.Vars.*;

public class Logic {

    public static void load() {
        state = State.menu;
    }

    public static void update() {
        Time.update();

        if (!headless) return;

        Groups.units.each(Unit::update);
        Groups.turrets.each(Turret::update);
    }
}
