package org.jmock.function;

public interface Proc1<P1, X extends Throwable> {
    void call(P1 p1) throws X;
}
