package rubt.ui;

import arc.graphics.Color;
import arc.scene.ui.Button.ButtonStyle;
import arc.scene.ui.Dialog.DialogStyle;
import arc.scene.ui.Label.LabelStyle;
import arc.scene.ui.ScrollPane.ScrollPaneStyle;
import arc.scene.ui.TextButton.TextButtonStyle;
import arc.scene.ui.TextField.TextFieldStyle;
import rubt.graphics.Palette;
import rubt.graphics.Textures;

import static arc.Core.*;

public class Styles {

    public static ButtonStyle button;
    public static TextButtonStyle textButton;

    public static LabelStyle label;
    public static TextFieldStyle field;
    public static ScrollPaneStyle scroll;
    public static DialogStyle dialog;

    public static void load() {
        scene.addStyle(ButtonStyle.class, button = new ButtonStyle() {{
            up = Textures.alphabg;
            over = Textures.mainbg;
            down = Textures.accentbg;
            disabled = Textures.mainbg;
        }});

        scene.addStyle(TextButtonStyle.class, textButton = new TextButtonStyle() {{
            font = Fonts.tech;

            up = Textures.alphabg;
            over = Textures.mainbg;
            down = Textures.accentbg;
            disabled = Textures.mainbg;
        }});

        scene.addStyle(LabelStyle.class, label = new LabelStyle() {{
            font = Fonts.tech;
        }});

        scene.addStyle(TextFieldStyle.class, field = new TextFieldStyle() {{
            font = Fonts.tech;

            fontColor = Color.white;
            background = Textures.mainbg;
            invalidBackground = Textures.redbg;
            cursor = Textures.whiteui;
            selection = Textures.accentbg;
        }});

        scene.addStyle(ScrollPaneStyle.class, scroll = new ScrollPaneStyle() {{
            vScroll = Textures.mainbg;
            vScrollKnob = Textures.accentbg;
            hScroll = Textures.mainbg;
            hScrollKnob = Textures.accentbg;
        }});

        scene.addStyle(DialogStyle.class, dialog = new DialogStyle() {{
            titleFont = Fonts.tech;

            titleFontColor = Palette.accent;
            background = Textures.alphabg;
            stageBackground = Textures.darkbg;
        }});
    }
}
