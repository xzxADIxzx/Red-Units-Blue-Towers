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
import rubt.net.Send;
import rubt.ui.Fonts;

import static arc.Core.*;
import static rubt.Vars.*;

public class ChatFragment extends Table {

    public static final int messagesShown = 12;
    public static final float width = 600f;
    public static final float textWidth = width - 32f;

    public Seq<String> messages = new Seq<>();
    public Seq<String> history = new Seq<>();
    public int scroll;
    public int position;

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

        font = Fonts.aldrich;
        layout = new GlyphLayout();
        sy = 16f + field.getHeight();
    }

    @Override
    public void draw() {
        super.draw();

        alpha = Mathf.lerpDelta(alpha, Mathf.num(shown || state == State.lobby), .005f);
        Draw.alpha(Interp.pow3In.apply(alpha));

        if (messages.isEmpty()) return;

        Textures.alphabg.draw(8f, sy, width, y - sy + 8f);

        y = sy;
        int amount = Math.max(messages.size - scroll - messagesShown, 0);
        for (int i = messages.size - scroll - 1; i >= amount; i--) {

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

    public void flush(String message) {
        if (!message.isBlank()) messages.add(message);
        alpha = 1f; // show messages when someone sends a new one
    }

    public void flush() {
        if (!field.getText().isBlank()) {
            Send.chatMessage(field.getText());

            history.add(field.getText());
            position = history.size;
        }

        field.clearText();
    }

    // region control

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

    public void next() {
        if (position > 0) field.setText(history.get(--position));
    }

    public void prev() {
        if (position < history.size) field.setText(++position == history.size ? "" : history.get(position));
    }

    // endregion
}
