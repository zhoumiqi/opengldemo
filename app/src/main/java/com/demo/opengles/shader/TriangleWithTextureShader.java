package com.demo.opengles.shader;

import android.opengl.GLES20;

import com.demo.opengles.MyApp;
import com.demo.opengles.utils.Constants;
import com.demo.opengles.utils.OpenGlUtils;

public class TriangleWithTextureShader implements Shader {
    @Override
    public int loadVertexShader() {
        return OpenGlUtils.loadShader(OpenGlUtils.getShaderString(MyApp.getInstance(), "triangle_vertex_texture.glsl"), GLES20.GL_VERTEX_SHADER);
    }

    @Override
    public int loadFragmentShader() {
        return OpenGlUtils.loadShader(OpenGlUtils.getShaderString(MyApp.getInstance(), "triangle_fragment_texture.glsl"), GLES20.GL_FRAGMENT_SHADER);
    }
}
