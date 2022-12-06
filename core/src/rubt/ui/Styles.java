package rubt.ui;

import arc.scene.ui.Button.ButtonStyle;
import rubt.graphics.Textures;

import static arc.Core.*;

public class Styles {

    public static ButtonStyle button;

    public static void load() {
        scene.addStyle(ButtonStyle.class, button = new ButtonStyle() {{
            up = Textures.alphabg;
            over = Textures.mainbg;
            down = Textures.accentbg;
        }});
    }
}
