package org.jmock.function;

public interface Function0<R, X extends Throwable> {
    R apply() throws X;
}
