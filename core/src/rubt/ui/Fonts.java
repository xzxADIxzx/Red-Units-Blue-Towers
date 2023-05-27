package rubt.ui;

import arc.freetype.FreeTypeFontGenerator;
import arc.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import arc.graphics.g2d.Font;

import static arc.Core.*;

public class Fonts {

    public static Font tech, aldrich;

    public static void load() {
        tech = load("tech", new FreeTypeFontParameter() {{ size = 18; }});
        tech.getData().markupEnabled = true;
        tech.getData().down *= 1.5f;

        // this font is licensed under the SIL Open Font License
        // designed by MADType, russian chars by Daymarius
        aldrich = load("aldrich", new FreeTypeFontParameter() {{
            size = 20;
            characters += "АаБбВвГгДдЕеЁёЖжЗзИиЙйКкЛлМмНнОоПпРрСсТтУуФфХхЦцЧчШшЩщЪъЫыЬьЭэЮюЯя";
        }});
        aldrich.getData().markupEnabled = true;
    }

    public static Font load(String name, FreeTypeFontParameter parameter) {
        var generator = new FreeTypeFontGenerator(files.internal("fonts/" + name + ".ttf"));
        return generator.generateFont(parameter);
    }
}
