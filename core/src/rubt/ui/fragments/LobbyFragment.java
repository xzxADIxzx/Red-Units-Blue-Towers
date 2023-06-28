package rubt.ui.fragments;

import arc.scene.Group;
import arc.scene.ui.layout.Table;
import arc.util.Timer;
import rubt.Groups;
import rubt.graphics.Textures;
import rubt.logic.State;
import rubt.logic.Team;
import rubt.net.Net;
import rubt.ui.Icons;

import static rubt.Vars.*;

public class LobbyFragment {

    public Table list;
    public boolean needRebuilding;

    public void build(Group parent) {
        Timer.schedule(() -> { // TODO looks bad
            if (needRebuilding) rebuildList();
        }, 0f, .5f);

        parent.fill(cont -> {
            cont.name = "Lobby Fragment";
            cont.visible(() -> state == State.lobby);

            cont.margin(8f);

            cont.pane(list -> { // player list
                list.name = "Player list";
                list.defaults().growX().padBottom(8f);

                this.list = list.top();
            }).width(600f).growY().padRight(16f);

            cont.table(info -> { // server info
                info.name = "Server info";
                info.defaults().height(64f).growX().padBottom(8f);

                info.table(Textures.mono_alpha).height(512f).colspan(2).row();
                // TODO some info about game like rules & etc.

                info.button("Ready", () -> state = State.game).padRight(8f);
                info.button("Quit", Net::disconnect);
            }).growX().top();
        });
    }

    // region build

    public void buildPlayers(Team team) {
        Groups.players.each(player -> player.team == team, player -> {
            list.button(button -> {
                button.margin(0f);

                button.image(player.avatar()).size(48f).pad(8f);
                button.add(player.name).growX();
                button.image(Icons.done).size(48f).pad(8f).visible(() -> player.admin);
            }, () -> {/* TODO information, change team, kick/ban and etc. */}).row();
        });
    }

    public void rebuildList() {
        list.clear();

        for (Team team : Team.values()) {
            ui.partition(list, team.name());
            buildPlayers(team);
        }

        needRebuilding = false;
    }

    // endregion
}
