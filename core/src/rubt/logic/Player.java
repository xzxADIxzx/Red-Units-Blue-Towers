package rubt.logic;

import arc.graphics.g2d.TextureRegion;
import rubt.Groups;
import rubt.Groups.GroupObject;

import static arc.Core.*;

public class Player extends GroupObject {

    public TextureRegion avatar = atlas.find("redbg");
    public String name;

    public Team team = Team.observers;
    public boolean admin;

    public Player(String name) {
        super(Groups.players);
        this.name = name;
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
