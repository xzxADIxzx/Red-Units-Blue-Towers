package rubt.ui;

import arc.graphics.Texture;
import arc.graphics.Texture.TextureFilter;
import arc.scene.style.Drawable;

import static arc.Core.*;

public class Textures {

    public static Drawable whiteui, circle;

    public static void load() {
        whiteui = loadui("whiteui");
        circle = loadui("circle");
    }

    public static Drawable load(String path, String name) {
        Texture texture = new Texture(path + name + ".png");
        texture.setFilter(TextureFilter.linear); // for better experience

        atlas.addRegion(name, texture, 0, 0, texture.width, texture.height);
        return atlas.drawable(name);
    }

    public static Drawable loadui(String name) {
        return load("sprites/ui/", name);
    }
}
