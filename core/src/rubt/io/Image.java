package rubt.io;

import arc.files.Fi;
import arc.graphics.Pixmap;
import arc.graphics.Pixmaps;
import arc.struct.Seq;

public class Image {

    public static final Seq<String> extensions = Seq.with("png", "jpg", "jpeg", "bmp");

    /** Reads an image, scales it to 40x40 pixels and returns it as a byte[]. */
    public static byte[] read(Fi file) {
        var pixmap = Pixmaps.scale(new Pixmap(file), 40, 40, true);

        byte[] output = new byte[40 * 40 * 4]; // 40x40 pixels * 4 byte
        pixmap.pixels.position(0).get(output); // 6KiB is quite a lot but tolerable

        return output;
    }
}
