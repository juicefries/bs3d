//
// Created by juicefries
// The project name is bs3d
// Data 2026/08/04 19:18
//

package io.github.juicefries.bs3d;

import java.util.List;
import java.util.Objects;

// 深夜脑抽想出来的 第二天写的
public class Array {

    public static <T> T match(boolean condition,T a,T b) {
        if (condition) {
            return a;
        } else {
            return b;
        }
    }

    public static Object[] create(int length) {
        if (length < 0) {
            return new Object[0];
        }
        return new Object[length];
    }

    public static int[] createI(int length) {
        if (length < 0) {
            return new int[0];
        }
        return new int[length];
    }

    public static long[] createL(int length) {
        if (length < 0) {
            return new long[0];
        }
        return new long[length];
    }

    public static double[] createD(int length) {
        if (length < 0) {
            return new double[0];
        }
        return new double[length];
    }

    public static float[] createF(int length) {
        if (length < 0) {
            return new float[0];
        }
        return new float[length];
    }

    @SafeVarargs
    public static <T> boolean contains(T value, T... values) {
        if (values == null) {
            throw new NullPointerException("values is null!");
        }
        if (values.length < 1) {
            throw new IllegalArgumentException("The length of values cannot be less than 1!");
        }
        for (T t : values) {
            if (Objects.equals(value, t)) return true;
        }
        return false;
    }

    @SafeVarargs
    public static <T> boolean notIncluded(T value, T... values) {
        return !contains(value, values);
    }

    public static void isNotEmpty(List<?> list, Runnable runnable) {
        if (list == null) {
            throw new NullPointerException("list is null!");
        }
        if (runnable == null) {
            throw new NullPointerException("runnable is null!");
        }
        if (!list.isEmpty()) {
            runnable.run();
        }
    }
}

