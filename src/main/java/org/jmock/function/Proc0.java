package org.jmock.function;

@FunctionalInterface
public interface Proc0<X extends Throwable> {
    void call() throws X;
}
