package org.jmock.function;

import org.hamcrest.Description;
import org.jmock.api.Action;
import org.jmock.api.Invocation;

@FunctionalInterface
public interface Proc0<X extends Exception> {

    void call() throws X;

    default public Action asAction() {
        return new Action() {

            @Override
            public Void invoke(Invocation invocation) throws X {
                Proc0.this.call();
                return null;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("invoke a supplier");
            }
        };
    }

}
