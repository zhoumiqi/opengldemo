package com.demo.opengles.shape;

import com.demo.opengles.shader.TriangleWithTextureShader;

public class TriangleWithTexture extends Shape<TriangleWithTextureShader> {
    @Override
    public float[] getVertexCoordinates() {
        return new float[]{
                0.0f, 1.0f, 0.0f,//顶点
                1.0f, 0.0f, 0.0f,//右下角
                -1.0f, 0.0f, 0.0f//左下角  注意不要以","结尾，不然会多出一个无效的顶点坐标导致绘图形显示异常
        };
    }

    @Override
    public float[] getFragColorOrTexCoords() {
        return new float[]{
                0.5f,0.0f, //顶点
                1.0f,1.0f, //右下角
                0.0f,1.0f  //左下角
        };//纹理坐标(二维)
    }

    @Override
    public int getVertexCount() {
        return getVertexCoordinates().length / getCoordsCountPerVertex();
    }

    @Override
    public int getCoordsCountPerVertex() {
        return 3;
    }

    @Override
    public int getVectorCountPerFragColorOrTexCoord() {
        return 2;
    }
}
