attribute vec4 a_position;
attribute vec2 a_texCoord0;
varying vec2 v_texCoords;

void main() {
	gl_Position = a_position;
	v_texCoords = a_texCoord0;
}