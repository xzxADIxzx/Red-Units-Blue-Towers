package rubt.ui;

import arc.scene.event.Touchable;
import arc.scene.ui.layout.WidgetGroup;
import rubt.ui.fragments.JoinFragment;

import static arc.Core.*;

public class UI {

    public final WidgetGroup hud = new WidgetGroup();

    public JoinFragment joinfrag = new JoinFragment();

    public void load() {
        input.addProcessor(scene);
        scene.add(hud);

        hud.setFillParent(true);
        hud.touchable = Touchable.childrenOnly;

        joinfrag.build(hud);
    }

    public void resize(int width, int height) {
        scene.resize(width, height);
    }
}
