package rubt.ui.fragments;

import arc.graphics.Color;
import arc.scene.Group;
import arc.scene.ui.layout.Table;
import rubt.Groups;
import rubt.graphics.Textures;
import rubt.logic.State;
import rubt.logic.Team;

import static rubt.Vars.*;

public class LobbyFragment {

    public Table list;

    public void build(Group parent) {
        parent.fill(cont -> {
            cont.name = "Lobby Fragment";
            cont.visible(() -> state == State.lobby);

            cont.margin(8f);

            cont.pane(list -> { // player list
                list.name = "Player list";
                list.defaults().growX().padBottom(8f);

                this.list = list.top();
            }).width(600f).growY().padRight(8f);

            cont.table(info -> { // TODO match info
            }).growX().top();
        });
    }

    // region build

    public void partition(String name) {
        list.table(gap -> {
            gap.add(name).color(Color.gray).padRight(4f);
            gap.image().color(Color.gray).height(4f).growX();
        }).height(32f).row();
    }

    public void buildPlayers(Team team) {
        Groups.players.each(player -> player.team == team, player -> {
            list.button(button -> {
                button.margin(0f);

                button.image(player.avatar()).size(48f).pad(8f);
                button.add(player.name).growX();
                button.image(Textures.redbg).size(48f).pad(8f).visible(() -> player.admin); // TODO icon
            }, () -> {/* TODO information, change team, kick/ban and etc. */}).row();
        });
    }

    public void rebuildList() {
        list.clear();

        for (Team team : Team.values()) {
            partition(team.name());
            buildPlayers(team);
        }
    }

    // endregion
}
