//默认生成的 版本号会加载报错，去掉
//设置float 类型默认精度，顶点着色器默认highp,片元着色器需要用户声明
precision mediump float;
//颜色值，vec4代表四维向量，此处由用户传入，数据格式为{r,g,b,a}
uniform vec4 vColor;
void main() {
    //该片元最终颜色值
    gl_FragColor = vColor;
}