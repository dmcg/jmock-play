package org.jmock.function;

import org.hamcrest.Description;
import org.jmock.api.Action;
import org.jmock.api.Invocation;

public class Proc1Action<P1, X extends Exception> implements Action {
    private final Proc1<P1, X> proc;

    public Proc1Action(Proc1<P1, X> proc) {
        this.proc = proc;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Invocation invocation) throws Throwable {
        proc.call((P1) invocation.getParameter(0));
        return null;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("invoke a function");
    }
}
