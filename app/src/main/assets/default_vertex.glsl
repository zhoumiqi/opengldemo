//默认生成的 版本号会加载报错，去掉
//根据所设置的顶点数据而插值后的顶点坐标
attribute vec4 vPosition;
attribute vec4 aColor;
varying vec4 vColor;
void main() {
    //设置最终坐标
    gl_Position = vPosition;
    vColor = aColor;
}