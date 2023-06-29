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

    public static void reset() {
        Groups.clear();
        ui.menufrag.toggle(); // update menu fragment
    }

    public static Team nextTeam() {
        if (rules.duel)
            return Groups.players.count(Player::red) == 0 ? Team.red : Groups.players.count(Player::blue) == 0 ? Team.blue : Team.observers;
        else
            return Groups.players.count(Player::red) > Groups.players.count(Player::blue) ? Team.blue : Team.red;
    }
}
