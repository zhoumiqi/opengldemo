package com.demo.opengles.shape;

import com.demo.opengles.shader.QuadrilateralShader;
import com.demo.opengles.shader.Shader;

/**
 * 四边形
 */
public class Quadrilateral extends Shape<QuadrilateralShader> {
    @Override
    public float[] getVertexCoordinates() {
        return new float[]{
                0.0f, 1.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                0.0f, -1.0f, 0.0f
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

    @Override
    public Shader getShader() {
        return super.getShader();
    }

    @Override
    public int getVertexCount() {
        return getVertexCoordinates().length/getCoordinatesCountPerVertex();
    }

    @Override
    public int getCoordinatesCountPerVertex() {
        return 3;
    }

    public @Override
    int getVectorCountPerFragColor() {
        return 4;
    }
}
