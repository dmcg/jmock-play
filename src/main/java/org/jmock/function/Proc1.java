package org.jmock.function;

import org.hamcrest.Description;
import org.jmock.api.Action;
import org.jmock.api.Invocation;

@FunctionalInterface
public interface Proc1<P1, X extends Exception> {

    void call(P1 p1) throws X;

    default public Action asAction() {
        return new Action() {

            @SuppressWarnings("unchecked")
            @Override
            public Object invoke(Invocation invocation) throws X {
                Proc1.this.call((P1) invocation.getParameter(0));
                return null;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("invoke a procedure");
            }
        };
    }


    @SuppressWarnings("unchecked")
    default public void callWithArgs(Object... args) throws X {
        this.call((P1) args[0]);
    }
}
