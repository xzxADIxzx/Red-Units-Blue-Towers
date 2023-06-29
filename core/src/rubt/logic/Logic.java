package rubt.logic;

import arc.func.Boolp;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.Timer;
import arc.util.Timer.Task;
import rubt.Groups;
import rubt.world.*;

import static rubt.Vars.*;

public class Logic {

    /** Frequency of calling low-important tasks per second. */
    private static final int litsFrequency = 10;
    /** Low important tasks, such as snapshots. Tasks will be executed one by one, distributing the load. */
    private static final Seq<Runnable> lits = new Seq<>();

    /** Id of the last called low-important task. */
    private static int lastLitId;
    /** Task to perform low-important tasks. */
    private static Task litTask;

    // region lits

    /** Schedules a task to run 10 times per second. Usually used for heavy or low-important tasks. */
    public static void schedule(Runnable task) {
        lits.add(task);

        if (litTask != null) litTask.cancel();
        litTask = Timer.schedule(() -> {
            lits.get(lastLitId++).run();
            if (lastLitId % lits.size == 0) lastLitId = 0;
        }, 0f, 1f / lits.size / litsFrequency);
    }

    /** @see #schedule(Runnable) */
    public static void schedule(Boolp condition, Runnable task) {
        schedule(() -> {
            if (condition.get()) task.run();
        });
    }

    // endregion

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
