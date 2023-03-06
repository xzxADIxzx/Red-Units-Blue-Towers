package rubt.ui.fragments;

import arc.input.KeyCode;
import arc.scene.Group;
import arc.scene.ui.Image;
import arc.scene.ui.layout.Table;
import arc.util.Scaling;
import rubt.content.UnitTypes;
import rubt.graphics.Textures;
import rubt.logic.State;
import rubt.ui.Styles;

import static arc.Core.*;
import static rubt.Vars.*;

public class RedHudFragment {

    public Runnable rebuild;

    public void build(Group parent) {
        parent.fill(cont -> {
            cont.name = "Red Hud Fragment";
            cont.visible(() -> state == State.game);

            cont.bottom();

            cont.table(Textures.alphabg, list -> { // uBnits list
                list.name = "Units List";
                list.visible(() -> handler.controlled.any());
                list.defaults().size(64f);

                rebuild = () -> {
                    list.clear();

                    int[] amount = new int[UnitTypes.all.size];
                    handler.controlled.each(unit -> amount[unit.type.id]++);

                    for (int i = 0; i < amount.length; i++) {
                        if (amount[i] == 0) continue;

                        final int id = i; // I love java
                        final String label = String.valueOf(amount[i]);

                        list.button(b -> {
                            b.stack(new Image(UnitTypes.all.get(id).icon).setScaling(Scaling.fit),
                                    new Table(pad -> pad.bottom().right().add(label)));
                        }, Styles.emptyButton, () -> {
                            // select units of this type (LMB)
                            handler.controlled.filter(unit -> unit.type.id == id);
                            app.post(this::rebuild);
                        }).margin(0f).get().clicked(KeyCode.mouseRight, () -> {
                            // unselect units of this type (RMB)
                            handler.controlled.removeAll(unit -> unit.type.id == id);
                            app.post(this::rebuild);
                        });
                    }
                };
            }).pad(0f, 256f, 32f, 256f);
        });
    }

    public void rebuild() {
        rebuild.run();
    }
}
