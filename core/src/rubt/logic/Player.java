package rubt.logic;

import arc.graphics.Pixmap;
import arc.graphics.Texture;
import arc.graphics.g2d.TextureRegion;
import arc.net.Connection;
import rubt.Groups;
import rubt.Groups.GroupObject;

import static arc.Core.*;

public class Player extends GroupObject {

    public final Connection con;

    public Pixmap avatar;
    public String name;

    public Team team = Team.observers;
    public boolean admin;

    public Player(Connection con) {
        super(Groups.players);
        this.con = con;
    }

    public String ip() {
        return con.getRemoteAddressTCP().getAddress().getHostName();
    }

    public TextureRegion avatar() {
        return avatar == null ? atlas.find("avatar") : new TextureRegion(new Texture(avatar));
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
}
