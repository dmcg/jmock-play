package org.jmock.function;

public interface Function2<P1, P2, R, X extends Throwable> {
    R apply(P1 p1, P2 p2) throws X;
}
