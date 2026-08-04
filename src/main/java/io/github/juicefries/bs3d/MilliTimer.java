package io.github.juicefries.bs3d;

public class MilliTimer extends Timer {

    private static final long TIMER_RESOLUTION = 1000L;
    private static final float INVERSE_RESOLUTION = 1f / 1000f;

    private long startTime;
    private long previousTime;
    private float tpf;
    private float fps;

    public MilliTimer() {
        startTime = System.currentTimeMillis();
        previousTime = getTime();
    }

    @Override
    public long getTime() {
        return System.currentTimeMillis() - startTime;
    }

    @Override
    public long getResolution() {
        return TIMER_RESOLUTION;
    }

    @Override
    public float getFrameRate() {
        return fps;
    }

    @Override
    public float getTimePerFrame() {
        return tpf;
    }

    @Override
    public void update() {
        long now = getTime();
        tpf = (now - previousTime) * INVERSE_RESOLUTION;
        if (tpf > 0) fps = 1f / tpf;
        previousTime = now;
    }

    @Override
    public void reset() {
        startTime = System.currentTimeMillis();
        previousTime = getTime();
    }
}