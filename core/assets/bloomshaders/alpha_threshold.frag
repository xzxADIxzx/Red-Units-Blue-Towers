uniform lowp sampler2D u_texture0;
uniform lowp vec2 threshold;

varying vec2 v_texCoords;

void main() {
	vec4 color = texture2D(u_texture0, v_texCoords);
	gl_FragColor = color;
}