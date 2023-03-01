package rubt.ui;

import arc.scene.ui.Button.ButtonStyle;
import arc.scene.ui.TextButton.TextButtonStyle;
import rubt.graphics.Textures;

import static arc.Core.*;

public class Styles {

    public static ButtonStyle button;
    public static TextButtonStyle textButton;

    public static void load() {
        scene.addStyle(ButtonStyle.class, button = new ButtonStyle() {{
            up = Textures.alphabg;
            over = Textures.mainbg;
            down = Textures.accentbg;
        }});

        scene.addStyle(TextButtonStyle.class, textButton = new TextButtonStyle() {{
            font = Fonts.tech;

            up = Textures.alphabg;
            over = Textures.mainbg;
            down = Textures.accentbg;
        }});
    }
}
