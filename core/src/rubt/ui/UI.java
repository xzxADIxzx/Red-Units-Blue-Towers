package rubt.ui;

import arc.files.Fi;
import arc.func.Cons;
import arc.graphics.Color;
import arc.scene.event.Touchable;
import arc.scene.ui.layout.Table;
import arc.scene.ui.layout.WidgetGroup;
import arc.struct.Seq;
import rubt.ui.dialogs.*;
import rubt.ui.fragments.*;

import static arc.Core.*;

public class UI {

    public final WidgetGroup hud = new WidgetGroup();
    public final WidgetGroup menu = new WidgetGroup();

    public JoinFragment joinfrag = new JoinFragment();
    public LobbyFragment lobbyfrag = new LobbyFragment();
    public RedHudFragment redfrag = new RedHudFragment();
    public ChatFragment chatfrag = new ChatFragment();
    public LoadingFragment loadfrag = new LoadingFragment();
    public DebugFragment debugfrag = new DebugFragment();

    public FileChooserDialog fileChooser;
    public AddHostDialog addHost;
    public AvatarDialog avatar;

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
        loadfrag.build(menu);
        debugfrag.build(menu);

        // dialogs are created here because before the load() call, the styles have not yet been created
        fileChooser = new FileChooserDialog();
        addHost = new AddHostDialog();
        avatar = new AvatarDialog();

        app.post(() -> app.post(() -> app.post(() -> app.post(() -> { // TODO load & discover on fragment opened
            joinfrag.loadSavedHosts();
            joinfrag.discoverLocalHosts();
            joinfrag.rebuildList();
        }))));
    }

    public void resize(int width, int height) {
        scene.resize(width, height);
        loadfrag.resized();
    }

    // region build

    public void openFile(String title, Seq<String> extensions, Cons<Fi> cons) {
        fileChooser.show(title, true, extensions, cons);
    }

    public void saveFile(String title, String extension, Cons<Fi> cons) {
        fileChooser.show(title, false, Seq.with(extension), cons);
    }

    // TODO add something like announce(String text)

    public void partition(Table list, String name) {
        list.table(gap -> {
            gap.add(name, Styles.tech).color(Color.gray).padRight(4f);
            gap.image().color(Color.gray).height(4f).growX();
        }).height(32f).row();
    }

    // endregion
}
