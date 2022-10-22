package rubt.client;

import arc.ApplicationListener;
import arc.graphics.g2d.SortedSpriteBatch;
import arc.graphics.g2d.TextureAtlas;
import rubt.content.*;
import rubt.logic.Logic;
import rubt.ui.Textures;

import static arc.Core.*;
import static rubt.Vars.*;

public abstract class ClientLauncher implements ApplicationListener {

    public void init() {
        UnitTypes.load();
        TurretTypes.load();

        Logic.load();

        if (headless) return;

        batch = new SortedSpriteBatch();
        atlas = TextureAtlas.blankAtlas();

        Textures.load();

        UnitTypes.loadui();
        TurretTypes.loadui();
    }
}
