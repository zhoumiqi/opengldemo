//根据所设置的顶点数据而插值后的顶点坐标
attribute vec4 vPosition;
void main() {
    //设置最终坐标
    gl_Position = vPosition;
}