package rubt.ui.fragments;

import arc.func.Floatp;
import arc.graphics.Color;
import arc.graphics.g2d.*;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.Group;
import arc.scene.actions.Actions;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import rubt.graphics.Palette;
import rubt.graphics.Textures;
import rubt.ui.Styles;

import static arc.Core.*;
import static rubt.Vars.*;

public class LoadingFragment extends Table {

    public static final float size = 60f;
    public static final float space = 80f;
    public static final float skew = size / 2f;
    public static final float width = size / 1.7f;

    public static final Seq<Vec2> hexes = new Seq<>();

    public Floatp progress;

    public void build(Group parent) {
        parent.addChild(this);
        setFillParent(true);

        name = "Loading Fragment";
        superHide();

        label(() -> (int) (progress.get() * 100) + "%").style(Styles.tech);
    }

    public void resize(int width, int height) {
        hexes.clear(); // TODO optimize

        int spacing = 150;
        float h = Mathf.sqrt3 * spacing / 4f;

        for (int x = 0; x < width / spacing; x++) {
            for (int y = 0; y < height / h; y++) {
                int cx = (int) (x * spacing * 1.5f + (y % 2) * spacing * 0.75f) + spacing / 2;
                int cy = (int) (y * h) + spacing / 2;

                hexes.add(new Vec2(cx - skew, cy - size));
            }
        }
    }

    @Override
    public void draw() {
        Draw.color(Color.white, color.a);
        Textures.darkbg.draw(0f, 0f, graphics.getWidth(), graphics.getHeight());

        super.draw();

        float progress = this.progress.get();

        renderer.bloom.setBloomIntesity(1.5f + progress * 1.5f);
        renderer.bloom.capture();

        float w = graphics.getWidth(), h = graphics.getHeight();
        float x = w / 2f, y = h / 2f;

        Draw.color(Palette.accent, color.a);

        for (int i = 0; i < hexes.size; i++) {

            float alpha = Mathf.clamp(progress * hexes.size - i);
            if (alpha == 0f) break; // the rest of the hexes will have the same result

            Draw.color(Palette.accent, alpha * color.a);

            Vec2 hex = hexes.get(i);
            Fill.poly(hex.x, hex.y, 6, size);
        }

        Draw.color(Color.black);
        Fill.rect(x, y, w, 120f);

        Draw.color(Palette.accent, color.a);
        Fill.rect(x, y + 45f, w, 10f);
        Fill.rect(x, y - 45f, w, 10f);

        int bars = (int) (w / size / 2);
        for (int i = 2; i < bars; i++) {

            float fract = 1f - (i - 2f) / (bars - 1f);
            float alpha = progress >= fract ? 1f : Mathf.clamp(1f - (fract - progress) * bars);

            Draw.color(Palette.accent, alpha * color.a);

            for (int side : Mathf.signs) {
                float bx = x + i * space * side - width / 2f;

                Fill.rects(bx, y, width, skew, -skew * side);
                Fill.rects(bx, y, width, -skew, -skew * side);
            }
        }

        renderer.bloom.render();
    }

    public void show(Floatp progress) {
        this.progress = progress;
        this.visible = true;

        hexes.shuffle();
        actions(Actions.alpha(0f), Actions.alpha(1f, 1f));
    }

    public void superHide() {
        this.progress = () -> 1f;
        this.visible = false;
    }

    public void hide() {
        actions(Actions.alpha(0f, 1f), Actions.run(this::superHide));
    }
}
