package rubt.ui.dialogs;

import arc.graphics.Pixmap;
import arc.graphics.Pixmaps;
import arc.scene.ui.Dialog;
import arc.scene.ui.layout.Table;
import arc.util.Log;
import rubt.io.Image;
import rubt.ui.Icons;

import static arc.Core.*;
import static rubt.Vars.*;

public class AvatarDialog extends BaseDialog {

    public Pixmap original, image;

    public Table preview;
    public boolean filter, antialias;

    public AvatarDialog() {
        super("Avatar");
        addCloseButton();

        buttons.button("Select file", Icons.file, () -> ui.openFile("Select avatar", Image.extensions, file -> {
            try {
                original = new Pixmap(file);
                rebuild();
            } catch (Exception ex) {
                Log.err("Could not to load image", ex);
                // TODO ui.announce("corrupt img")
            }
        }));
        buttons.button("Apply", Icons.done, () -> {
            settings.put("player-avatar", Image.write(image));
            hide();
        }).disabled(t -> image == null);

        cont.table(t -> preview = t);
        cont.table(sets -> {

            sets.check("Filter", value -> {
                filter = value;
                app.post(this::rebuild);
            }).left().row();

            sets.check("Antialias", value -> {
                antialias = value;
                app.post(this::rebuild);
            }).left().row();
        });
    }

    public void rebuild() {
        preview.clear();
        if (image != null) image.dispose();

        if (original == null)
            preview.image(Icons.avatar).size(48f);
        else {
            image = Pixmaps.scale(original, 48, 48, filter);
            if (antialias) Pixmaps.antialias(image);

            preview.image(Image.wrap(image));
        }
    }

    public Dialog show() {
        if (original == null && settings.has("player-avatar"))
            original = Image.rgb2rgba(settings.getBytes("player-avatar"));

        rebuild();
        return super.show();
    }
}
