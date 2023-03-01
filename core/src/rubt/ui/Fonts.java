package rubt.ui;

import arc.freetype.FreeTypeFontGenerator;
import arc.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import arc.graphics.g2d.Font;

import static arc.Core.*;

public class Fonts {

    public static Font tech;

    public static void load() {
        tech = load("tech", new FreeTypeFontParameter() {{ size = 18; }});
    }

    public static Font load(String name, FreeTypeFontParameter parameter) {
        var generator = new FreeTypeFontGenerator(files.internal("fonts/" + name + ".ttf"));
        return generator.generateFont(parameter);
    }
}
