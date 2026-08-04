//
// Created by juicefries
// The project name is bs3d
// Data 2026/08/04 03:21
//

package io.github.juicefries.bs3d;

import java.util.Objects;
import org.lwjgl.system.MemoryUtil;

public class Settings implements Cloneable {

    private String title = "window";
    private int width = 800;
    private int height = 600;
    private long monitor = MemoryUtil.NULL;
    private long share = MemoryUtil.NULL;

    public Settings(Settings settings) {
        if (settings == null) {
            return;
        }
        setTitle(settings.title);
        setWidth(settings.width);
        setHeight(settings.height);
        setMonitor(settings.monitor);
        setShare(settings.share);
    }

    public Settings() {
        this(null);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setMonitor(long monitor) {
        this.monitor = monitor;
    }

    public void setShare(long share) {
        this.share = share;
    }

    public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getMonitor() {
        return monitor;
    }

    public long getShare() {
        return share;
    }


    public static Settings build(Settings settings) {
        return new Settings(settings);
    }

    public static Settings build() {
        return build(null);
    }

    public static Settings checkAndBuild(Settings settings) {
        return Objects.requireNonNullElseGet(settings, Settings::build);
    }

    @Override
    public Object clone() {
        try {
            Settings settings = (Settings) super.clone();
            settings.setTitle(title);
            settings.setWidth(width);
            settings.setHeight(height);
            settings.setMonitor(monitor);
            settings.setShare(share);
            return settings;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Settings settings)) return false;
        return getWidth() == settings.getWidth() && getHeight() == settings.getHeight() && getMonitor() == settings.getMonitor() && getShare() == settings.getShare() && Objects.equals(getTitle(), settings.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getWidth(), getHeight(), getMonitor(), getShare());
    }

    @Override
    public String toString() {
        return getClass().getCanonicalName() + "[" +
                "title='" + title + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", monitor=" + monitor +
                ", share=" + share +
                ']';
    }
}
