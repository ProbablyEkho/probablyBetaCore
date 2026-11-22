#version 150

uniform sampler2D DiffuseSampler0;

in vec2 texCoord;
in vec2 oneTexel;

uniform vec2 InSize;

uniform float Resolution;
uniform float MosaicSize;
uniform float PlayerHealth;
uniform float Dead;

out vec4 fragColor;

void main() {
    vec2 mosaicInSize = InSize / MosaicSize;
    vec2 fractPix = fract(texCoord * mosaicInSize) / mosaicInSize;

    vec4 baseTexel = texture(DiffuseSampler0, texCoord - fractPix);

    vec3 fractTexel = (baseTexel.rgb - fract(baseTexel.rgb * Resolution) / Resolution - PlayerHealth / 4) * mix(1.0, 2.0, PlayerHealth);
    float luma = dot(fractTexel, vec3(0.3, 0.59, 0.11));
    vec3 chroma = vec3(luma * 0.75, luma * 0.5, luma * 0.5);
    baseTexel.rgb = mix(fractTexel, chroma, PlayerHealth);
	baseTexel.rgb = clamp(mix(baseTexel.rgb, (baseTexel.rgb - 0.5) * 2.0, Dead), 0.0, 1.0);
    baseTexel.a = 1.0;

    fragColor = baseTexel;
}
