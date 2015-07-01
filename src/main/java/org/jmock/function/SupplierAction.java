package org.jmock.function;

import org.hamcrest.Description;
import org.jmock.api.Action;
import org.jmock.api.Invocation;

public class SupplierAction<R, X extends Throwable> implements Action {
    private final FallibleSupplier<R, X> supplier;

    public SupplierAction(FallibleSupplier<R, X> supplier) {
        this.supplier = supplier;
    }

    @Override
    public R invoke(Invocation invocation) throws X {
        return supplier.get();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("invoke a supplier");
    }
}
