#type vertex
#version 330 core

layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aTexCoords;
layout (location=3) in float aTextureID;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

out vec4 fColor;
out vec2 fTexCoords;
out float fTextureID;

void main() {
    // Pass through
    fColor = aColor;
    fTexCoords = aTexCoords;
    fTextureID = aTextureID;

    // Position
    gl_Position = projectionMatrix * viewMatrix * vec4(aPos, 1.0);
}

#type fragment
#version 330 core

in vec4 fColor;
in vec2 fTexCoords;
in float fTextureID;

uniform sampler2D uTexture[10];

out vec4 color;

void main() {
    int id = int(fTextureID);
    vec4 texture_colour = fColor;
    if(id >= 0) {
        texture_colour = fColor * texture(uTexture[id], fTexCoords);
    }
    color = texture_colour;
}
