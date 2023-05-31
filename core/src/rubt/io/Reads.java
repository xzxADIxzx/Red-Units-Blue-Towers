package rubt.io;

import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.util.io.ByteBufferInput;

import java.io.*;
import java.nio.ByteBuffer;

/** Builtin reads but with some extra methods for convenience. */
public class Reads extends arc.util.io.Reads {

    private static Reads instance = new Reads();

    public Reads() {
        super(null);
    }

    public static Reads of(DataInput output) {
        instance.input = output;
        return instance;
    }

    public static Reads of(InputStream output) {
        instance.input = new DataInputStream(output);
        return instance;
    }

    public static Reads of(ByteBuffer buffer) {
        instance.input = new ByteBufferInput(buffer);
        return instance;
    }

    public static Reads of(byte[] bytes) {
        return of(ByteBuffer.wrap(bytes));
    }

    public Position p() {
        return new Vec2(f(), f());
    }
}
