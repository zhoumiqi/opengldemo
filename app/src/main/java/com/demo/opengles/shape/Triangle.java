package com.demo.opengles.shape;

import com.demo.opengles.shader.TriangleShader;

/**
 * 三角形
 */
public class Triangle extends Shape<TriangleShader> {

    @Override
    public float[] getVertexCoordinates() {
        return new float[]{
                0.0f, 1.0f, 0.0f,//顶点
                1.0f, 0.0f, 0.0f,//右下角
                -1.0f, 0.0f, 0.0f//左下角  注意不要以","结尾，不然会多出一个无效的顶点坐标导致绘图形显示异常
        };
    }

    @Override
    public float[] getFragColor() {
        return new float[]{
                1.0f, //r
                0.0f, //g
                0.0f, //b
                1.0f  //a
        };//红色不透明
    }
}
