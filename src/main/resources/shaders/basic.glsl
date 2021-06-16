#type all
#version 410

struct PassedAttributes
{
    vec2 uvs;
};

#type vertex
in vec3 position;
in vec2 uvs;

uniform mat4 uProjection;
uniform mat4 uView;

out PassedAttributes vPassedAttributes;

void main()
{
    gl_Position =uProjection * uView * vec4(position, 1.0f);

    vPassedAttributes.uvs = uvs;
}


#type fragment
in PassedAttributes vPassedAttributes;

out vec4 fragColor;

uniform sampler2D sampleTexture;

void main() {
    fragColor = vec4(0.2f, 0.75f, 0.35f, 1.0f);//texture(sampleTexture, vPassedAttributes.uvs);
}
