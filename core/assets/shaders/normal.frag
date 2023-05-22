uniform sampler2D u_texture;
uniform float u_rotation;

varying vec2 v_texCoords;

const vec2 light = vec2(-.7, -.7);
const float div = 6.2830;

void main() {
    vec4 orig = texture2D(u_texture, v_texCoords.xy);
    float n = acos(dot(orig.xy - .5, light)) / div;

    gl_FragColor = vec4(n, n, n, orig.w);
}