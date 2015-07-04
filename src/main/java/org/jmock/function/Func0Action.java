package org.jmock.function;

import org.hamcrest.Description;
import org.jmock.api.Action;
import org.jmock.api.Invocation;

public class Func0Action<R, X extends Throwable> implements Action {
    private final Func0<R, X> function;

    public Func0Action(Func0<R, X> function) {
        this.function = function;
    }

    @Override
    public R invoke(Invocation invocation) throws X {
        return function.apply();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("invoke a supplier");
    }
}
