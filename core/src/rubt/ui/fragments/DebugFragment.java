package rubt.ui.fragments;

import arc.func.Cons;
import arc.scene.Group;
import arc.scene.ui.layout.Table;
import rubt.Groups;
import rubt.graphics.Textures;
import rubt.net.Net;

import static arc.Core.*;
import static rubt.Vars.*;

public class DebugFragment {

    public boolean shown;

    public void build(Group parent) {
        parent.fill(Textures.dark, cont -> {
            cont.name = "Chat Fragment";
            cont.visible(() -> shown);

            cont.margin(8f).top().left();
            cont.defaults().width(600f).padRight(8f);

            group(cont, "Performance", perf -> {
                perf.label(() -> "FPS: " + graphics.getFramesPerSecond()).row();
                perf.label(() -> "FRAME: " + graphics.getFrameId()).row();
                perf.label(() -> "DELTA: " + graphics.getDeltaTime()).row();
            });

            group(cont, "Groups", grps -> {
                grps.label(() -> "SYNC: " + Groups.sync.size).row();
                grps.label(() -> "TILES: " + Groups.tiles.size).row();
                grps.label(() -> "UNITS: " + Groups.units.size).row();
                grps.label(() -> "TURRETS: " + Groups.turrets.size).row();
                grps.label(() -> "PLAYERS: " + Groups.players.size).row();
            });
            
            group(cont, "Net", netp -> {
                netp.label(() -> "PACKETS READ: " + Net.provider.packetsRead()).row();
                netp.label(() -> "PACKETS WRITTEN: " + Net.provider.packetsWritten()).row();
                netp.label(() -> "BYTES READ: " + Net.provider.bytesRead()).row();
                netp.label(() -> "BYTES WRITTEN: " + Net.provider.bytesWritten()).row();
                netp.label(() -> "PLAYER: " + player).row();
            });
        });
    }

    public void group(Table list, String name, Cons<Table> cons) {
        list.table(Textures.mono_alpha, cont -> {
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
