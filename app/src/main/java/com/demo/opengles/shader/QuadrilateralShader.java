package com.demo.opengles.shader;

import android.opengl.GLES20;

import com.demo.opengles.MyApp;
import com.demo.opengles.utils.OpenGlUtils;

public class QuadrilateralShader implements Shader {
    private static final String VERTEX_SHADER = "default_vertex.glsl";
    private static final String FRAGMENT_SHADER = "default_fragment.glsl";

    @Override
    public int loadVertexShader() {
        return OpenGlUtils.loadShader(OpenGlUtils.getShaderString(MyApp.getInstance(), VERTEX_SHADER), GLES20.GL_VERTEX_SHADER);
    }

    @Override
    public int loadFragmentShader() {
        return OpenGlUtils.loadShader(OpenGlUtils.getShaderString(MyApp.getInstance(), FRAGMENT_SHADER), GLES20.GL_FRAGMENT_SHADER);
    }
}
