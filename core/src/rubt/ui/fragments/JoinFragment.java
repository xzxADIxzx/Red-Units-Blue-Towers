package rubt.ui.fragments;

import arc.scene.Group;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Strings;
import arc.util.Time;
import rubt.graphics.Textures;
import rubt.logic.State;
import rubt.net.Host;
import rubt.net.Net;
import rubt.ui.Icons;

import static arc.Core.*;
import static rubt.Vars.*;

@SuppressWarnings("unchecked")
public class JoinFragment {

    public Seq<Host> saved, local = new Seq<>();
    public Host selected;

    public Table list;
    public boolean discovering;

    public void build(Group parent) {
        parent.fill(cont -> {
            cont.name = "Join Fragment";
            cont.visible(() -> state == State.join);

            cont.margin(8f);

            cont.pane(list -> { // server list
                list.name = "Server list";
                list.defaults().growX().padBottom(8f);

                this.list = list.top();
            }).width(600f).growY().padRight(16f);

            cont.table(info -> { // nickname & server info
                info.name = "Nickname & Server Info";
                info.defaults().height(64f).growX().padBottom(8f);

                info.table(nick -> {
                    nick.field(settings.getString("player-name"), name -> settings.put("player-name", name))
                            .grow().padRight(8f).maxTextLength(maxNameLength);

                    nick.button(Icons.avatar, () -> ui.avatar.show()).size(64f);
                }).padBottom(16f).row();

                info.table(Textures.mono_alpha, name -> {
                    name.label(() -> selected == null ? "Server name" : selected.name()).growX();
                    name.label(() -> selected == null ? "" : selected.address());
                }).row();
                info.table(Textures.slashed_alpha).height(512f).row();

                info.table(btns -> {
                    btns.defaults().grow();

                    btns.button("Join", () -> {
                        try {
                            Net.connect(selected);
                        } catch (Exception ex) {
                            ui.error("Couldn't connect to the server", ex);
                        }
                    }).disabled(b -> selected == null).padRight(8f);

                    btns.button("Back", () -> state = State.menu);
                });
            }).growX().top();
        });
    }

    public void show() {
        loadSavedHosts();
        rebuildList();

        state = State.join;
    }

    // region hosts

    public void loadSavedHosts() {
        saved = settings.getJson("saved-servers", Seq.class, Seq::new);
        saved.each(Host::fetchServerInfo); // refresh servers info
    }

    public void saveHosts() {
        settings.putJson("saved-servers", saved);
    }

    public void discoverLocalHosts() {
        local.clear();
        discovering = true;

        Net.discover(local::add, () -> {
            rebuildList();
            discovering = false;
        });
    }

    public void addHost(Host host) {
        saved.add(host);
        saveHosts();
        rebuildList();

        host.fetchServerInfo();
    }

    public void removeHost(Host host) {
        saved.remove(host);
        saveHosts();
        rebuildList();
    }

    // endregion
    // region build

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

        ui.partition(list, "Saved");
        buildHosts(saved);
        list.button("Add", ui.addHost::show).height(64f).row();

        ui.partition(list, "Local");
        buildHosts(local);
        list.button(b -> {
            b.label(() -> discovering ? "Discovering" + Strings.animated(Time.time, 4, 20, ".") : "Refresh");
        }, this::discoverLocalHosts).disabled(b -> discovering).height(64f).row();
    }

    // endregion
}
