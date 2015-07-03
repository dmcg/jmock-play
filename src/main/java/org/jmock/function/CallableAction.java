package org.jmock.function;

import org.hamcrest.Description;
import org.jmock.api.Action;
import org.jmock.api.Invocation;

public class CallableAction<X extends Throwable> implements Action {
    private final FallibleCallable<X> callable;

    public CallableAction(FallibleCallable<X> callable) {
        this.callable = callable;
    }

    @Override
    public Object invoke(Invocation invocation) throws Throwable {
        callable.call();
        return null;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("invoke a function");
    }
}
