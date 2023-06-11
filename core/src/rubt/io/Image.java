package rubt.io;

import arc.files.Fi;
import arc.graphics.*;
import arc.graphics.g2d.TextureRegion;
import arc.struct.Seq;
import rubt.graphics.Palette;

import static arc.Core.*;

import java.nio.ByteBuffer;

public class Image { // TODO increase size up to 48px by saving only RGB

    public static final Seq<String> extensions = Seq.with("png", "jpg", "jpeg", "bmp");

    /** 40x40 pixels * 4 byte. */
    public static final int rgbaSize = 40 * 40 * 4;
    /** 40x40 pixels * 3 byte. */
    public static final int rgbSize = 40 * 40 * 3;

    /** Reads an image, scales it to 40x40 pixels and returns it as a byte[]. */
    public static byte[] read(Fi file) {
        var pixmap = Pixmaps.scale(new Pixmap(file), 40, 40, true);

        byte[] output = new byte[rgbaSize];
        pixmap.pixels.position(0).get(output); // 6KiB is quite a lot but tolerable

        return output;
    }

    /** Wraps an image into {@link TextureRegion}. */
    public static TextureRegion wrap(Fi file) {
        var texture = new Texture(file); // some magic to update the texture
        app.post(() -> texture.load(texture.getTextureData()));

        return new TextureRegion(texture);
    }

    /** Creates an image from the raw data, adds an 4px outline and wraps it into {@link TextureRegion}. */
    public static TextureRegion wrap(byte[] data, String fileName) {
        Fi temp = getTemp(fileName);
        if (temp.exists()) return wrap(temp); // use already uploaded avatar

        // idk why but Pixmap can only use direct bytebuffer
        var buffer = ByteBuffer.allocateDirect(40 * 40 * 4).put(data);
        var pixmap = new Pixmap(buffer, 40, 40);

        var outline = Pixmaps.resize(pixmap, 48, 48); // increase the size to fit an outline
        temp.writePng(outline.outline(Palette.accent, 4)); // save avatar

        return wrap(temp);
    }

    /** Returns a file for temporary storage of an avatar. */
    public static Fi getTemp(String fileName) {
        return settings.getDataDirectory().child("avatars/" + fileName);
    }

    /** Removes all uploaded avatars. */
    public static void clearTemp() {
        settings.getDataDirectory().child("avatars").deleteDirectory();
    }
}
