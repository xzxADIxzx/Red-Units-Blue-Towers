package rubt.client;

import arc.ApplicationListener;
import arc.graphics.Camera;
import arc.graphics.g2d.SortedSpriteBatch;
import arc.graphics.g2d.TextureAtlas;
import rubt.content.*;
import rubt.graphics.Renderer;
import rubt.graphics.Textures;
import rubt.input.DesktopInput;
import rubt.logic.Logic;

import static arc.Core.*;
import static rubt.Vars.*;

public abstract class ClientLauncher implements ApplicationListener {

    public void init() {
        UnitTypes.load();
        TurretTypes.load();

        Logic.load();

        if (headless) return;

        camera = new Camera();
        batch = new SortedSpriteBatch();
        atlas = TextureAtlas.blankAtlas();

        mobile = app.isMobile();
        renderer = new Renderer();
        handler = mobile ? null : new DesktopInput();

        Textures.load();

        UnitTypes.loadui();
        TurretTypes.loadui();
    }
}
