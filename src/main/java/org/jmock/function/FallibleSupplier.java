package org.jmock.function;

@FunctionalInterface
public interface FallibleSupplier<R, X extends Throwable> {
    public R get() throws X;
}
