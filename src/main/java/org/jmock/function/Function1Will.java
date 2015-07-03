package org.jmock.function;

import org.jmock.internal.InvocationExpectationBuilder;

public class Function1Will<P1, R, X extends Throwable> extends BaseWill {

    protected Function1Will(InvocationExpectationBuilder currentBuilder) {
        super(currentBuilder);
    }

    public void will(FallibleSupplier<R, X> supplier) {
        will(new SupplierAction<>(supplier));
    }

    public void will(Function1<P1, R, X> f) {
        will(new Function1Action<>(f));
    }
}
