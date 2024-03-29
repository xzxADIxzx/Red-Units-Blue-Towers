package rubt.graphics;

import arc.graphics.Color;
import arc.graphics.g2d.Bloom;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import arc.struct.Seq;
import rubt.Groups;
import rubt.logic.Player;
import rubt.logic.Team;
import rubt.types.ContentType;

import static arc.Core.*;
import static rubt.Vars.*;

public class Renderer {

    public Bloom bloom = new Bloom(true);

    public float minZoom = 1f, maxZoom = 7f;
    public float current = 4f, target = 4f;

    public void zoom(float amount) {
        target *= (amount / 4) + 1;
        target = Mathf.clamp(target, minZoom, maxZoom);
    }

    public <T extends ContentType.Provider<T>> void drawContent(Seq<T> items, Color glow) {
        Draw.color();
        items.each(i -> i.type().draw(i));

        bloom.setBloomIntesity(2.8f);
        bloom.capture();

        Draw.color(glow);
        items.each(i -> i.type().drawGlow(i));

        bloom.render();
    }

    public void draw() {
        // zoom update
        float dest = Mathf.clamp(Mathf.round(target, 0.5f), minZoom, maxZoom);
        current = Mathf.lerpDelta(current, dest, 0.1f);
        if (Mathf.equal(current, dest, 0.001f)) current = dest;

        // width & height
        int w = graphics.getWidth(), h = graphics.getHeight();
        float wc = w / current, hc = h / current;

        // other stuff
        camera.resize(wc, hc);
        bloom.resize(w, h);
        bloom.blurPasses = 6;

        Draw.proj(camera);
        Draw.sort(true);

        Draw.draw(Layers.bg, () -> {
            graphics.clear(Palette.background);
            Fill.light(camera.position.x, camera.position.y, 64, hc * .8f, Palette.lightbg, Palette.background);

            if (player == null) return;

            bloom.setBloomIntesity(2.8f);
            bloom.capture();

            graphics.clear(player.team == Team.red ? Palette.red : Palette.blue);
            Fill.dropShadow(camera.position.x, camera.position.y, wc, hc, 8f, 1f);

            bloom.render();
        });

        Draw.draw(Layers.tiles, () -> drawContent(Groups.tiles, Color.white));
        Draw.draw(Layers.units, () -> drawContent(Groups.units, Palette.red));
        Draw.draw(Layers.turrets, () -> drawContent(Groups.turrets, Palette.blue));
        Draw.draw(Layers.bullets, () -> drawContent(Groups.bullets, Color.white));

        Draw.draw(Layers.overlay, () -> {
            bloom.setBloomIntesity(1.8f);
            bloom.capture();

            handler.draw();

            if (player != null) {
                Draw.color(player.team == Team.red ? Palette.red : Palette.blue);
                Groups.players.each(other -> other.team == player.team && other != player, Player::drawCursor);
            }

            bloom.render();
        });

        Draw.flush();

        scene.act();
        scene.draw();
    }
}
