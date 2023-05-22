uniform sampler2D u_texture;
uniform float u_rotation;

varying vec2 v_texCoords;

const vec2 light = vec2(-.7, -.7);
const float div = 6.2830;

vec2 rot(vec2 vec, float angle) {
	float c = cos(angle), s = sin(angle);
    return vec * mat2(c, s, -s, c);
}

void main() {
    vec4 orig = texture2D(u_texture, v_texCoords.xy);
    float n = acos(dot(rot(orig.xy - .5, u_rotation), light)) / div;

    gl_FragColor = vec4(n, n, n, orig.w);
}