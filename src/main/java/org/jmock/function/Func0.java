package org.jmock.function;

@FunctionalInterface
public interface Func0<R, X extends Exception> {
    R apply() throws X;
}
