//our attributes
attribute vec2 a_position;
attribute vec4 a_color;

//our camera matrix
uniform mat4 u_projTrans;

//send the color out to the fragment shader
varying vec4 vColor;

void main() {
	gl_Position = u_projTrans * vec4(a_position.xy, 0.0, 1.0);
}
/*uniform mat4 u_projTrans;

attribute vec4 a_position;
attribute vec2 a_texCoord0;
attribute vec4 a_color;

varying vec4 v_color;
varying vec2 v_texCoord;

uniform vec2 u_viewportInverse;

void main() {
    gl_Position = u_projTrans * a_position;
    v_texCoord = a_texCoord0;
    //v_color = a_color;
}*/