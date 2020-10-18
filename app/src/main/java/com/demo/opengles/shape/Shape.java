package com.demo.opengles.shape;

import com.demo.opengles.shader.Shader;
import com.demo.opengles.utils.Constants;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.Buffer;

public abstract class Shape<T extends Shader> implements IShape {
    private T mShader;

    public Shape() {
        Shader shader = getShader();
        if (shader != null) {
            this.mShader = (T) shader;
        } else {
            this.mShader = createDefaultShader();
        }
    }

    public int getVertexId() {
        return (mShader != null) ? mShader.loadVertexShader() : 0;
    }

    public int getFragmentId() {
        return (mShader != null) ? mShader.loadFragmentShader() : 0;
    }

    public Shader getShader() {
        return null;
    }

    private T createDefaultShader() {
        //获得当前类的泛型参数类型
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        if (type != null) {
            Type[] actualTypeArguments = type.getActualTypeArguments();
            Class<T> tClass = (Class<T>) actualTypeArguments[0];
            try {
                return tClass.newInstance();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //TODO 待优化
    public <S> Buffer getBuffer(S[] data) {
        return null;
    }

    /**
     * 获取顶点个数
     *
     * @return 顶点个数
     */
    public int getVertexCount() {
        return getVertexCoordinates().length / getCoordsCountPerVertex();
    }

    /**
     * 获取每个顶点的坐标个数
     * 每个顶点属性的组件数量。必须为1、2、3或者4。初始值为4。（如position是由3个（x,y,z）组成，而颜色是4个（r,g,b,a））,纹理坐标则是2维的
     *
     * @return 每个顶点坐标个数
     */
    public abstract int getCoordsCountPerVertex();

    /**
     * 获取每个顶点元素的颜色向量个数或者每个纹理坐标的个数
     * 每个顶点属性的组件数量。必须为1、2、3或者4。初始值为4。（如position是由3个（x,y,z）组成，而颜色是4个（r,g,b,a））,纹理坐标则是2维的
     *
     * @return 颜色维度数或者纹理坐标维度
     */
    public abstract int getVectorCountPerFragColorOrTexCoord();

    /**
     * 获取绘制顺序的索引数组
     *
     * @return 索引数组
     */
    public short[] getIndexData() {
        return new short[0];
    }

    /**
     * 获取顶点坐标属性名
     *
     * @return 顶点坐标属性名
     */
    public String getPositionAttrName() {
        return Constants.DEFAULT_POSITION_ATTR_NAME;
    }

    /**
     * 获取片元颜色属性名
     *
     * @return 片元颜色属性名
     */
    public String getColorAttrName() {
        return Constants.DEFAULT_COLOR_ATTR_NAME;
    }

    /**
     * 获取纹理坐标属性名
     *
     * @return 纹理坐标属性名
     */
    public String getTextureCoordAttrName() {
        return Constants.DEFAULT_TEXTURE_COORD_ATTR_NAME;
    }

    /**
     * 获取矩阵统一变量名
     *
     * @return 矩阵统一变量名
     */
    public String getMatrixUniformName() {
        return Constants.DEFAULT_MATRIX_UNIFORM_NAME;
    }

    /**
     * 是否有纹理坐标属性
     *
     * @return 默认false
     */
    public boolean haveTextureAttr() {
        return false;
    }

    /**
     * 是否有矩阵统一变量
     *
     * @return 默认false
     */
    public boolean haveMatrixUniform() {
        return false;
    }

    /**
     * 是否开启深度测试
     *
     * @return 默认false
     */
    public boolean needEnableDepthTest() {
        return false;
    }

    /**
     * 是否使用指定索引顺序绘制
     *
     * @return 默认false
     */
    public boolean drawWithSpecifiedIndex() {
        return false;
    }

    /**
     * 是否开启颜色混合
     * @return 默认false 不开启
     */
    public boolean enableColorBlend(){
        return false;
    }

}



