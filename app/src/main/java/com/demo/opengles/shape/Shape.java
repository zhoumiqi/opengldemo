package com.demo.opengles.shape;

import com.demo.opengles.shader.Shader;

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
}



