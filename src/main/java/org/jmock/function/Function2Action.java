package org.jmock.function;

import org.hamcrest.Description;
import org.jmock.api.Action;
import org.jmock.api.Invocation;

public class Function2Action<P1, P2, R, X extends Throwable> implements Action {
    private final Function2<P1,  P2,  R, X> function;

    public Function2Action(Function2<P1, P2, R, X> function) {
        this.function = function;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Invocation invocation) throws Throwable {
        return function.apply((P1) invocation.getParameter(0), (P2) invocation.getParameter(1));
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("invoke a function");

    }
}
