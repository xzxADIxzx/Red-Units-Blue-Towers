package rubt.io;

import arc.files.Fi;
import arc.graphics.Pixmap;
import arc.struct.Seq;
import arc.util.Tmp;

import java.io.IOException;

import javax.imageio.ImageIO;

public class Image {

    public static final Seq<String> extensions = Seq.with("png", "jpg", "jpeg", "bmp");

    public static Pixmap read(Fi file) throws IOException {
        return switch (file.extension()) {
            case "png" -> new Pixmap(file);
            case "jpg", "jpeg", "bmp" -> readNonPng(file);
            default -> throw new IOException("Unsupported image format");
        };
    }

    public static Pixmap readNonPng(Fi file) throws IOException {
        var image = ImageIO.read(file.file());
        var pixmap = new Pixmap(image.getWidth(), image.getHeight());

        pixmap.each((x, y) -> pixmap.set(x, y, Tmp.c1.rgb888(image.getRGB(x, y)).rgba()));
        return pixmap;
    }
}
