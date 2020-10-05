//三角形片元着色器(含纹理坐标)
precision mediump float;
varying vec2 vTextureCoord;
uniform sampler2D sTexture;//纹理采样器
void main() {
    gl_FragColor = texture2D(sTexture, vTextureCoord);//进行纹理采样
}
