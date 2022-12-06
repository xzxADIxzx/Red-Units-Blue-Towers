package rubt.ui.fragments;

import arc.scene.Group;
import arc.util.Log;
import rubt.logic.State;
import rubt.net.Host;

import static rubt.Vars.*;

public class JoinFragment {

    public void build(Group parent) {
        parent.fill(cont -> {
            cont.name = "Join Fragment";
            cont.visible(() -> state == State.menu);

            // TODO normal join
            cont.button(b -> {}, () -> {
                try {
                    new Host("127.0.0.1", 6567).join();
                } catch (Throwable error) {
                    Log.err("Could not to join server", error);
                }
            }).size(100f, 40f);
        });
    }
}
