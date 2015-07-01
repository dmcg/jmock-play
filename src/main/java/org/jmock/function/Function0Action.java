package org.jmock.function;

import org.hamcrest.Description;
import org.jmock.api.Action;
import org.jmock.api.Invocation;

public class Function0Action<R, X extends Throwable> implements Action {
    private final Function0<R, X> function;

    public Function0Action(Function0<R, X> function) {
        this.function = function;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Invocation invocation) throws Throwable {
        return function.apply();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("invoke a function");

    }
}
