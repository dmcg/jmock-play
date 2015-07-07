package org.jmock.function;

import org.hamcrest.Description;
import org.jmock.api.Action;
import org.jmock.api.Invocation;

@FunctionalInterface
public interface Func0<R, X extends Exception> {

    R apply() throws X;

    default public Action asAction() {
        return new Action() {

            @Override
            public R invoke(Invocation invocation) throws X {
                return Func0.this.apply();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("invoke a supplier");
            }
        };
    }
}
