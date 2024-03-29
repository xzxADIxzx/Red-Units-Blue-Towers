package rubt.ui.dialogs;

import arc.scene.ui.Dialog;
import rubt.ui.Icons;

import static arc.Core.*;

public class BaseDialog extends Dialog {

    public BaseDialog(String title, DialogStyle style) {
        super(title, style);
        this.title.setEllipsis(false);

        titleTable.clear();
        titleTable.image().color(style.titleFontColor).height(4f).growX();
        titleTable.add(this.title).pad(4f);
        titleTable.image().color(style.titleFontColor).height(4f).growX();
    }

    public BaseDialog(String title) {
        this(title, scene.getStyle(DialogStyle.class));
    }

    @Override
    public void addCloseButton() {
        buttons.defaults().size(200f, 64f);
        buttons.button("Back", Icons.close, this::hide);

        closeOnBack();
    }
}
