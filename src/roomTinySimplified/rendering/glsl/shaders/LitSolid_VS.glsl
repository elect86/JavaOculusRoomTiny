#version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec4 color;
layout (location = 2) in vec3 normal;

uniform mat4 projection;
uniform mat4 view;

out vec3 oVPos;
out vec4 oColor;
out vec3 oNormal;

void main(){

    gl_Position = projection * (view * vec4(position, 1));

    oNormal = vec3(view * vec4(normal, 0));

    oVPos = vec3(view * vec4(position, 1));

    oColor = color;
}