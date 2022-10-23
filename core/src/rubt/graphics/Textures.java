package rubt.graphics;

import arc.graphics.Texture;
import arc.graphics.Texture.TextureFilter;
import arc.scene.style.Drawable;

import static arc.Core.*;

public class Textures {

    public static Drawable whiteui, circle;

    public static void load() {
        whiteui = loadUI("whiteui");
        circle = loadUI("circle");
    }

    public static void load(String path, String name) {
        Texture texture = new Texture(path + name + ".png");
        texture.setFilter(TextureFilter.linear); // for better experience

        atlas.addRegion(name, texture, 0, 0, texture.width, texture.height);
    }

    public static Drawable loadUI(String name) {
        load("sprites/ui/", name);
        return atlas.drawable(name);
    }

    public static Drawable loadIcon(String name) {
        load("sprites/icons/", name);
        return atlas.drawable(name);
    }
}
