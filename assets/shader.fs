#version 120

uniform sampler2D sampler;

uniform int winHeight;
uniform int winWidth;

uniform int widthResolution;
uniform int heightResolution;

uniform vec2 lightPositions[8];
uniform int lightsOn[8];
uniform float lightRadius[8];


varying vec2 tex_coords;

void main(){

    gl_FragColor = vec4(0,0,0,1);

    float screenCoordX = ((2.0f*gl_FragCoord.x / (winWidth)) - 2.0f);
    float screenCoordY = ((2.0f*gl_FragCoord.y / (winHeight)) - 2.0f);

    float smallestDistRatio = 1;

    bool inLight = false;

    for(int i=0; i< 8; i++){
        if(lightsOn[i] == 1){
            float dist = ((screenCoordX - lightPositions[i].x)*(screenCoordX - lightPositions[i].x)) + ((screenCoordY - lightPositions[i].y)*(screenCoordY - lightPositions[i].y));
            if(dist < lightRadius[i] && (dist / lightRadius[i]) < smallestDistRatio){
                smallestDistRatio = dist / lightRadius[i];
                float attenuation = 1.0 - (dist / lightRadius[i]);
                vec4 newColor = texture2D(sampler, tex_coords) * vec4(attenuation,attenuation,attenuation,pow(attenuation,4));

                gl_FragColor = newColor;

            }


        }
    }


}