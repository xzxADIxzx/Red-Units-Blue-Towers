package rubt.ui.dialogs;

import arc.files.Fi;
import arc.func.Cons;
import arc.scene.ui.*;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Align;
import arc.util.Structs;
import rubt.ui.Icons;
import rubt.ui.Styles;

import static arc.Core.*;

public class FileChooserDialog extends BaseDialog {

    public static final String externalStorage = files.getExternalStoragePath();
    public static final Fi homeDirectory = files.absolute(externalStorage);

    public Fi dir = files.absolute(settings.getString("last-dir", externalStorage));
    public FileHistory history = new FileHistory();

    public boolean open;
    public Seq<String> extensions;
    public Cons<Fi> cons;

    public Table list;
    public ScrollPane pane;
    public TextField field;
    public TextButton button;

    public FileChooserDialog() {
        super("");
        addCloseButton();

        buttons.button("", () -> {
            cons.get(dir.child(field.getText()));
            hide();
        }).disabled(b -> {
            String name = field.getText();
            return open ? !dir.child(name).exists() || dir.child(name).isDirectory() : name.isBlank();
        }).with(b -> this.button = b);

        cont.table(nav -> {
            nav.left();
            nav.defaults().size(48f).padRight(8f);

            nav.button(Icons.home, this::openHomeDirectory);
            nav.button(Icons.arrow_left, history::back).disabled(b -> !history.hasBack());
            nav.button(Icons.arrow_right, history::forward).disabled(b -> !history.hasForward());
            nav.button(Icons.arrow_up, this::openParentDirectory);
        }).growX().row();

        cont.pane(l -> this.list = l.top()).size(550f, 700f).with(p -> this.pane = p).row();
        cont.table(name -> {
            field = new TextField();
            field.setMaxLength(128);
            field.setMessageText("Empty");

            name.add("File").pad(0f, 8f, 0f, 8f);
            name.add(field).grow();
        }).growX();
    }

    public void show(String title, boolean open, Seq<String> extensions, Cons<Fi> cons) {
        this.title.setText(title);
        this.field.setDisabled(open);
        this.button.setText(open ? "Open" : "Save");

        this.open = open;
        this.extensions = extensions;
        this.cons = cons;

        if (!dir.exists()) dir = homeDirectory;
        updateFiles(false);

        show();
    }

    // region files

    public Seq<Fi> availableFiles() {
        return dir.seq()
                .filter(file -> !file.file().isHidden())
                .filter(file -> file.isDirectory() || extensions.contains(file::extEquals))
                .sort(Structs.comps(Structs.comparingBool(file -> !file.isDirectory()), Structs.comparing(Fi::name)));
    }

    public void updateFiles(boolean push) {
        if (push) history.push();
        settings.put("last-dir", dir.absolutePath());

        list.clear();
        list.defaults().height(48f).growX();

        Cons<TextButton> labelAlign = b -> b.getLabelCell().padLeft(8f).labelAlign(Align.left);
        list.button(dir.toString(), Icons.arrow_up, this::openParentDirectory).with(labelAlign).row();

        availableFiles().each(file -> list.button(file.name(), file.isDirectory() ? Icons.folder : Icons.file, Styles.textButton/* Check */, () -> {
            if (file.isDirectory())
                openChildDirectory(file.name());
            else
                field.setText(file.name());
        }).checked(b -> field.getText().equals(file.name())).with(labelAlign).row());

        pane.setScrollY(0f);
        if (open) field.clearText();
    }

    // endregion
    // region directories

    public void openHomeDirectory() {
        dir = homeDirectory;
        updateFiles(true);
    }

    public void openParentDirectory() {
        dir = dir.parent();
        updateFiles(true);
    }

    public void openChildDirectory(String name) {
        dir = dir.child(name);
        updateFiles(true);
    }

    // endregion

    public class FileHistory extends Seq<Fi> {

        public static final int max = 64;
        public int index;

        public void push() {
            if (index != size) truncate(index); // new element is added not to the end

            add(dir);

            if (size > max) remove(0);
            else index++;
        }

        public void back() {
            dir = get(--index - 1);
            updateFiles(false);
        }

        public void forward() {
            dir = get(index++);
            updateFiles(false);
        }

        public boolean hasBack() {
            return index > 1;
        }

        public boolean hasForward() {
            return index < size;
        }
    }
}
