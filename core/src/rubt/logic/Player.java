package rubt.logic;

import arc.graphics.g2d.TextureRegion;
import arc.net.Connection;
import rubt.Groups;
import rubt.Groups.Entity;
import rubt.io.*;

import static arc.Core.*;
import static rubt.Vars.*;

public class Player extends Entity {

    public Connection con;

    public byte[] avatar;
    public String name;

    public Team team = Team.observers;
    public boolean admin;

    /** TODO sync cursor position so teammates can see you. */
    public float cursorX, cursorY;

    public Player() {
        super(Groups.players);
    }

    public String ip() {
        return con.getRemoteAddressTCP().getAddress().getHostName();
    }

    public TextureRegion avatar() {
        return avatar == null ? atlas.find("avatar") : Image.wrap(avatar, id);
    }

    public boolean red() {
        return team == Team.red;
    }

    public boolean blue() {
        return team == Team.blue;
    }

    public boolean observer() {
        return team == Team.observers;
    }

    public float getX() {
        return cursorX;
    }

    public float getY() {
        return cursorY;
    }

    // region serialization

    public void write(Writes w) {
        w.b(avatar);
        w.str(name);
        w.b(team.ordinal());
        w.bool(admin);
    }

    public void read(Reads r) {
        avatar = r.b(Image.rgbSize);
        name = r.str();
        team = Team.values()[r.b()];
        admin = r.bool();

        ui.lobbyfrag.rebuildList();
    }

    // endregion
}
