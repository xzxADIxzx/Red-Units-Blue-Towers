package rubt.ui.fragments;

import arc.func.Cons;
import arc.scene.Group;
import arc.scene.ui.layout.Table;
import arc.util.Time;
import rubt.Groups;
import rubt.graphics.Textures;

import static arc.Core.*;
import static rubt.Vars.*;

public class DebugFragment {

    public boolean shown;

    public void build(Group parent) {
        parent.fill(Textures.darkbg, cont -> {
            cont.name = "Chat Fragment";
            cont.visible(() -> shown);

            cont.margin(8f).top().left();
            cont.defaults().width(600f).padRight(8f);

            group(cont, "Performance", perf -> {
                perf.label(() -> "FPS: " + graphics.getFramesPerSecond()).row();
                perf.label(() -> "FRAME: " + graphics.getFrameId()).row();
                perf.label(() -> "TIME: " + Time.time).row();
                perf.label(() -> "DELTA: " + graphics.getDeltaTime()).row();
            });

            group(cont, "Groups", grps -> {
                grps.label(() -> "SYNC: " + Groups.sync.size).row();
                grps.label(() -> "TILES: " + Groups.tiles.size).row();
                grps.label(() -> "UNITS: " + Groups.units.size).row();
                grps.label(() -> "TURRETS: " + Groups.turrets.size).row();
                grps.label(() -> "PLAYERS: " + Groups.players.size).row();
            });
        });
    }

    public void group(Table list, String name, Cons<Table> cons) {
        list.table(Textures.alphabg, cont -> {
            cont.top();
            cont.defaults().growX().padBottom(8f);

            ui.partition(cont, name);
            cons.get(cont);
        }).top();
    }

    public void toggle() {
        shown = !shown;
    }
}
