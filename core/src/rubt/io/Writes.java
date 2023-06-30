package rubt.io;

import arc.math.geom.Position;
import arc.util.io.ByteBufferOutput;

import java.io.*;
import java.nio.ByteBuffer;

/** Builtin writes but with some extra methods for convenience. */
public class Writes extends arc.util.io.Writes {

    public Writes(DataOutput output) {
        super(output);
    }

    public static Writes of(DataOutput output) {
        return new Writes(output);
    }

    public static Writes of(OutputStream stream) {
        return new Writes(new DataOutputStream(stream));
    }

    public static Writes of(ByteBuffer buffer) {
        return new Writes(new ByteBufferOutput(buffer));
    }

    /** Write nullable byte array. */
    public void nb(byte[] array) {
        bool(array != null);
        if (array != null) b(array);
    }

    /** Write position. */
    public void p(Position pos) {
        f(pos.getX());
        f(pos.getY());
    }
}
