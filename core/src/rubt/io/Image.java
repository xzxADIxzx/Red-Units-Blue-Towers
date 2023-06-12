package rubt.io;

import arc.graphics.*;
import arc.graphics.g2d.TextureRegion;
import arc.struct.Seq;

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
    // region read/write

    /** Writes the rgb channels of an image to byte[]. */
    public static byte[] write(Pixmap pixmap) {
        byte[] output = new byte[rgbaSize];
        pixmap.pixels.position(0).get(output); // 9KiB is quite a lot but tolerable

        return rgba2rgb(output);
    }

    /** Wraps an image into {@link TextureRegion}. */
    public static TextureRegion wrap(Pixmap pixmap) {
        return new TextureRegion(new Texture(pixmap));
    }

    /** Reads an image from a raw data. */
    public static TextureRegion read(byte[] data) {
        return wrap(rgb2rgba(data));
    }

    // endregion
}
