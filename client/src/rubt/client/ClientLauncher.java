package rubt.client;

import arc.ApplicationListener;
import arc.graphics.Camera;
import arc.graphics.g2d.SortedSpriteBatch;
import arc.graphics.g2d.TextureAtlas;
import arc.scene.Scene;
import arc.util.*;
import rubt.content.*;
import rubt.graphics.*;
import rubt.input.DesktopInput;
import rubt.logic.Logic;
import rubt.net.Packets;
import rubt.ui.*;
import rubt.world.Entities;

import static arc.Core.*;
import static rubt.Vars.*;

public abstract class ClientLauncher implements ApplicationListener {

    public long lastTime;

    public void init() {
        ContentTypes.load();

        Logic.load();
        Packets.load();
        Entities.load();

        settings.setAppName("rubt");
        settings.load();
        settings.defaults("player-name", "Nooby");

        // save settings once a minute
        settings.setAutosave(false);
        Timer.schedule(() -> {
            if (settings.modified()) settings.forceSave();
        }, 60f, 60f);

        if (headless) return;

        camera = new Camera();
        batch = new SortedSpriteBatch();
        scene = new Scene();
        atlas = TextureAtlas.blankAtlas();

        mobile = app.isMobile();
        renderer = new Renderer();
        ui = new UI();
        handler = mobile ? null : new DesktopInput();

        Shaders.load();
        Fonts.load();
        Textures.load();
        Icons.load();
        Styles.load();

        ui.load();
        ContentTypes.loadui();
    }

    public void dispose() {
        settings.forceSave();
    }

    public void limitFPS(int targetFPS) {
        long target = 1000000000 / targetFPS; // target in nanos
        long elapsed = Time.timeSinceNanos(lastTime);

        if (elapsed < target)
            Threads.sleep((target - elapsed) / 1000000, (int) ((target - elapsed) % 1000000));

        lastTime = Time.nanos();
    }
}
