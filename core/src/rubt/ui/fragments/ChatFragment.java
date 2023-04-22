package rubt.ui.fragments;

import arc.graphics.Color;
import arc.graphics.g2d.*;
import arc.math.Interp;
import arc.math.Mathf;
import arc.scene.Group;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Align;
import rubt.graphics.Textures;
import rubt.logic.State;
import rubt.ui.Fonts;

import static arc.Core.*;
import static rubt.Vars.*;

public class ChatFragment extends Table {

    public static final float width = 600f;
    public static final float textWidth = width - 32f;

    public Seq<String> messages = new Seq<>();
    public Seq<String> history = new Seq<>();

    public TextField field;

    public Font font;
    public GlyphLayout layout;
    public float sy, y;

    public boolean shown;
    public float alpha;

    public void build(Group parent) {
        scene.add(this);
        setFillParent(true);

        name = "Chat Fragment";
        visible(() -> state == State.lobby || state == State.game);

        margin(8f).bottom();

        field = new TextField();
        field.setMaxLength(maxMessageLength);

        add(field).growX().visible(() -> shown);

        font = Fonts.tech;
        layout = new GlyphLayout();
        sy = 16f + field.getHeight();
    }

    @Override
    public void draw() {
        super.draw();

        alpha = Mathf.lerpDelta(alpha, Mathf.num(shown), .005f);
        Draw.alpha(Interp.pow3In.apply(alpha));

        Textures.alphabg.draw(8f, sy, width, y - sy + 8f);

        y = sy;
        for (int i = Math.min(messages.size - 1, 10); i >= 0; i--) {
            var msg = messages.get(i);

            layout.setText(font, msg, Color.white, textWidth, Align.bottomLeft, true);
            y += layout.height + 8f;

            var cache = font.getCache();
            cache.clear();
            cache.setColor(Color.white);

            cache.addText(msg, 16f, y, textWidth, Align.bottomLeft, true);
            cache.setAlphas(Interp.pow5In.apply(alpha));
            cache.draw();
        }
    }

    public void toggle() {
        if (shown && scene.getKeyboardFocus() != field) return;

        shown = !shown;
        alpha = 1f;

        app.post(() -> {
            if (shown) {
                field.requestKeyboard();
                field.fireClick();
            } else
                flush();
        });
    }

    public void flush() {
        if (field.getText().isBlank()) return;

        messages.add(field.getText()); // TODO replace with Send.chatMessage
        field.clearText();
    }
}
