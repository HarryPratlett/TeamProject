#version 120

uniform sampler2D sampler;
uniform float opacity;

uniform vec4 colour;

varying vec2 tex_coords;

void main(){


    gl_FragColor = texture2D(sampler, tex_coords);

    gl_FragColor = vec4(gl_FragColor.x,gl_FragColor.y,gl_FragColor.z,0.1);
}