package org.jmock.function;

@FunctionalInterface
public interface FallibleCallable<X extends Throwable> {
    public void call() throws X;
}
