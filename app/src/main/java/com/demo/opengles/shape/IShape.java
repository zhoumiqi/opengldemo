package com.demo.opengles.shape;

/**
 * 定义形状
 */
public interface IShape {
    /**
     * 获取顶点坐标数据
     *
     * @return 顶点坐标
     */
    float[] getVertexCoordinates();

    /**
     * 获取片元颜色数据
     *
     * @return 颜色数据
     */
    float[] getFragColor();

}
