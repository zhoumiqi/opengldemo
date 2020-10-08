package com.demo.opengles.shader;

import android.opengl.GLES20;

import com.demo.opengles.MyApp;
import com.demo.opengles.utils.OpenGlUtils;

public class CubeShader implements Shader {

    @Override
    public int loadVertexShader() {
        return OpenGlUtils.loadShader(OpenGlUtils.getShaderString(MyApp.getInstance(), "cube_vertex.glsl"), GLES20.GL_VERTEX_SHADER);
    }

    @Override
    public int loadFragmentShader() {
        return OpenGlUtils.loadShader(OpenGlUtils.getShaderString(MyApp.getInstance(), "cube_fragment.glsl"), GLES20.GL_FRAGMENT_SHADER);
    }
}
