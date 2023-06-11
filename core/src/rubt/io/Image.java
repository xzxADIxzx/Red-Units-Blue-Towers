package rubt.io;

import arc.files.Fi;
import arc.graphics.*;
import arc.graphics.g2d.TextureRegion;
import arc.struct.Seq;

import static arc.Core.*;

import java.util.Arrays;

public class Image {
    public static final Seq<String> extensions = Seq.with("png", "jpg", "jpeg", "bmp");

    /** 48x48 pixels * 4 byte. */
    public static final int rgbaSize = 48 * 48 * 4;
    /** 48x48 pixels * 3 byte. */
    public static final int rgbSize = 48 * 48 * 3;

    // region rgb/rgba

    /** Converts a byte image from RGBA to RGB to save space. */
    public static byte[] rgba2rgb(byte[] data) {
        for (int i = 0, j = 0; i < data.length; i++)
            if (i % 4 != 3) data[j++] = data[i]; // skip alpha

        return Arrays.copyOfRange(data, 0, rgbSize); // idk why but copyOfRange is slightly faster than copyOf
    }

    /** Converts a byte image from RGB to RGBA and writes into {@link Pixmap}. */
    public static Pixmap rgb2rgba(byte[] data) {
        var pixmap = new Pixmap(48, 48);

        for (int i = 0; i < data.length; i++) {
            pixmap.pixels.put(data[i]);
            if (i % 3 == 2) pixmap.pixels.put((byte) 0xFF); // put alpha
        }

        return pixmap;
    }

    // endregion
    // region read/wrap

    /** Reads an image, scales it to 48x48 pixels and returns it as a byte[]. */
    public static byte[] read(Fi file) {
        var pixmap = Pixmaps.scale(new Pixmap(file), 48, 48, false);
        Pixmaps.antialias(pixmap); // TODO dialog with some settings and preview

        byte[] output = new byte[rgbaSize];
        pixmap.pixels.position(0).get(output); // 6KiB is quite a lot but tolerable

        return rgba2rgb(output);
    }

    /** Wraps an image into {@link TextureRegion}. */
    public static TextureRegion wrap(Fi file) {
        var texture = new Texture(file); // some magic to update the texture
        app.post(() -> texture.load(texture.getTextureData()));

        return new TextureRegion(texture);
    }

    /** Creates an image from the raw data and wraps it into {@link TextureRegion}. */
    public static TextureRegion wrap(byte[] data, String fileName) {
        Fi temp = getTemp(fileName);

        if (temp.exists()) return wrap(temp); // use an already uploaded avatar if possible
        temp.writePng(rgb2rgba(data)); // save avatar

        return wrap(temp);
    }

    // endregion
    // region temp

    /** Returns a file for temporary storage of an avatar. */
    public static Fi getTemp(String fileName) {
        return settings.getDataDirectory().child("avatars/" + fileName); // TODO use id instead of name?
    }

    /** Removes all uploaded avatars. */
    public static void clearTemp() {
        settings.getDataDirectory().child("avatars").deleteDirectory();
    }

    // endregion
}
