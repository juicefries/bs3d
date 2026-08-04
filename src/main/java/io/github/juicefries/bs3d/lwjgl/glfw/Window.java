//
// Created by juicefries
// The project name is bs3d
// Data 2026/08/04 03:09
//

package io.github.juicefries.bs3d.lwjgl.glfw;

import io.github.juicefries.bs3d.Array;
import io.github.juicefries.bs3d.Lock;
import io.github.juicefries.bs3d.Resources;
import io.github.juicefries.bs3d.Settings;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

public class Window {

    private final static Lock LOCK = Lock.create();
    private final static AtomicBoolean GLFW_INIT = new AtomicBoolean(false);

    private long window = MemoryUtil.NULL;
    private Settings settings;
    private final AtomicBoolean build = new AtomicBoolean(false);
    private final Lock lock = Lock.create();

    public Window(Settings settings) {
        setSettings(settings);
    }

    public Window() {
        this(null);
    }


    // ========================= OTM =========================

    public void build(boolean bind) {
        if (build.get()) return;
        synchronized (lock) {
            this.settings = Settings.checkAndBuild(this.settings);
            final int width = settings.getWidth();
            final int height = settings.getHeight();
            final String title = settings.getTitle();
            final long monitor = settings.getMonitor();
            final long share = settings.getShare();
            window = GLFW.glfwCreateWindow(width, height, title, monitor, share);
            if (window == MemoryUtil.NULL) {
                throw new IllegalArgumentException("Failed to create window!");
            }
            if (bind) bind();
            build.set(true);
        }
    }

    public void build() {
        build(false);
    }

    public void unbind() {
        GLFW.glfwMakeContextCurrent(MemoryUtil.NULL);
    }

    public void bind() {
        synchronized (lock) {
            GLFW.glfwMakeContextCurrent(window);
            GL.createCapabilities();
        }
    }

    public void pollEvents(boolean check) {
        if (!check()) return;
        if (!check) return;
        synchronized (lock) {
            GLFW.glfwPollEvents();
        }
    }

    public void waitEvents(boolean check) {
        if (!check()) return;
        if (!check) return;
        synchronized (lock) {
            GLFW.glfwWaitEvents();
        }
    }

