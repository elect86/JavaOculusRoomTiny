#version 330

uniform sampler2D texture0;

layout(std140) uniform lighting {
    vec3 ambient;
    float lightCount;
    vec4 lightPos[8];
    vec4 lightColor[8];
};

in vec3 oVPos;
in vec4 oColor;
in vec2 oUV;
in vec3 oNormal;

out vec4 outputColor;

vec4 getLight(){

    vec3 norm = normalize(oNormal);
    
    vec3 light = ambient;

    for (int i = 0; i < int(lightCount); i++) {

        vec3 ltp = lightPos[i].xyz - oVPos;

        float ldist = length(ltp);

        ltp = normalize(ltp);

        light += clamp(lightColor[i].rgb * oColor.rgb * (dot(norm, ltp) / ldist), 0.0, 1.0f);
    }
    return vec4(light, oColor.a);
}

void main(){

    outputColor = getLight() * texture2D(texture0, oUV);
//if(lightCount == 3)
/*if(lightColor[2].z==3f)
outputColor=vec4(1,0,0,1);
else
outputColor=vec4(0,1,0,1);*/
}