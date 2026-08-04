//
// Created by juicefries
// The project name is bs3d
// Data 2026/08/04 19:42
//

package io.github.juicefries.bs3d;

import io.github.juicefries.bs3d.lwjgl.glfw.Window;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

/**
 * 提示类
 * <p>
 *     简单的提示封装类
 * </p>
 * @param hint 提示类型
 * @param value 提示的值
 * @see GLFW#glfwWindowHint(int, int)
 * @since 1.0.0
 * @version 1.0
 */
public record Hint(int hint, int value) implements Cloneable {

    /**
     * 复制提示的值
     * @param hint 提示
     * @throws NullPointerException 提示类不能为{@code null}
     */
    public Hint(final Hint hint) {
        if (hint == null) {
            throw new NullPointerException("hint is null!");
        }
        this(hint.hint, hint.value);
    }

    /**
     * 应用提示
     * @see GLFW#glfwWindowHint(int, int)
     */
    public void apply() {
        Window.checkGlfwInit(true);
        GLFW.glfwWindowHint(hint,value);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Hint(int hint2, int value1))) {
            return false;
        }
        return (hint == hint2) && (value == value1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hint, value);
    }

    @Override
    public String toString() {
        return "Hint{" + "hint=" + hint + ", value=" + value + '}';
    }

    public Object clone() {
        try {
            Hint hc = (Hint) super.clone();
            return create(hc);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public Hint cloned() {
        return create(hint,value);
    }

    // reset

    public static void reset() {
        GLFW.glfwDefaultWindowHints();
    }

    // create

    public static Hint create(int hint, int value) {
        return new Hint(hint, value);
    }

    public static Hint create(Hint hint) {
        return new Hint(hint);
    }

    // 窗口相关
    public static Hint resizable(boolean enabled) {
        return create(GLFW.GLFW_RESIZABLE, enabled ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }

    public static Hint visible(boolean enabled) {
        return create(GLFW.GLFW_VISIBLE, enabled ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }

    public static Hint decorated(boolean enabled) {
        return create(GLFW.GLFW_DECORATED, enabled ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }

    public static Hint focused(boolean enabled) {
        return create(GLFW.GLFW_FOCUSED, enabled ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }

    public static Hint autoIconify(boolean enabled) {
        return create(GLFW.GLFW_AUTO_ICONIFY, enabled ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }

    public static Hint floating(boolean enabled) {
        return create(GLFW.GLFW_FLOATING, enabled ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }

    public static Hint maximized(boolean enabled) {
        return create(GLFW.GLFW_MAXIMIZED, enabled ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }

    public static Hint centerCursor(boolean enabled) {
        return create(GLFW.GLFW_CENTER_CURSOR, enabled ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }

    public static Hint transparentFramebuffer(boolean enabled) {
        return create(GLFW.GLFW_TRANSPARENT_FRAMEBUFFER, enabled ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }

    public static Hint focusOnShow(boolean enabled) {
        return create(GLFW.GLFW_FOCUS_ON_SHOW, enabled ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }

    public static Hint scaleToMonitor(boolean enabled) {
        return create(GLFW.GLFW_SCALE_TO_MONITOR, enabled ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }

    // 帧缓冲位深
    public static Hint redBits(int bits) {
        return create(GLFW.GLFW_RED_BITS, bits);
    }

    public static Hint greenBits(int bits) {
        return create(GLFW.GLFW_GREEN_BITS, bits);
    }

    public static Hint blueBits(int bits) {
        return create(GLFW.GLFW_BLUE_BITS, bits);
    }

    public static Hint alphaBits(int bits) {
        return create(GLFW.GLFW_ALPHA_BITS, bits);
    }

    public static Hint depthBits(int bits) {
        return create(GLFW.GLFW_DEPTH_BITS, bits);
    }

    public static Hint stencilBits(int bits) {
        return create(GLFW.GLFW_STENCIL_BITS, bits);
    }

    public static Hint accumRedBits(int bits) {
        return create(GLFW.GLFW_ACCUM_RED_BITS, bits);
    }

    public static Hint accumGreenBits(int bits) {
        return create(GLFW.GLFW_ACCUM_GREEN_BITS, bits);
    }

    public static Hint accumBlueBits(int bits) {
        return create(GLFW.GLFW_ACCUM_BLUE_BITS, bits);
    }

    public static Hint accumAlphaBits(int bits) {
        return create(GLFW.GLFW_ACCUM_ALPHA_BITS, bits);
    }

    // 缓冲相关
    public static Hint auxBuffers(int num) {
        return create(GLFW.GLFW_AUX_BUFFERS, num);
    }

    public static Hint samples(int num) {
        return create(GLFW.GLFW_SAMPLES, num);
    }

    public static Hint refreshRate(int rate) {
        return create(GLFW.GLFW_REFRESH_RATE, rate);
    }

    public static Hint doubleBuffer(boolean enabled) {
        return create(GLFW.GLFW_DOUBLEBUFFER, enabled ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }

    public static Hint stereo(boolean enabled) {
        return create(GLFW.GLFW_STEREO, enabled ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }

    // OpenGL 上下文
    public static Hint clientApi(int api) {
        return create(GLFW.GLFW_CLIENT_API, api);
    }

    public static Hint contextVersionMajor(int major) {
        return create(GLFW.GLFW_CONTEXT_VERSION_MAJOR, major);
    }

    public static Hint contextVersionMinor(int minor) {
        return create(GLFW.GLFW_CONTEXT_VERSION_MINOR, minor);
    }

    public static Hint contextRobustness(int robustness) {
        return create(GLFW.GLFW_CONTEXT_ROBUSTNESS, robustness);
    }

    public static Hint contextReleaseBehavior(int behavior) {
        return create(GLFW.GLFW_CONTEXT_RELEASE_BEHAVIOR, behavior);
    }

    public static Hint openglForwardCompat(boolean enabled) {
        return create(GLFW.GLFW_OPENGL_FORWARD_COMPAT, enabled ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }

    public static Hint openglDebugContext(boolean enabled) {
        return create(GLFW.GLFW_OPENGL_DEBUG_CONTEXT, enabled ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }

    public static Hint openglProfile(int profile) {
        return create(GLFW.GLFW_OPENGL_PROFILE, profile);
    }

}
