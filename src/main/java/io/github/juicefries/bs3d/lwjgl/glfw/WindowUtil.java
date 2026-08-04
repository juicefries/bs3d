package io.github.juicefries.bs3d.lwjgl.glfw;

import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class WindowUtil {

    public static int[] getSize(long window) {
        if (window == MemoryUtil.NULL) {
            throw new IllegalArgumentException("The window handle cannot be 0!");
        }
        int[] size = new int[2];
        int[] width = new int[1];
        int[] height = new int[1];
        GLFW.glfwGetWindowSize(window,width,height);
        size[0] = width[0];
        size[1] = height[0];
        return size;
    }

    public static int getWidth(long window) {
        return WindowUtil.getSize(window)[0];
    }

    public static double getWindowSizeCenterX(long window) {
        return getWidth(window) / 2.0d;
    }

    public static double getWindowSizeCenterY(long window) {
        return getHeight(window) / 2.0d;
    }

    public static int getHeight(long window) {
        return WindowUtil.getSize(window)[1];
    }



    public static long[] getMonitors() {
        PointerBuffer monitors = GLFW.glfwGetMonitors();

        if (monitors != null) {
            long[] ms = new long[monitors.limit()];
            for (int i = 0; i < monitors.limit(); i++) {
                long m = monitors.get(i);
                ms[i] = m;
            }
            return ms;
        }
        return null;
    }

    public static GLFWVidMode getVideoMode(long monitor) {
        GLFWVidMode vidMode;
        if (monitor != MemoryUtil.NULL) {
            vidMode = GLFW.glfwGetVideoMode(monitor);
        } else {
            vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        }
        return vidMode;
    }

    public static void setWindowIcon(long window, byte[] icon) {
        if (window == MemoryUtil.NULL) {
            throw new RuntimeException("Invalid window handle!");
        }
        if (icon == null) {
            throw new NullPointerException("icon is null!");
        }

        // byte[] 转 ByteBuffer
        ByteBuffer inBuffer = BufferUtils.createByteBuffer(icon.length);
        inBuffer.put(icon).flip();

        // 解码图片 格式PNG/JPEG/BMP/TGA/GIF/PSD/HDR/PIC
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);

        ByteBuffer rgba = STBImage.stbi_load_from_memory(inBuffer, w, h, comp, 4);

        if (rgba == null) {
            throw new RuntimeException("解码失败: " + STBImage.stbi_failure_reason());
        }

        // 创建GLFW图标
        GLFWImage image = GLFWImage.malloc()
                .set(w.get(0), h.get(0), rgba);

        GLFWImage.Buffer images = GLFWImage.malloc(1)
                .put(0, image);

        // 设置窗口图标
        GLFW.glfwSetWindowIcon(window, images);

        // 清理内存
        image.free();
        images.free();
        STBImage.stbi_image_free(rgba);
    }

    // setWindowSizeLimits

    public static void setWindowSizeLimits(long window,int[] limits4) {
        if (limits4 == null) {
            throw new NullPointerException("limits4 is null!");
        }
        if (window == MemoryUtil.NULL) {
            throw new IllegalArgumentException("window of value is 0L!");
        }
        if (limits4.length < 4) {
            throw new IllegalArgumentException("Array length cannot be less than 4!");
        }
        GLFW.glfwSetWindowSizeLimits(window, limits4[0], limits4[1], limits4[2], limits4[3]);
    }

}
