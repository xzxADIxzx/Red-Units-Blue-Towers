package rubt.ui.fragments;

import arc.func.Cons;
import arc.math.Mathf;
import arc.scene.Group;
import arc.scene.event.Touchable;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import rubt.graphics.Palette;
import rubt.graphics.Textures;
import rubt.logic.State;
import rubt.net.Net;
import rubt.ui.Icons;

import static arc.Core.*;
import static rubt.Vars.*;

public class MenuFragment {

    public Table cont, pane;
    public boolean shown;

    public void build(Group parent) {
        parent.fill(cont -> {
            cont.name = "Menu Fragment";
            cont.visible(() -> state == State.menu || shown);

            cont.margin(8f).left();

            cont.table(Textures.slashed_alpha, pane -> { // pane
                pane.name = "Pane";
                pane.defaults().width(256f).padBottom(8f);

                this.cont = cont;
                this.pane = pane;
                rebuild();
            }).growY();
        });
    }

    public void toggle() {
        shown = !shown && (state == State.game || state == State.editor);
        rebuild();
    }

    public void rebuild() {
        cont.background(state == State.game || state == State.editor ? Textures.dark : null);
        cont.touchable = Touchable.enabled; // control block while the player is in the menu

        pane.clear();
        Cons<TextButton> anim = b -> b.translation.x = Mathf.lerpDelta(b.translation.x, b.hasMouse() ? 24f : 0f, .1f);

        ui.partition(pane, "[#FF0040]RU[#0040FF]BT", Palette.red, Palette.blue);

        if (state == State.menu)
            pane.button("Play", Icons.play, ui.joinfrag::show).update(anim).row();
        else
            pane.button("Continue", Icons.play, this::toggle).update(anim).row();

        pane.button("Editor", Icons.design, () -> ui.openEditor.show()).update(anim).row();
        pane.button("Settings", Icons.settings, () -> {}).update(anim).row();

        if (state == State.menu)
            pane.button("Exit", Icons.exit, app::exit).update(anim).row();
        else
            pane.button("Quit", Icons.close, this::quit).update(anim).row();

        if (state != State.editor) return;

        ui.partition(pane, "Editor");

        pane.button("Save as", Icons.save, () -> {}).update(anim).row();
        pane.button("Resize", Icons.resize, () -> {}).update(anim).row();
    }

    public void quit() {
        ui.confirm("Quit", "Are you sure want to quit?\n[scarlet]All unsaved data will be lost!", () -> {
            if (Net.connected()) // multiplayer game
                Net.disconnect();
            else { // editor
                state = State.menu;
                toggle();
            }
        });
    }
}
