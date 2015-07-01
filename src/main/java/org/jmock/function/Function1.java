package org.jmock.function;

public interface Function1<P1, R, X extends Throwable> {
    R apply(P1 p1) throws X;
}
