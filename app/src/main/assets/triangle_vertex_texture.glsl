//三角形顶点着色器(含纹理坐标)
attribute vec4 vPosition;//顶点位置
attribute vec2 aTexCoord;//顶点纹理坐标
varying vec2 vTextureCoord;//用于传递给片元着色器的易变变量
void main() {
    gl_Position = vPosition;
    vTextureCoord = aTexCoord;
}
