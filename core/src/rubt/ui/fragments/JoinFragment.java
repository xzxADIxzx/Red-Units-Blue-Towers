package rubt.ui.fragments;

import arc.scene.Group;
import arc.util.Log;
import rubt.graphics.Textures;
import rubt.logic.State;
import rubt.net.Host;

import static rubt.Vars.*;

public class JoinFragment {

    public void build(Group parent) {
        parent.fill(cont -> {
            cont.name = "Join Fragment";
            cont.visible(() -> state == State.menu);

            cont.margin(8f);

            cont.table(list -> { // server list
                list.name = "Server list";
                list.defaults().height(200f).growX().padBottom(8f);

                for (int i = 0; i < 4; i++)
                    list.table(Textures.alphabg).row();
            }).width(600f).padRight(8f);

            cont.table(info -> { // nickname & server info
                info.name = "Nickname & Server info";
                info.defaults().height(64f).growX().padBottom(8f);

                info.table(Textures.alphabg).padBottom(16f).row();

                info.table(Textures.alphabg).row();
                info.table(Textures.alphabg).height(512f).row();

                info.button(b -> {}, () -> { // TODO normal join
                    try {
                        new Host("127.0.0.1", 6567).join();
                    } catch (Throwable error) {
                        Log.err("Could not to join server", error);
                    }
                });
            }).growX().top();
        });
    }
}
