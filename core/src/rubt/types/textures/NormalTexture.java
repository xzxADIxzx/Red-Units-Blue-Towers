package rubt.types.textures;

import arc.graphics.Color;
import arc.graphics.Pixmap;
import arc.graphics.Texture;
import arc.graphics.g2d.TextureRegion;
import arc.graphics.gl.PixmapTextureData;
import arc.util.Tmp;

public class NormalTexture {

    public Pixmap raw, out;
    public TextureRegion region;

    public NormalTexture(String file) {
        raw = new Pixmap(file);
        out = raw.copy();
        region = new TextureRegion(new Texture(out));
    }

    public TextureRegion region(float rotation) {
        raw.each((x, y) -> {
            Tmp.c1.set(raw.get(x, y));
            if (Tmp.c1.a == 0f) return;

            Tmp.v1.set(Tmp.c1.r - .5f, Tmp.c1.g - .5f).rotate(rotation);
            float n = Tmp.v1.angleTo(-1f, -1f) / 180f;

            out.set(x, y, Color.rgba8888(n, n, n, Tmp.c1.a));
        });

        region.texture.load(new PixmapTextureData(out, false, false));
        return region;
    }
}
