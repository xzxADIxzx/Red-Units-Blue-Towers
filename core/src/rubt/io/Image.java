package rubt.io;

import arc.files.Fi;
import arc.graphics.Pixmap;
import arc.graphics.Pixmaps;
import arc.struct.Seq;

public class Image {

    public static final Seq<String> extensions = Seq.with("png", "jpg", "jpeg", "bmp");

    /** Reads an image, scales it to 48x48 pixels and returns it as a byte[]. */
    public static byte[] read(Fi file) {
        var pixmap = Pixmaps.scale(new Pixmap(file), 48, 48, true);

        byte[] output = new byte[48 * 48 * 4]; // 48x48 pixels * 4 byte
        pixmap.pixels.position(0).get(output); // 9KiB is quite a lot

        return output;
    }
}
