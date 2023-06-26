package rubt.ui.dialogs;

import arc.util.Log;
import rubt.logic.State;
import rubt.ui.Icons;

import static rubt.Vars.*;

import java.io.IOException;

public class OpenEditorDialog extends BaseDialog {

    public OpenEditorDialog() {
        super("Open editor");
        addCloseButton();

        buttons.button("Open map", Icons.file, () -> {
            ui.openFile("Open map", "rbwld", fi -> {
                try {
                    world.load(fi.read());
                    state = State.editor;
                    hide();
                } catch (IOException ex) {
                    Log.err(ex);
                    // TODO ui.err
                }
            });
        });

        buttons.button("New map", Icons.plus, () -> {
            world.resize(10, 10);
            state = State.editor;
            hide();
        });
    }
}
