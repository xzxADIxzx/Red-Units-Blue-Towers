package rubt.ui.dialogs;

import rubt.logic.Logic;
import rubt.logic.State;
import rubt.ui.Icons;

import static rubt.Vars.*;

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
                } catch (Exception ex) {
                    ui.error("Couldn't load map", ex);
                    Logic.reset(); // for safety
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
