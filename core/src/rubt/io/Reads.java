package rubt.io;

import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.util.io.ByteBufferInput;

import java.io.*;
import java.nio.ByteBuffer;

/** Builtin reads but with some extra methods for convenience. */
public class Reads extends arc.util.io.Reads {

    public Reads(DataInput input) {
        super(input);
    }

    public static Reads of(DataInput input) {
        return new Reads(input);
    }

    public static Reads of(InputStream stream) {
        return new Reads(new DataInputStream(stream));
    }

    public static Reads of(ByteBuffer buffer) {
        return new Reads(new ByteBufferInput(buffer));
    }

    public static Reads of(byte[] bytes) {
        return of(ByteBuffer.wrap(bytes));
    }

    /** Read nullable byte array. */
    public byte[] nb(int length) {
        return bool() ? b(length) : null;
    }

    /** Read position. */
    public Position p() {
        return new Vec2(f(), f());
    }
}
