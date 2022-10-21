package rubt.client;

import arc.ApplicationListener;
import rubt.Logic;

public abstract class ClientLauncher implements ApplicationListener {
    
    public void init() {
        Logic.load();
        // TODO load content & etc.
    }
}
