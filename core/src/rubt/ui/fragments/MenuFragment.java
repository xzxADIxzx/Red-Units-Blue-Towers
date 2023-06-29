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
import rubt.ui.Icons;
import rubt.ui.Styles;

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

                pane.table(title -> {
                    title.image().color(Palette.red).height(4f).growX();
                    title.add("[#FF0040]RU[#0040FF]BT", Styles.tech).pad(4f);
                    title.image().color(Palette.blue).height(4f).growX();
                }).row();

                Cons<TextButton> anim = b -> b.translation.x = Mathf.lerpDelta(b.translation.x, b.hasMouse() ? 24f : 0f, .1f);

                pane.button("Play", Icons.play, ui.joinfrag::show).update(anim).row();
                pane.button("Editor", Icons.design, () -> ui.openEditor.show()).update(anim).row();
                pane.button("Settings", Icons.settings, () -> {}).update(anim).row();
                pane.button("Exit", Icons.exit, app::exit).update(anim);

                this.cont = cont;
                this.pane = pane;
            }).growY();
        });
    }

    public void toggle() {
        shown = !shown && (state == State.game || state == State.editor);
        if (shown) rebuild();
    }

    public void rebuild() {
        cont.background(state == State.game || state == State.editor ? Textures.dark : null);
        cont.touchable = Touchable.enabled; // control block while the player is in the menu
    }
}
