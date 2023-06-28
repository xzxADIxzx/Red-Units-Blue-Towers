package rubt.io;

import arc.math.geom.Position;
import arc.util.io.ByteBufferOutput;

import java.io.*;
import java.nio.ByteBuffer;

/** Builtin writes but with some extra methods for convenience. */
public class Writes extends arc.util.io.Writes {

    private static Writes instance = new Writes();

    public Writes() {
        super(null);
    }

    public static Writes of(DataOutput output) {
        instance.output = output;
        return instance;
    }

    public static Writes of(OutputStream output) {
        instance.output = new DataOutputStream(output);
        return instance;
    }

    public static Writes of(ByteBuffer buffer) {
        instance.output = new ByteBufferOutput(buffer);
        return instance;
    }

    public void nb(byte[] array) {
        bool(array != null);
        if (array != null) b(array);
    }

    public void p(Position pos) {
        f(pos.getX());
        f(pos.getY());
    }
}
