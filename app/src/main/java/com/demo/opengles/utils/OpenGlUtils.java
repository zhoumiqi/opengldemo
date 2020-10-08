package com.demo.opengles.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import androidx.annotation.DrawableRes;

import com.demo.opengles.MyApp;
import com.demo.opengles.shape.Shape;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class OpenGlUtils {
    private static final String TAG = "OpenGlUtils";

    /**
     * 加载程序
     *
     * @param shape 要绘制的形状
     * @return 程序句柄
     */
    public static int loadProgram(Shape<?> shape) {
        if (shape == null) {
            throw new IllegalArgumentException("param shape is null");
        }
        int vertexShaderId;
        int fragmentShaderId;
        int programId;
        int[] link = new int[1];
        //获取编译后的顶点着色器句柄
        vertexShaderId = shape.getVertexId();
        if (vertexShaderId == 0) {
            Log.e(TAG, "load program,vertex shader failed");
            return 0;
        }
        //获取编译后的片元着色器句柄
        fragmentShaderId = shape.getFragmentId();
        if (fragmentShaderId == 0) {
            Log.e(TAG, "load program,fragment shader failed");
            return 0;
        }
        //创建一个Program
        programId = GLES20.glCreateProgram();
        //添加顶点着色器与片元着色器到program
        GLES20.glAttachShader(programId, vertexShaderId);
        GLES20.glAttachShader(programId, fragmentShaderId);
        //链接生成可执行的Program
        GLES20.glLinkProgram(programId);
        //获取Program句柄，并存在在link数组容器中
        GLES20.glGetProgramiv(programId, GLES20.GL_LINK_STATUS, link, 0);
        //容错
        if (link[0] <= 0) {
            Log.e(TAG, "load program,linking failed");
            return 0;
        }
        //删除已链接后的着色器
        GLES20.glDeleteShader(vertexShaderId);
        GLES20.glDeleteShader(fragmentShaderId);
        return programId;
    }

    /**
     * 加载着色器
     *
     * @param shaderSourceString 着色器源码字符串
     * @param shaderType         着色器类型
     * @return 着色器ID
     */
    public static int loadShader(final String shaderSourceString, final int shaderType) {
        int[] compiled = new int[1];
        //创建指定类型的着色器
        int shaderId = GLES20.glCreateShader(shaderType);
        //将源码添加到iShader并编译它
        GLES20.glShaderSource(shaderId, shaderSourceString);
        GLES20.glCompileShader(shaderId);
        //获取编译后的着色器句柄存在compiled 数组容器中
        GLES20.glGetShaderiv(shaderId, GLES20.GL_COMPILE_STATUS, compiled, 0);
        //容错判断
        if (compiled[0] == 0) {
            Log.e(TAG, "load shader failed, compilation " + GLES20.glGetShaderInfoLog(shaderId));
            return 0;
        }
        return shaderId;
    }

    /**
     * 从glsl文件中获取着色器源码字符串
     *
     * @param context  上下文
     * @param fileName glsl文件名字(含文件后缀的文件名)
     * @return 返回 着色器源码字符串
     */
    public static String getShaderString(Context context, String fileName) {
        InputStream inputStream = null;
        StringBuilder result = new StringBuilder();
        try {
            inputStream = context.getAssets().open(fileName);
            String line;
            //此处用byte[] 读取容易出错，读取最后一行为非法字符导致shader加载失败
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

    /**
     * 获取纹理id
     *
     * @return 纹理id
     */
    public static int getTextureId(@DrawableRes int resId) {
        int textureId = -1;
        int[] textures = new int[1];//生成纹理id
        //创建纹理对象
        GLES20.glGenTextures(
                1, //产生纹理id的数量
                textures, //纹理id的数组
                0);//偏移量
        textureId = textures[0];
        //绑定纹理id,将对象绑定到环境的纹理单元
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        //设置纹理采样方式<纹理采样就是根据片元的纹理坐标到纹理图中提取对应位置颜色的过程,但是由于被渲染图元中的片元数量与其对应纹理区域中像素的数量并不一定相同，也就是说图元中的片元与纹理图中的像素并不总是一一对应的。例如，将较小的纹理图映射到较大的图元或将较大的纹理图映射到较小的图元时，通过纹理坐标在纹理图中不一定能找到与之完全对应的像素，这个时候就必须采取一些策略>
        // 1、GL_NEAREST<最近点采样>:根据片元的纹理坐标在纹理图中的位置距离那个像素近就选择哪个像素的颜色值作为该片元的颜色采样值。最近点采样计算很简单，但存在的问题就是将较小的纹理图映射到较大的图元上容易产生明显的锯齿，同时将较大的纹理缩小的一定的程度后也就不好用了。
        // 2、GL_LINEAR<线性纹理采样>:线性纹理采样的结果并不仅来自纹理图中的一个像素，在进行纹理采样时会考虑到该片元对应的纹理坐标周围的几个像素，然后根据周围几个像素的比例进行加权得到最终的采样结果。由于线性采样对多个像素进行了加权平均，仅此将较小的图元纹理映射到较大的图元时，不会有锯齿现象，而是平滑过渡。
        // 因此一般来说对于缩小映射采用最近点采样，对于放大映射来说采用线性采样。
        //MIN与MAG采样
        //当一副尺寸较大的纹理图映射到一个显示到屏幕上尺寸比其小的图元时，系统将采用GLES20.GL_TEXTURE_MIN_FILTER 的采样方式。
        //当一副尺寸较小的纹理图映射到一个显示到屏幕上尺寸比其大的图元时，系统将采用GLES20.GL_TEXTURE_MAG_FILTER 的采样方式。
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);//设置MIN采样方式
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);//设置MAG采样方式
        //设置纹理拉伸方式
        // 1、GL_REPEAT<重复拉伸>:当顶点纹理坐标大于1时则实际起作用的纹理坐标为纹理坐标的小数部分，也就是若纹理坐标为3.3，则其作用的是0.3)
        // 2、GL_CLAMP_TO_EDGE<截取拉伸>:当纹理坐标的值大于1时都看做1，因此会产生边缘被拉伸的效果
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);//设置S轴拉伸方式
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);//设置T轴拉伸方式
        //加载图片
        Log.d(TAG, "decode bitmap,current thread:" + Thread.currentThread().getName());
        Bitmap bitmap = BitmapFactory.decodeResource(MyApp.getInstance().getResources(), resId);
        if (bitmap != null) {
            GLUtils.texImage2D(//实际加载纹理进显存
                    GLES20.GL_TEXTURE_2D,//纹理类型
                    0,//纹理层次，0表示基本图像层，可以理解为直接贴图
                    bitmap,//纹理图像
                    0);//纹理边框尺寸
            bitmap.recycle();
        }
        return textureId;
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

    public static ByteBuffer getByteBuffer(byte[] data) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(data.length).put(data);
        byteBuffer.position(0);
        return byteBuffer;
    }

    public static ShortBuffer getShortBuffer(short[] data) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(data.length * 2);
        byteBuffer.order(ByteOrder.nativeOrder());
        ShortBuffer indexBuffer = byteBuffer.asShortBuffer();
        indexBuffer.put(data);
        indexBuffer.position(0);
        return indexBuffer;
    }

    public static float getHalfScreen() {
        return (float) Math.min(getScreenWidth(), getScreenHeight()) / (float) 2;
    }

    public static int getScreenWidth() {
        return MyApp.getInstance().getResources().getConfiguration().screenWidthDp;
    }

    public static int getScreenHeight() {
        return MyApp.getInstance().getResources().getConfiguration().screenHeightDp;
    }

}
