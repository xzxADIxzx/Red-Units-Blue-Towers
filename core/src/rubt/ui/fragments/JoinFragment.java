package rubt.ui.fragments;

import arc.scene.Group;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Log;
import rubt.graphics.Textures;
import rubt.logic.State;
import rubt.net.Host;

import static rubt.Vars.*;

public class JoinFragment {

    public Seq<Host> hosts;
    public Host selected;

    public Table list;

    public void build(Group parent) {
        parent.fill(cont -> {
            cont.name = "Join Fragment";
            cont.visible(() -> state == State.menu);

            cont.margin(8f);

            cont.pane(list -> { // server list
                list.name = "Server list";
                list.defaults().height(200f).growX().padBottom(8f);

                this.list = list.top();
            }).width(600f).growY().padRight(8f);

            loadHosts();
            rebuildList();

            cont.table(info -> { // nickname & server info
                info.name = "Nickname & Server info";
                info.defaults().height(64f).growX().padBottom(8f);

                info.field("[#0096FF]xzxADIxzx", nickname -> {}).padBottom(16f).row();

                info.table(Textures.alphabg, name -> {
                    name.label(() -> selected == null ? "Server name" : selected.name()).growX();
                    name.label(() -> selected == null ? "" : selected.address());
                }).row();
                info.table(Textures.alphabg).height(512f).row();

                info.button("Join", () -> {
                    try {
                        selected.join();
                    } catch (Throwable error) {
                        Log.err("Could not to join server", error);
                    }
                }).disabled(b -> selected == null);
            }).growX().top();
        });
    }

    // region hosts

    public void loadHosts() { // TODO load from settings
        hosts = Seq.with(new Host("localhost", 6567));
    }

    public void saveHosts() {}

    public void addHost(Host host) {
        hosts.add(host);
        saveHosts();
        rebuildList();
    }

    public void removeHost(Host host) {
        hosts.remove(host);
        saveHosts();
        rebuildList();
    }

    public void rebuildList() {
        list.clear();

        hosts.each(host -> {
            list.button(b -> {
                b.top();
                b.defaults().growX();

                b.labelWrap(host::name).padBottom(16f).row();
                b.labelWrap(host::enemy).padBottom(8f).fontScale(.8f).row();
                b.labelWrap(host::desc);
            }, () -> this.selected = host).row();
        });

        list.button("Add", () -> addHost(new Host("localhost", 6567))).height(64f); // TODO dialog with input field
    }

    // endregion
}
