#type vertex
#version 330 core

layout (location=0) in vec3 aPos;
layout (location=2) in vec2 aTextCoords;

out vec2 textCoords;

void main() {
    textCoords = aTextCoords;

    gl_Position = vec4(aPos, 1.0);
}

#type fragment
#version 330 core

in vec2 textCoords;

uniform sampler2D uTexture;

out vec4 color;

void main() {
    color = vec4(1.0f, 1.0f, 0.0f, 1.0f);
}
