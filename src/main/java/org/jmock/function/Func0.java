package org.jmock.function;

@FunctionalInterface
public interface Func0<R, X extends Throwable> {
    R apply() throws X;
}
