//
// Created by juicefries
// The project name is bs3d
// Data 2026/08/04 03:02
//

package io.github.juicefries.bs3d;

import java.util.Objects;

public final class Lock {

    // nano time id
    private final long nt;
    private final long ctm;
    private final long value;

    private Lock(long value) {
        this.value = value;
        this.nt = System.nanoTime();
        this.ctm = System.currentTimeMillis();
    }

    public long getNt() {
        return nt;
    }

    public long getCtm() {
        return ctm;
    }

    public long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Lock lock)) return false;
        return (nt == lock.nt) && (ctm == lock.ctm) && (value == lock.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nt, ctm,value);
    }

    @Override
    public String toString() {
        return getClass().getCanonicalName() + "[nti=" + nt + ",ctm=" + ctm + ",value=" + value + "]";
    }

    public static Lock create(long value) {
        return new Lock(value);
    }

    public static Lock create() {
        return create(System.nanoTime());
    }

}

