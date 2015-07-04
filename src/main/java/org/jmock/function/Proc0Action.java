package org.jmock.function;

import org.hamcrest.Description;
import org.jmock.api.Action;
import org.jmock.api.Invocation;

public class Proc0Action<X extends Throwable> implements Action {
    private final Proc0<X> proc;

    public Proc0Action(Proc0<X> proc) {
        this.proc = proc;
    }

    @Override
    public Object invoke(Invocation invocation) throws Throwable {
        proc.call();
        return null;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("invoke a function");
    }
}
