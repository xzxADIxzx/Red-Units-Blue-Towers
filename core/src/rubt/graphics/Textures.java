package rubt.graphics;

import arc.graphics.Color;
import arc.graphics.Texture;
import arc.graphics.Texture.TextureFilter;
import arc.graphics.g2d.TextureRegion;
import arc.scene.style.*;
import arc.util.serialization.JsonReader;
import arc.util.serialization.JsonValue;
import rubt.annotations.Annotations.IconLoader;
import rubt.types.drawers.TileDrawer;

import static arc.Core.*;

public class Textures {

    public static JsonValue splits;
    public static Drawable

    mono_white, mono_main, mono_alpha, mono_accent, mono_red,

    slashed_white, slashed_main, slashed_alpha, slashed_accent,

    dotted_white, dotted_main, dotted_alpha, dotted_accent,

    white, dark, accent, circle;

    public static TextureRegion engine;

    public static void load() {
        splits = new JsonReader().parse(files.internal("ui/splits.json"));

        mono_white = ui("mono-white");
        mono_main = tint(mono_white, Palette.main);
        mono_alpha = tint(mono_white, Palette.alpha);
        mono_accent = tint(mono_white, Palette.accent);
        mono_red = tint(mono_white, Palette.red);

        slashed_white = ui("slashed-white");
        slashed_main = tint(slashed_white, Palette.main);
        slashed_alpha = tint(slashed_white, Palette.alpha);
        slashed_accent = tint(slashed_white, Palette.accent);

        dotted_white = ui("dotted-white");
        dotted_main = tint(dotted_white, Palette.main);
        dotted_alpha = tint(dotted_white, Palette.alpha);
        dotted_accent = tint(dotted_white, Palette.accent);

        white = ui("white");
        circle = ui("circle");

        var drawable = (TextureRegionDrawable) white;
        dark = drawable.tint(Palette.background.cpy().a(.5f));
        accent = drawable.tint(Palette.accent);

        engine = load("sprites/", "engine");
    }

    public static TextureRegion load(String path, String name) {
        Texture texture = new Texture(path + name + ".png");
        texture.setFilter(TextureFilter.linear); // for better experience

        var region = atlas.addRegion(name, texture, 0, 0, texture.width, texture.height);
        if (splits.has(name)) {
            region.splits = splits.get(name).asIntArray();
            region.pads = splits.get(name + "-pads").asIntArray();
        }

        return region;
    }

    public static Drawable drawable(String path, String name) {
        load(path, name);
        return atlas.drawable(name);
    }

    public static Drawable tint(Drawable from, Color tint) {
        return ((NinePatchDrawable) from).tint(tint);
    }

    @IconLoader
    public static Drawable icon(String name) {
        return drawable("icons/", name);
    }

    public static Drawable ui(String name) {
        return drawable("ui/", name);
    }

    public static TileDrawer tile(String name) {
        Texture texture = new Texture("tiles/" + name + ".png");
        return new TileDrawer(texture);
    }
}
