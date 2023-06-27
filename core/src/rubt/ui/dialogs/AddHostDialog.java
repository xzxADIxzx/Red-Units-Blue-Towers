package rubt.ui.dialogs;

import arc.util.Strings;
import rubt.net.Host;
import rubt.ui.Icons;

import static rubt.Vars.*;

public class AddHostDialog extends BaseDialog {

    public String address = "localhost:4755";
    public boolean valid;

    public AddHostDialog() {
        super("Add host");
        addCloseButton();

        buttons.button("Add", Icons.done, () -> {
            String[] parts = address.split(":");
            ui.joinfrag.addHost(new Host(parts[0], Strings.parseInt(parts[1])));

            hide();
        }).disabled(b -> !valid);

        cont.add("Address:").padRight(4f);
        cont.field(address, text -> address = text).valid(text -> {
            String[] parts = text.split(":");
            this.valid = text.contains(":") && parts.length == 2 && Strings.canParseInt(parts[1]);

            return valid;
        }).maxTextLength(100).width(400f);
    }
}
