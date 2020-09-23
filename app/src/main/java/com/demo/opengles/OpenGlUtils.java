package com.demo.opengles;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class OpenGlUtils {
    private static final String TAG = "OpenGlUtils";

    public static int loadProgram(String vertexShader, String fragmentShader) {
        int iVShader;
        int iFShader;
        int iProgId;
        int[] link = new int[1];
        //获取编译后的顶点着色器句柄
        iVShader = loadShader(vertexShader, GLES20.GL_VERTEX_SHADER);
        if (iVShader == 0) {
            Log.e(TAG, "load program,Vertex Shader Failed");
            return 0;
        }
        //获取编译后的片元着色器句柄
        iFShader = loadShader(fragmentShader, GLES20.GL_FRAGMENT_SHADER);
        if (iFShader == 0) {
            Log.e(TAG, "load program,Fragment Shader Failed");
            return 0;
        }
        //创建一个Program
        iProgId = GLES20.glCreateProgram();
        //添加顶点着色器与片元着色器到program
        GLES20.glAttachShader(iProgId, iVShader);
        GLES20.glAttachShader(iProgId, iFShader);
        //链接生成可执行的Program
        GLES20.glLinkProgram(iProgId);
        //获取Program句柄，并存在在link数组容器中
        GLES20.glGetProgramiv(iProgId, GLES20.GL_LINK_STATUS, link, 0);
        //容错
        if (link[0] <= 0) {
            Log.e(TAG, "load program,Linking failed");
            return 0;
        }
        //删除已链接后的着色器
        GLES20.glDeleteShader(iVShader);
        GLES20.glDeleteShader(iFShader);
        return iProgId;
    }

    public static int loadShader(final String strSource, final int iType) {
        int[] compiled = new int[1];
        //创建指定类型的着色器
        int iShader = GLES20.glCreateShader(iType);
        //将源码添加到iShader并编译它
        GLES20.glShaderSource(iShader, strSource);
        GLES20.glCompileShader(iShader);
        //获取编译后的着色器句柄存在compiled 数组容器中
        GLES20.glGetShaderiv(iShader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        //容错判断
        if (compiled[0] == 0) {
            Log.e(TAG, "Load Shader failed, Compilation " + GLES20.glGetShaderInfoLog(iShader));
            return 0;
        }
        return iShader;
    }

    public static FloatBuffer getFloatBuffer(float[] data) {
        //初始化（顶点）字节缓冲区，用于存放（三角形顶点）数据
        ByteBuffer bb = ByteBuffer.allocateDirect(
                //每个浮点数占用4个字节
                data.length * 4
        );
        //设置使用设备硬件的原生字节序
        bb.order(ByteOrder.nativeOrder());
        //从ByteBuffer中创建一个浮点缓冲区
        FloatBuffer floatBuffer = bb.asFloatBuffer();
        //把所有坐标都添加到FloatBuffer中
        floatBuffer.put(data);
        //设置buffer从第一个位置开始读
        //因为每次调用put加入数据后position都会加1，因此要将position重置为0
        floatBuffer.position(0);
        return floatBuffer;
    }


    public static String getShaderString(Context context, String fileName) {
        InputStream inputStream = null;
        StringBuilder result = new StringBuilder();
        try {
            inputStream = context.getAssets().open(fileName);
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = br.readLine()) != null) {
                result.append(line).append("\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toString();
    }
}
