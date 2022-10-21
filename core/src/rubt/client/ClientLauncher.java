package rubt.client;

import arc.ApplicationListener;
import rubt.Logic;
import rubt.ui.Textures;

import static rubt.Vars.*;

public abstract class ClientLauncher implements ApplicationListener {

    public void init() {
        Logic.load();
        // TODO load content & etc.

        if (headless) return;

        Textures.load();
    }
}
