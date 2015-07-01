package org.jmock.function;

public interface Function3<P1, P2, P3, R, X extends Throwable> {
    R apply(P1 p1, P2 p2, P3 p3) throws X;
}
