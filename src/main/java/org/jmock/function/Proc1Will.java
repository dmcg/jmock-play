package org.jmock.function;

import org.jmock.internal.InvocationExpectationBuilder;

public class Proc1Will<P1, X extends Throwable> extends BaseWill {

    protected Proc1Will(InvocationExpectationBuilder currentBuilder) {
        super(currentBuilder);
    }

    public void will(FallibleCallable<X> callable) {
        will(new CallableAction<>(callable));
    }

    public void will(Proc1<P1, X> f) {
        will(new Proc1Action<>(f));
    }
}