    public void swapBuffers(boolean check) {
        if (!check()) return;
        if (!check) return;
        synchronized (lock) {
            GLFW.glfwSwapBuffers(window);
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean check() {
        return Window.check(this);
    }

    public void setWindowSizeCallback(GLFWWindowSizeCallbackI callbackI) {
        if (callbackI == null) return;
        if (!check()) return;
        GLFW.glfwSetWindowSizeCallback(window,callbackI);
    }

    public void cleanup(boolean destroy) {
        if (!build.get()) return;

        synchronized (lock) {
            if (destroy) {
                Window.checkGlfwInit(true);
                if (!check()) return;
                GLFW.glfwDestroyWindow(window);
            }
            window = 0;
            build.set(false);
        }
    }

    public void cleanup() {
        cleanup(false);
    }

    public void hide() {
        setVisible(false);
    }

    public void show() {
        setVisible(true);
    }

    // ========================= SET =========================

    public void setTitle(String title) {
        if (!build.get()) return;

        synchronized (lock) {
            setTitle(window, Objects.requireNonNullElse(title, ""));
        }
    }

    public void setSize(int width,int height) {
        if (!build.get()) return;

        if (width < 0) {
            throw new IllegalArgumentException("Width cannot be less than 0!");
        }
        if (height < 0) {
            throw new IllegalArgumentException("Height cannot be less than 0!");
        }
        if (!check()) return;
        Vector2i size = getSize();
        if (size.equals(width,height)) { // 如果大小相同就不设置了
            return;
        }
        synchronized (lock) {
            GLFW.glfwSetWindowSize(window,width,height);
        }
    }

    public void setSize(Vector2i size) {
        if (!build.get()) return;

        if (size == null) {
            throw new NullPointerException("size is null!");
        }
        setSize(size.x,size.y);
    }

    public void setSize(float ratio,boolean align) {
        if (!build.get()) return;
        Window.checkGlfwInit(true);
        if (!check()) return;
        this.settings = Settings.checkAndBuild(settings);
        GLFWVidMode vidMode = WindowUtil.getVideoMode(settings.getMonitor());
        if (vidMode == null) {
            return;
        }
        int width = (int)(vidMode.width() * ratio);
        int height = Array.match(align,width,(int)(vidMode.height() * ratio));

        setSize(width, height);
    }

    public void setSize(float ratio) {
        if (!build.get()) return;
        Window.checkGlfwInit(true);
        if (!check()) return;
        setSize(ratio,false);
    }

    public void setSize(float widthRatio,float heightRatio) {
        if (!build.get()) return;
        Window.checkGlfwInit(true);
        if (!check()) return;
        this.settings = Settings.checkAndBuild(settings);
        GLFWVidMode vidMode = WindowUtil.getVideoMode(settings.getMonitor());
        if (vidMode == null) {
            return;
        }
        int width = (int)(vidMode.width() * widthRatio);
        int height = (int)(vidMode.height() * heightRatio);

        setSize(width, height);
    }

    public void setPoint(int x,int y) {
        if (!build.get()) return;

        if (!check()) return;
        Vector2i point = getPoint();
        if (point.equals(x, y)) return; // 跳过
        synchronized (lock) {
            GLFW.glfwSetWindowPos(window,x,y);
        }
    }

    public void setPoint(Vector2i point) {
        if (!build.get()) return;
        if (!check()) return;

        if (point != null) {
            setPoint(point.x, point.y);
            return;
        }
        Window.checkGlfwInit(true);
        this.settings = Settings.checkAndBuild(settings);
        GLFWVidMode vidMode = WindowUtil.getVideoMode(settings.getMonitor());
        if (vidMode != null) {
            int x = (vidMode.width() - getWidth()) / 2;
            int y = (vidMode.height() - getHeight()) / 2;
            setPoint(x,y);
        }
    }

    public void setVisible(boolean visible) {
        if (!build.get()) return;

        if (!check()) return;
        boolean result = isVisible(); // 这里由isVisible抛出异常
        if (visible && result) return;
        if (!visible && !result) return;
        synchronized (lock) {
            if (visible) {
                GLFW.glfwShowWindow(window);
            } else {
                GLFW.glfwHideWindow(window);
            }
        }
    }

    public void setSettings(Settings settings) {
        this.settings = Settings.build(settings);
    }

    public void setIcon(GLFWImage.Buffer icon) {
        if (!build.get()) return;

        if (icon == null) {
            throw new NullPointerException("icon is null!");
        }
        if (!check()) return;
        GLFW.glfwSetWindowIcon(window,icon);
    }

    public void setIcon(byte[] icon) {
        if (!build.get()) return;

        if (icon == null) {
            throw new NullPointerException("icon is null!");
        }

        if (!check()) return;
        WindowUtil.setWindowIcon(window,icon);
    }

    public void setIcon(Class<?> clazz,String name) {
        if (!build.get()) return;

        if (clazz == null) {
            throw new NullPointerException("clazz is null!");
        }
        if (name == null) {
            throw new NullPointerException("name is null!");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name is empty!");
        }
        if (!check()) return;
        try(InputStream in = Resources.getResourceAsStream(clazz, name)) {
            if (in != null) {
                setIcon(in.readAllBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setIcon(String name) {
        if (!build.get()) return;

        setIcon(Window.class,name);
    }


    // ========================= GET =========================

    public Settings getSettings() {
        return settings;
    }

    public long getWindow() {
        return window;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isBuild() {
        return build.get();
    }

    public String getTitle() {
        if (!build.get()) return null;
        Window.checkGlfwInit(true);
        if (!check()) return null;
        return GLFW.glfwGetWindowTitle(window);
    }

    public boolean isVisible() {
        if (!build.get()) return false;
        Window.checkGlfwInit(true);
        if (!check()) return false;
        return GLFW.glfwGetWindowAttrib(window,GLFW.GLFW_VISIBLE) == GLFW.GLFW_TRUE;
    }

    public Vector2i getPoint() {
        if (!build.get()) return null;

        Window.checkGlfwInit(true);
        if (!check()) return null;
        return new Vector2i(getPoint(window));
    }

    public int getX() {
        if (!build.get()) return 0;
        if (!check()) return 0;
        return getX(window);
    }

    public int getY() {
        if (!build.get()) return 0;
        if (!check()) return 0;
        return getY(window);
    }

    public Vector2i getSize() {
        if (!build.get()) return null;
        Window.checkGlfwInit(true);
        if (!check()) return null;
        return new Vector2i(getSize(window));
    }

    public int getWidth() {
        if (!build.get()) return -1;
        if (!check()) return -1;
        return getWidth(window);
    }

    public int getHeight() {
        if (!build.get()) return -1;
        if (!check()) return -1;
        return getHeight(window);
    }

    // ========================= EEE =========================

    // ========================= S-OTM =========================

    public static void glfwInit() {
        if (isGlfwInit()) return;

        synchronized (Window.getLock()) {
            if (!GLFW.glfwInit()) {
                throw new IllegalStateException("GLFW initialization failed!");
            }
            GLFW_INIT.set(true);
        }
    }

    public static void glfwTerminate() {
        if (!isGlfwInit()) return;
        synchronized (Window.getLock()) {
            GLFW.glfwTerminate();
            GLFW_INIT.set(false);
        }
    }

    public static boolean checkGlfwInit(boolean init) {
        if (isGlfwInit()) return true;
        if (init) glfwInit();
        return false;
    }

    public static boolean checkGlfwInit() {
        return checkGlfwInit(false);
    }

    public static boolean checkWindow(long window) {
        checkGlfwInit(true);
        return window != MemoryUtil.NULL;
    }

    public static boolean checkWindow(Window window) {
        if (window == null || !window.isBuild()) {
            return false;
        }
        return checkWindow(window.window);
    }

    public static boolean checkCurrentContext() {
        checkGlfwInit(true);
        return GLFW.glfwGetCurrentContext() != MemoryUtil.NULL;
    }

    public static boolean checkContext(long window) {
        return GLFW.glfwGetCurrentContext() == window;
    }

    public static boolean check(long window) {
        return checkWindow(window) && checkCurrentContext() && checkContext(window);
    }

    public static boolean check(Window window) {
        if (window == null || !window.isBuild()) return false;
        return check(window.window);
    }

    // ========================= S-SET =========================

    public static void setGlfwInit(boolean init) {
        synchronized (Window.getLock()) {
            GLFW_INIT.set(init);
        }
    }

    public static void setTitle(long window,String title) {
        if (window == MemoryUtil.NULL) {
            throw new IllegalArgumentException("The window handle cannot be 0!");
        }
        if (title == null) {
            throw new NullPointerException("title is null!");
        }
        Window.checkGlfwInit(true);
        GLFW.glfwSetWindowTitle(window,title);
    }

    // ========================= S-GET =========================

    @Deprecated(since = "0.0.2",forRemoval = true)
    private static AtomicBoolean getGlfwInit() {
        return GLFW_INIT;
    }

    private static Lock getLock() {
        return Window.LOCK;
    }

    public static boolean isGlfwInit() {
        synchronized (getLock()) {
            return GLFW_INIT.get();
        }
    }

    public static int[] getSize(long window) {
        if (window == MemoryUtil.NULL) {
            throw new IllegalArgumentException("The window handle cannot be 0!");
        }
        Window.checkGlfwInit(true);
        int[][] size = new int[3][];
        size[0] = Array.createI(2);
        size[1] = Array.createI(1);
        size[2] = Array.createI(1);
        GLFW.glfwGetWindowSize(window,size[1],size[2]);
        size[0][0] = size[1][0];
        size[0][1] = size[2][0];
        return size[0];
    }

    public static int getWidth(long window) {
        return Window.getSize(window)[0];
    }

    public static int getHeight(long window) {
        return Window.getSize(window)[1];
    }

    public static int[] getPoint(long window) {
        if (window == MemoryUtil.NULL) {
            throw new IllegalArgumentException("The window handle cannot be 0!");
        }
        Window.checkGlfwInit(true);
        // 深夜脑抽写的
        int[][] point = new int[3][];
        if (!check(window)) return point[1];
        point[0] = Array.createI(2);
        point[1] = Array.createI(1);
        point[2] = Array.createI(1);
        GLFW.glfwGetWindowPos(window,point[1],point[2]);
        point[0][0] = point[1][0];
        point[0][1] = point[2][0];
        return point[0];
    }

    public static int getX(long window) {
        return getPoint(window)[0];
    }

    public static int getY(long window) {
        return getPoint(window)[1];
    }

    public static String getTitle(long window) {
        if (window == MemoryUtil.NULL) {
            throw new IllegalArgumentException("The window handle cannot be 0!");
        }
        Window.checkGlfwInit(true);
        return GLFW.glfwGetWindowTitle(window);
    }


}
