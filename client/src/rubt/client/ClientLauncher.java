package rubt.client;

import arc.ApplicationListener;
import arc.graphics.Camera;
import arc.graphics.g2d.SortedSpriteBatch;
import arc.graphics.g2d.TextureAtlas;
import arc.util.Threads;
import arc.util.Time;
import rubt.content.*;
import rubt.graphics.Renderer;
import rubt.graphics.Textures;
import rubt.input.DesktopInput;
import rubt.logic.Logic;

import static arc.Core.*;
import static rubt.Vars.*;

public abstract class ClientLauncher implements ApplicationListener {

    public long lastTime;

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

    public void limitFPS(int targetFPS) {
        long target = 1000000000 / targetFPS; // target in nanos
        long elapsed = Time.timeSinceNanos(lastTime);

        if (elapsed < target)
            Threads.sleep((target - elapsed) / 1000000, (int) ((target - elapsed) % 1000000));

        lastTime = Time.nanos();
    }
}
