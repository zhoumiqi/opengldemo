package com.demo.opengles.shader;

import android.opengl.GLES20;

import com.demo.opengles.utils.OpenGlUtils;

public class TriangleShader implements Shader {
    //顶点着色器
    private static final String VERTEX_SHADER =
            "//根据所设置的顶点数据而插值后的顶点坐标\n" +
                    "attribute vec4 vPosition;" +
                    "void main(){" +
                    "//设置最终坐标\n" +
                    "gl_Position = vPosition;" +
                    "}";

    //片元着色器
    private static final String FRAGMENT_SHADER =
            "//设置float 类型默认精度，顶点着色器默认highp,片元着色器需要用户声明\n" +
                    "precision mediump float;" +
                    "//颜色值，vec4代表四维向量，此处由用户传入，数据格式为{r,g,b,a}\n" +
                    "uniform vec4 vColor;" +
                    "void main(){" +
                    "//该片元最终颜色值\n" +
                    "gl_FragColor = vColor;" +
                    "}";

    @Override
    public int loadVertexShader() {
        return OpenGlUtils.loadShader(VERTEX_SHADER, GLES20.GL_VERTEX_SHADER);
    }

    @Override
    public int loadFragmentShader() {
        return OpenGlUtils.loadShader(FRAGMENT_SHADER, GLES20.GL_FRAGMENT_SHADER);
    }
}
