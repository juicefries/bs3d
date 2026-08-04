//
// Created by juicefries
// The project name is fwt
// Data 2026/07/30 02:27
//

package io.github.juicefries.bs3d;

public class LockNanoTimer extends NanoTimer{

    private final Lock lock = Lock.create();

    @Override
    public void update() {
        synchronized (lock) {
            super.update();
        }
    }

    @Override
    public void reset() {
        synchronized (lock) {
            super.reset();
        }
    }

    public LockNanoTimer copy() {
        LockNanoTimer timer = new LockNanoTimer();
        synchronized (lock) {
            timer.startTime = startTime;
            timer.fps = fps;
            timer.previousTime = previousTime;
            timer.tpf = tpf;
        }
        return timer;
    }
}
