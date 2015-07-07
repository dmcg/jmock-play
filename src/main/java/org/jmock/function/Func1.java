package org.jmock.function;

import org.hamcrest.Description;
import org.jmock.api.Action;
import org.jmock.api.Invocation;

@FunctionalInterface
public interface Func1<P1, R, X extends Exception> {

    R apply(P1 p1) throws X;

    default public Action asAction() {
        return new Action() {

            @SuppressWarnings("unchecked")
            @Override
            public R invoke(Invocation invocation) throws X {
                return Func1.this.apply((P1) invocation.getParameter(0));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("invoke a function");
            }
        };
    }
}
