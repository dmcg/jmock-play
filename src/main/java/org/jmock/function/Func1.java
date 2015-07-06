package org.jmock.function;

public interface Func1<P1, R, X extends Exception> {
    R apply(P1 p1) throws X;
}
