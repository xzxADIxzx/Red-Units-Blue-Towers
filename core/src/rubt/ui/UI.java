package rubt.ui;

import arc.scene.event.Touchable;
import arc.scene.ui.layout.WidgetGroup;
import rubt.ui.dialogs.AddHostDialog;
import rubt.ui.fragments.JoinFragment;

import static arc.Core.*;

public class UI {

    public final WidgetGroup hud = new WidgetGroup();

    public JoinFragment joinfrag = new JoinFragment();

    public AddHostDialog addHost;

    public void load() {
        input.addProcessor(scene);
        scene.add(hud);

        hud.setFillParent(true);
        hud.touchable = Touchable.childrenOnly;

        joinfrag.build(hud);

        // dialogs are created here because before the load() call, the styles have not yet been created
        addHost = new AddHostDialog();

        app.post(() -> app.post(() -> app.post(() -> app.post(() -> { // TODO load & discover on fragment opened
            joinfrag.loadSavedHosts();
            joinfrag.discoverLocalHosts();
            joinfrag.rebuildList();
        }))));
    }

    public void resize(int width, int height) {
        scene.resize(width, height);
    }
}
