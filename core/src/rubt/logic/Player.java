package rubt.logic;

import arc.graphics.g2d.TextureRegion;
import arc.net.Connection;
import arc.util.Align;
import rubt.Groups;
import rubt.Groups.NetObject;
import rubt.io.*;
import rubt.ui.Fonts;
import rubt.ui.Icons;

import static arc.Core.*;
import static rubt.Vars.*;

public class Player extends NetObject {

    public Connection con;

    public byte[] avatar;
    public String name;

    public Team team = Team.observers;
    public boolean admin;

    /** Cursor position that will be visible to teammates. */
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

    // region cursor

    public float getX() {
        return cursorX;
    }

    public float getY() {
        return cursorY;
    }

    public void drawCursor() {
        Icons.cursor.draw(cursorX - 2f, cursorY - 7f, 8f, 8f);
    }

    // endregion
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

    public void writeSnapshot(Writes w) {
        w.p(this);
    }

    public void readSnapshot(Reads r) {
        cursorX = r.f();
        cursorY = r.f();
    }

    // endregion
}
