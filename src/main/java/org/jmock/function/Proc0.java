package org.jmock.function;

public interface Proc0<X extends Throwable> {
    void apply() throws X;
}
