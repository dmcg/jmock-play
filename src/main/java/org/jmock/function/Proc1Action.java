package org.jmock.function;

import org.hamcrest.Description;
import org.jmock.api.Action;
import org.jmock.api.Invocation;

public class Proc1Action<P1, X extends Throwable> implements Action {
    private final Proc1<P1, X> function;

    public Proc1Action(Proc1<P1, X> function) {
        this.function = function;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Invocation invocation) throws Throwable {
        function.apply((P1) invocation.getParameter(0));
        return null;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("invoke a function");
    }
}