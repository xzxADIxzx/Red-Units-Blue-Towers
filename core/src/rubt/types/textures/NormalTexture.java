package rubt.types.textures;

import arc.graphics.Color;
import arc.graphics.Pixmap;
import arc.graphics.Texture;
import arc.graphics.Texture.TextureFilter;
import arc.graphics.g2d.TextureRegion;
import arc.graphics.gl.PixmapTextureData;
import arc.struct.Seq;
import arc.util.Tmp;

public class NormalTexture {

    public static Seq<CachedTexture> textures = new Seq<>();
    public static int pointer;

    public Pixmap raw, out;
    public TextureRegion region;

    public NormalTexture(String file) {
        raw = new Pixmap(file);
        out = raw.copy();

        region = new TextureRegion(texture(out, -1f));
        region.texture.setFilter(TextureFilter.linear);
    }

    public TextureRegion region(float rotation) {
        var cache = texture();
        if (cache != null && cache.rotation == rotation) {
            region.texture = cache.texture;
            return region;
        }

        raw.each((x, y) -> {
            Tmp.c1.set(raw.get(x, y));
            if (Tmp.c1.a == 0f) return;

            Tmp.v1.set(Tmp.c1.r - .5f, Tmp.c1.g - .5f).rotate(rotation);
            float n = Tmp.v1.angleTo(-1f, -1f) / 180f;

            out.set(x, y, Color.rgba8888(n, n, n, Tmp.c1.a));
        });

        region.texture = cache == null ? texture(out, rotation) : cache.texture(out, rotation);
        return region;
    }

    public static CachedTexture texture() {
        return ++pointer < textures.size ? textures.get(pointer) : null;
    }

    public static Texture texture(Pixmap pixmap, float rotation) {
        Texture texture = new Texture(pixmap);
        textures.add(new CachedTexture(texture, rotation));
        return texture;
    }

    public static class CachedTexture {

        public final Texture texture;
        public float rotation;

        public CachedTexture(Texture texture, float rotation) {
            this.texture = texture;
            this.rotation = rotation;
        }

        public Texture texture(Pixmap pixmap, float rotation) {
            texture.load(new PixmapTextureData(pixmap, false, false));
            this.rotation = rotation;

            return texture;
        }
    }
}
