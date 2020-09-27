package com.demo.opengles.shape;

import com.demo.opengles.shader.Shader;
import com.demo.opengles.shader.SquareShader;
import com.demo.opengles.utils.OpenGlUtils;

/**
 * 正方形
 */
public class Square extends Shape<SquareShader> {
    //x轴半边长
    private float mXVertex;
    //y轴半边长
    private float mYVertex;

    public Square() {
        float vertexCordLength = OpenGlUtils.getHalfScreen();
        this.mXVertex = vertexCordLength / (float) OpenGlUtils.getScreenWidth();
        this.mYVertex = vertexCordLength / (float) OpenGlUtils.getScreenHeight();
    }


    @Override
    public float[] getVertexCoordinates() {
        return new float[]{
                -mXVertex, mYVertex, 0.0f,
                mXVertex, mYVertex, 0.0f,
                -mXVertex, -mYVertex, 0.0f,
                mXVertex, -mYVertex, 0.0f
        };
    }

    @Override
    public float[] getFragColor() {
        return new float[]{
                0.0f,0.0f, 1.0f,1.0f,
                0.0f,0.0f, 1.0f,1.0f,
                0.0f,0.0f, 1.0f,1.0f,
                0.0f,0.0f, 1.0f,1.0f
        };//蓝色不透明
    }

    @Override
    public Shader getShader() {
        return super.getShader();
    }
}
