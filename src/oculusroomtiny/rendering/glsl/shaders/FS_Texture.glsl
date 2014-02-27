#version 330

in vec4 oColor;
in vec2 oUV;
uniform sampler2D texture0;

out vec4 outputColor;

void main(){

    //outputColor = interpColor;

    outputColor = oColor * texture2D(texture0, oUV);

    //if(outputColor.a < 0.8)
      //  discard;
}
