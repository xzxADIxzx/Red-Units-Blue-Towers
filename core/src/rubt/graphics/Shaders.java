package rubt.graphics;

import arc.graphics.Color;
import arc.graphics.gl.FrameBuffer;
import arc.util.Time;

import static arc.Core.*;

public class Shaders {

    public static FrameBuffer buffer;

    public static void load() {
        buffer = new FrameBuffer();
    }

    public static class Shader extends arc.graphics.gl.Shader {

        public Shader(String vertex, String fragment) {
            super(files.internal("shaders/" + vertex), files.internal("shaders/" + fragment));
        }

        public void begin(){
            buffer.resize(graphics.getWidth(), graphics.getHeight());
            buffer.begin(Color.clear); 
        }

        public void end(){
            buffer.end();
            buffer.blit(this);
        }
    }
}