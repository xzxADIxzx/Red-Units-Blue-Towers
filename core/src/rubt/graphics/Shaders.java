package rubt.graphics;

import arc.graphics.Color;
import arc.graphics.gl.FrameBuffer;
import arc.math.Mathf;

import static arc.Core.*;

public class Shaders {

    public static NormalShader normal;
    public static FrameBuffer buffer;

    public static void load() {
        normal = new NormalShader();
        buffer = new FrameBuffer();
    }

    public static class Shader extends arc.graphics.gl.Shader {

        public Shader(String vertex, String fragment) {
            super(files.internal("shaders/" + vertex), files.internal("shaders/" + fragment));
        }

        public Shader(String fragment) {
            super(files.internal("bloomshaders/screenspace.vert"), files.internal("shaders/" + fragment));
        }

        public void begin() {
            buffer.resize(graphics.getWidth(), graphics.getHeight());
            buffer.begin(Color.clear);
        }

        public void end() {
            buffer.end();
            buffer.blit(this);
        }
    }

    public static class NormalShader extends Shader {

        public float last; // crutch, but noway

        public NormalShader() {
            super("normal.frag");
        }

        public void draw(float rotation, Runnable draw) {
            last = rotation * Mathf.degRad;

            begin();
            draw.run();
            end();
        }

        @Override
        public void apply() {
            setUniformf("u_rotation", last);
        }
    }
}