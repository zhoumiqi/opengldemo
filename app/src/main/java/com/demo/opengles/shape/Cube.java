package com.demo.opengles.shape;

import com.demo.opengles.shader.CubeShader;

/**
 * 正方体
 * 参考：https://www.jianshu.com/p/312786bafad8
 */
public class Cube extends Shape<CubeShader> {

    @Override
    public float[] getVertexCoordinates() {
        return new float[]{
                -1.0f, 1.0f, 1.0f,    //正面左上0
                -1.0f, -1.0f, 1.0f,   //正面左下1
                1.0f, -1.0f, 1.0f,    //正面右下2
                1.0f, 1.0f, 1.0f,     //正面右上3
                -1.0f, 1.0f, -1.0f,    //反面左上4
                -1.0f, -1.0f, -1.0f,   //反面左下5
                1.0f, -1.0f, -1.0f,    //反面右下6
                1.0f, 1.0f, -1.0f     //反面右上7
        };
    }

    @Override
    public float[] getFragColorOrTexCoords() {
        return new float[]{
                0f, 1f, 0f, 1f,
                0f, 1f, 0f, 1f,
                0f, 1f, 0f, 1f,
                0f, 1f, 0f, 1f,
                1f, 0f, 0f, 1f,
                1f, 0f, 0f, 1f,
                1f, 0f, 0f, 1f,
                1f, 0f, 0f, 1f
        };
    }

    @Override
    public short[] getIndexData() {
        return new short[]{
                6, 7, 4, 6, 4, 5,    //后面
                6, 3, 7, 6, 2, 3,    //右面
                6, 5, 1, 6, 1, 2,    //下面
                0, 3, 2, 0, 2, 1,    //正面
                0, 1, 5, 0, 5, 4,    //左面
                0, 7, 3, 0, 4, 7    //上面
        };
    }

    @Override
    public int getCoordsCountPerVertex() {
        return 3;
    }

    @Override
    public int getVectorCountPerFragColorOrTexCoord() {
        return 4;
    }

    @Override
    public boolean haveMatrixUniform() {
        return true;
    }

    @Override
    public boolean needEnableDepthTest() {
        return true;
    }

    @Override
    public boolean drawWithSpecifiedIndex() {
        return true;
    }
}
