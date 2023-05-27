package rubt.ui;

import arc.graphics.Color;
import arc.scene.event.Touchable;
import arc.scene.ui.layout.Table;
import arc.scene.ui.layout.WidgetGroup;
import rubt.ui.dialogs.AddHostDialog;
import rubt.ui.fragments.*;

import static arc.Core.*;

public class UI {

    public final WidgetGroup hud = new WidgetGroup();
    public final WidgetGroup menu = new WidgetGroup();

    public JoinFragment joinfrag = new JoinFragment();
    public LobbyFragment lobbyfrag = new LobbyFragment();
    public RedHudFragment redfrag = new RedHudFragment();
    public ChatFragment chatfrag = new ChatFragment();

    public AddHostDialog addHost;

    public void load() {
        input.addProcessor(scene);

        scene.add(hud);
        hud.setFillParent(true);
        hud.touchable = Touchable.childrenOnly;

        scene.add(menu);
        menu.setFillParent(true);
        menu.touchable = Touchable.childrenOnly;

        joinfrag.build(menu);
        lobbyfrag.build(menu);
        redfrag.build(hud);
        chatfrag.build(hud);

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

    // region build
    // TODO add something like announce(String text)

    public void partition(Table list, String name) {
        list.table(gap -> {
            gap.add(name, Styles.tech).color(Color.gray).padRight(4f);
            gap.image().color(Color.gray).height(4f).growX();
        }).height(32f).row();
    }

    // endregion
}
