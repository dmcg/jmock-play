package org.jmock.function;

public interface Proc1<P1, X extends Throwable> {
    void apply(P1 p1) throws X;
}
