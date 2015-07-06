package org.jmock.function;

@FunctionalInterface
public interface Proc0<X extends Exception> {
    void call() throws X;
}
