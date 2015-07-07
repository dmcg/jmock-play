package org.jmock.function;

@FunctionalInterface
public interface Proc1<P1, X extends Exception> {

    void call(P1 p1) throws X;

}
