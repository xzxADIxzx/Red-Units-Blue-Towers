package rubt.graphics;

import arc.graphics.Texture;
import arc.graphics.Texture.TextureFilter;
import arc.graphics.g2d.TextureRegion;
import arc.scene.style.Drawable;
import arc.util.serialization.JsonReader;
import arc.util.serialization.JsonValue;
import rubt.types.textures.NormalTexture;

import static arc.Core.*;

public class Textures {

    public static JsonValue splits;
    public static Drawable darkbg, mainbg, alphabg, accentbg, redbg, whiteui, circle;

    public static void load() {
        splits = new JsonReader().parse(files.internal("sprites/ui/splits.json"));

        darkbg = loadUI("darkbg");
        mainbg = loadUI("mainbg");
        alphabg = loadUI("alphabg");
        accentbg = loadUI("accentbg");
        redbg = loadUI("redbg");

        whiteui = loadUI("whiteui");
        circle = loadUI("circle");
    }

    public static TextureRegion load(String path, String name) {
        Texture texture = new Texture(path + name + ".png");
        texture.setFilter(TextureFilter.linear); // for better experience

        var region = atlas.addRegion(name, texture, 0, 0, texture.width, texture.height);
        if (splits.has(name)) region.splits = splits.get(name).asIntArray();

        return region;
    }

    public static Drawable loadUI(String name) {
        load("sprites/ui/", name);
        return atlas.drawable(name);
    }

    public static Drawable loadIcon(String name) {
        load("sprites/icons/", name);
        return atlas.drawable(name);
    }

    public static NormalTexture loadNormal(String file) {
        return new NormalTexture(file + ".png");
    }
}
