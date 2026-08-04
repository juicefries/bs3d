//
// Created by juicefries
// The project name is bs3d
// Data 2026/08/03 04:19
//

package io.github.juicefries.bs3d;

import io.github.juicefries.bs3d.lwjgl.glfw.Window;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL33;

// As you can see, I still don't know certain things.
public class Start {
    /*
     * TODO 0.0.2 I wrote a bit, but I still don't know what to write.
     */

    public final static double INTERVAL_NONE = 0.000000d;
    public final static double INTERVAL_20   = 0.050000d;
    public final static double INTERVAL_30   = 0.033333d;
    public final static double INTERVAL_40   = 0.025000d;
    public final static double INTERVAL_60   = 0.016667d;
    public final static double INTERVAL_90   = 0.011111d;
    public final static double INTERVAL_120  = 0.008333d;

    private final Lock lock = Lock.create();

    private Window window;
    private final AtomicBoolean END = new AtomicBoolean(false);
    private final AtomicBoolean renderReady = new AtomicBoolean(false);
    private final AtomicBoolean pause = new AtomicBoolean(false);
    private double logicUpdateInterval = INTERVAL_60;
    private double submissionScreenInterval = INTERVAL_20;
    private float speed = 1.0f;
    private final List<Runnable> invokes = new ArrayList<>();
    private final List<Runnable> waitingInvokes = new ArrayList<>();
    private final List<Runnable> screens = new ArrayList<>();
    private final List<Runnable> waitings = new ArrayList<>();
    private final Timer nanoTimer = new NanoTimer();
    private double updateTimer = 0.0d;
    private double submissionTimer = 0.0d;


    public void init() {
        if (!Window.isGlfwInit()) Window.glfwInit();
        window = new Window();
        new Thread(() -> {
            Hint.reset();
            Hint.visible(false).apply();
            Hint.contextVersionMajor(3).apply();
            Hint.contextVersionMinor(3).apply();
            Hint.openglProfile(GLFW.GLFW_OPENGL_CORE_PROFILE).apply();
            Hint.openglForwardCompat(true).apply();
            window.build(true);
            GLFW.glfwSetFramebufferSizeCallback(window.getWindow(),(_, width, height) -> {
                GL33.glViewport(0,0,width,height);
            });
            GLFW.glfwSetWindowCloseCallback(window.getWindow(), _ -> {
                renderReady.set(false);
                END.set(true);
                pause.set(true);
                speed = 0.0f;
                window.cleanup(true);
                invokes.clear();
                waitingInvokes.clear();
                screens.clear();
                waitings.clear();
                if (Window.isGlfwInit()) Window.glfwTerminate();
            });
            window.setIcon(Start.class,"icon.png");
            window.setSize(0.45f,0.55f);
            window.setPoint(null);
            window.show();
            renderLoop();
        },"Render").start();
        new Thread(this::logicLoop,"Logic").start();
    }


    public void renderLoop() {
        while (run()) {

            executeAndAddTask();

            window.swapBuffers(run());
            window.pollEvents(run());
            synchronized (lock) {
                if (end()) break;
            }
        }
    }

    public void executeInvokes() {
        if (invokes.isEmpty()) return;
        for (Runnable task : invokes) {
            synchronized (lock) {
                if (end()) break;
            }
            if (task == null) continue;
            task.run();
        }
        invokes.clear();
    }

    public void executeScreens() {
        if (screens.isEmpty()) return;
        for (Runnable task : screens) {
            synchronized (lock) {
                if (end()) break;
            }
            if (task == null) continue;
            task.run();
        }
    }

    public void addTask() {
        synchronized (lock) {
            if (end()) return;
        }
        if (!waitingInvokes.isEmpty()) {
            synchronized (lock) {
                invokes.addAll(waitingInvokes);
                waitingInvokes.clear();
            }
        }
        if (waitings.isEmpty()) return;
        synchronized (lock) {
            screens.clear();
            screens.addAll(waitings);
            waitings.clear();
        }
    }

    public void executeAndAddTask() {
        executeInvokes();
        executeScreens();
        addTask();
    }

    public void logicLoop() {
        while (run()) {
            if (!renderReady.get()) continue;
            if (pause.get() || speed == 0.0f) continue;
            nanoTimer.update();
            float tpf = nanoTimer.getTimePerFrame() * speed;
            if (logicUpdateInterval <= INTERVAL_NONE) {
                update(tpf);
            } else {
                updateTimer += tpf;
                if (updateTimer >= logicUpdateInterval) {
                    updateTimer -= logicUpdateInterval;
                    update(tpf);
                }
            }

            if (submissionTimer <= INTERVAL_NONE) {
                screen(tpf);
            } else {
                submissionTimer += tpf;
                if (submissionTimer >= submissionScreenInterval) {
                    submissionTimer -= submissionScreenInterval;
                    screen(tpf);
                }
            }
        }
    }

    public void update(float tpf) {

    }

    public void screen(float tpf) {

    }

    public void invoke(Runnable invoke) {
        if (invoke == null) {
            return;
        }
        synchronized (lock) {
            waitingInvokes.add(invoke);
        }
    }

    public void screen(Runnable screen) {
        if (screen == null) {
            return;
        }
        synchronized (lock) {
            waitings.add(screen);
        }
    }

    public boolean end() {
        return END.get();
    }

    public boolean run() {
        return !end();
    }

    private Start() {}

    public static Start build() {
        return new Start();
    }

}
