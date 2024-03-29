package rubt.logic;

import arc.graphics.g2d.TextureRegion;
import arc.net.Connection;
import arc.util.Align;
import arc.util.Time;
import rubt.Groups;
import rubt.Groups.NetObject;
import rubt.io.*;
import rubt.net.FloatLerp;
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

    /** Last update time via snapshots. */
    public long lastUpdate;
    public FloatLerp xLerp = new FloatLerp(), yLerp = new FloatLerp();

    public Player() {
        super(Groups.players);
    }

    public String ip() {
        return con.getRemoteAddressTCP().getAddress().getHostName();
    }

    public TextureRegion avatar() {
        return avatar == null ? atlas.find("avatar") : Image.read(avatar);
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

        // draw nickname
        Fonts.aldrich.setUseIntegerPositions(false);
        Fonts.aldrich.getData().setScale(.15f);

        var cache = Fonts.aldrich.getCache();
        cache.clear();
        cache.addText(name, cursorX + 4f, cursorY - 8f, 0f, Align.center, false);
        cache.draw();

        Fonts.aldrich.setUseIntegerPositions(true);
        Fonts.aldrich.getData().setScale(1f);
    }

    // endregion
    // region serialization

    public void write(Writes w) {
        w.nb(avatar);
        w.str(name);
        w.b(team.ordinal());
        w.bool(admin);
    }

    public void read(Reads r) {
        avatar = r.nb(Image.rgbSize);
        name = r.str();
        team = Team.values()[r.b()];
        admin = r.bool();

        ui.lobbyfrag.needRebuilding = true;
    }

    public void writeSnapshot(Writes w) {
        w.p(this);
    }

    public void readSnapshot(Reads r) {
        lastUpdate = Time.millis();
        xLerp.read(r);
        yLerp.read(r);
    }

    public void interpolate() {
        cursorX = xLerp.get(lastUpdate);
        cursorY = yLerp.get(lastUpdate);
    }

    // endregion
}
