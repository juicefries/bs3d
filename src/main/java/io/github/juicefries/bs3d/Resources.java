//
// Created by juicefries
// The project name is bs3d
// Data 2026/08/04 19:29
//

package io.github.juicefries.bs3d;


import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Resources {

    private final static Logger log = Logger.getLogger(Resources.class.getCanonicalName());

    public static InputStream getResourceAsStream(Class<?> clazz, String name) {
        if (clazz == null) {
            throw new NullPointerException("clazz is null!");
        }
        if (name == null) {
            throw new NullPointerException("name is null!");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("The string is empty!");
        }

        try {
            return clazz.getResourceAsStream(name);
        } catch (Exception e) {
            log.log(Level.WARNING,"Failed to load from clazz.", e);
        }

        try {
            //noinspection DataFlowIssue
            return clazz.getResource(name).openStream();
        } catch (IOException e) {
            log.log(Level.WARNING,"Failed to load from getResource.", e);
        }

        try {
            return clazz.getClassLoader().getResourceAsStream(name);
        } catch (Exception e) {
            log.log(Level.WARNING,"Failed to load from ClassLoader.", e);
        }

        try {
            return clazz.getModule().getResourceAsStream(name);
        } catch (IOException e) {
            log.log(Level.WARNING,"Failed to load from getModule.", e);
        }

        log.log(Level.WARNING,"Resource loading failed!");
        return null;
    }


    private Resources() {
    }

}
