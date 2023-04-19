package rubt.ui.fragments;

import arc.graphics.Color;
import arc.scene.Group;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Strings;
import arc.util.Time;
import rubt.graphics.Textures;
import rubt.logic.State;
import rubt.logic.Team;
import rubt.net.Host;
import rubt.net.Net;
import rubt.net.Packets.PlayerCreate;

import static rubt.Vars.*;

public class JoinFragment {

    public static PlayerCreate data = new PlayerCreate() {{ team = Team.observers; }};

    public Seq<Host> saved, local = new Seq<>();
    public Host selected;

    public Table list;
    public boolean discovering;

    public void build(Group parent) {
        parent.fill(cont -> {
            cont.name = "Join Fragment";
            cont.visible(() -> state == State.menu);

            cont.margin(8f);

            cont.pane(list -> { // server list
                list.name = "Server list";
                list.defaults().growX().padBottom(8f);

                this.list = list.top();
            }).width(600f).growY().padRight(8f);

            cont.table(info -> { // nickname & server info
                info.name = "Nickname & Server Info";
                info.defaults().height(64f).growX().padBottom(8f);

                info.field("[#0096FF]xzxADIxzx", nickname -> data.name = nickname).padBottom(16f).row();

                info.table(Textures.alphabg, name -> {
                    name.label(() -> selected == null ? "Server name" : selected.name()).growX();
                    name.label(() -> selected == null ? "" : selected.address());
                }).row();
                info.table(Textures.alphabg).height(512f).row();

                info.button("Join", () -> {
                    try {
                        Net.connect(selected);
                    } catch (Throwable error) {
                        Log.err("Could not to join server", error);
                    }
                }).disabled(b -> selected == null);
            }).growX().top();
        });
    }

    // region hosts

    public void loadSavedHosts() { // TODO load from settings
        saved = Seq.with(new Host("localhost", 6567));
        saved.each(Host::fetchServerInfo); // refresh servers info
    }

    public void discoverLocalHosts() {
        local.clear();
        discovering = true;

        Net.discover(host -> {
            local.add(host);
            rebuildList();
        }, () -> discovering = false);
    }

    public void saveHosts() {}

    public void addHost(Host host) {
        saved.add(host);
        saveHosts();
        rebuildList();
    }

    public void removeHost(Host host) {
        saved.remove(host);
        saveHosts();
        rebuildList();
    }

    // endregion
    // region build

    public void partition(String name) {
        list.table(gap -> {
            gap.add(name).color(Color.gray).padRight(4f);
            gap.image().color(Color.gray).height(4f).growX();
        }).height(32f).row();
    }

    public void buildHosts(Seq<Host> hosts) {
        hosts.each(host -> {
            list.button(b -> {
                b.top();
                b.defaults().growX();

                b.labelWrap(host::name).padBottom(16f).row();
                b.labelWrap(host::enemy).padBottom(8f).fontScale(.8f).row();
                b.labelWrap(host::desc);
            }, () -> this.selected = host).height(180f).row();
        });
    }

    public void rebuildList() {
        list.clear();

        partition("Saved");
        buildHosts(saved);
        list.button("Add", ui.addHost::show).height(64f).row();

        partition("Local");
        buildHosts(local);
        list.button(b -> {
            b.label(() -> discovering ? "Discovering" + Strings.animated(Time.time, 4, 20, ".") : "Refresh");
        }, this::discoverLocalHosts).disabled(b -> discovering).height(64f).row();
    }

    // endregion
}
