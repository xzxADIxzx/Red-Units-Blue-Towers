package rubt.ui;

import arc.graphics.Color;
import arc.scene.ui.Button.ButtonStyle;
import arc.scene.ui.CheckBox.CheckBoxStyle;
import arc.scene.ui.Dialog.DialogStyle;
import arc.scene.ui.ImageButton.ImageButtonStyle;
import arc.scene.ui.Label.LabelStyle;
import arc.scene.ui.ScrollPane.ScrollPaneStyle;
import arc.scene.ui.TextButton.TextButtonStyle;
import arc.scene.ui.TextField.TextFieldStyle;
import rubt.graphics.Palette;
import rubt.graphics.Textures;

import static arc.Core.*;

public class Styles {

    public static ButtonStyle button, emptyButton;
    public static TextButtonStyle textButton;
    public static ImageButtonStyle imageButton;

    public static LabelStyle label, tech;
    public static TextFieldStyle field;
    public static CheckBoxStyle check;
    public static ScrollPaneStyle scroll;
    public static DialogStyle dialog, errorDialog;

    public static void load() {
        scene.addStyle(ButtonStyle.class, button = new ButtonStyle() {{
            up = Textures.mono_alpha;
            over = Textures.mono_main;
            down = Textures.mono_accent;
            disabled = Textures.mono_main;
        }});

        emptyButton = new ButtonStyle() {{
            over = Textures.mono_main;
        }};

        scene.addStyle(TextButtonStyle.class, textButton = new TextButtonStyle() {{
            font = Fonts.aldrich;

            up = Textures.mono_alpha;
            over = Textures.mono_main;
            down = Textures.mono_accent;
            disabled = Textures.mono_main;
        }});

        scene.addStyle(ImageButtonStyle.class, imageButton = new ImageButtonStyle() {{
            up = Textures.mono_alpha;
            over = Textures.mono_main;
            down = Textures.mono_accent;
            disabled = Textures.mono_main;
        }});

        scene.addStyle(LabelStyle.class, label = new LabelStyle() {{
            font = Fonts.aldrich;
        }});

        tech = new LabelStyle() {{
            font = Fonts.tech;
        }};

        scene.addStyle(TextFieldStyle.class, field = new TextFieldStyle() {{
            font = Fonts.aldrich;
            fontColor = Color.white;

            background = Textures.mono_main;
            invalidBackground = Textures.mono_red;
            cursor = Textures.white;
            selection = Textures.accent;
        }});

        scene.addStyle(CheckBoxStyle.class, check = new CheckBoxStyle() {{
            font = Fonts.aldrich;

            checkboxOn = Textures.mono_accent;
            checkboxOver = Textures.mono_main;
            checkboxOff = Textures.mono_alpha;
        }});

        scene.addStyle(ScrollPaneStyle.class, scroll = new ScrollPaneStyle() {{
            vScroll = Textures.mono_main;
            vScrollKnob = Textures.mono_accent;
            hScroll = Textures.mono_main;
            hScrollKnob = Textures.mono_accent;
        }});

        scene.addStyle(DialogStyle.class, dialog = new DialogStyle() {{
            titleFont = Fonts.tech;
            titleFontColor = Palette.accent;

            background = Textures.mono_alpha;
            stageBackground = Textures.dark;
        }});

        errorDialog = new DialogStyle() {{
            titleFont = Fonts.tech;
            titleFontColor = Palette.red;

            background = Textures.mono_alpha;
            stageBackground = Textures.dark;
        }};
    }
}
