package rubt.ui.fragments;

import arc.func.Floatp;
import arc.graphics.Color;
import arc.graphics.g2d.*;
import arc.math.Mathf;
import arc.scene.Group;
import arc.scene.actions.Actions;
import arc.scene.ui.layout.Table;
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

    public Floatp progress;

    public void build(Group parent) {
        parent.addChild(this);
        setFillParent(true);

        name = "Loading Fragment";
        superHide();

        label(() -> (int) (progress.get() * 100) + "%").style(Styles.tech);
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

        Lines.stroke(10f); // TODO replace this square by hexes filling the screen
        Draw.color(Palette.accent, color.a);
        Lines.square(x, y, 210f, 45f);

        Fill.square(x, y, progress * 130f, 45f);
        Fill.rect(x, y, graphics.getWidth(), 100f);

        Draw.color(Color.black);
        Fill.rect(x, y, graphics.getWidth(), 80f);

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
