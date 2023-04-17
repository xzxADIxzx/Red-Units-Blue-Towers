package rubt.logic;

import rubt.Groups;
import rubt.Groups.GroupObject;

public class Player extends GroupObject {

    public String name;
    public Team team = Team.observers;

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
