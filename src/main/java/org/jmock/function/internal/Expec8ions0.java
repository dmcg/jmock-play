package org.jmock.function.internal;

import org.jmock.function.Func0;
import org.jmock.function.Func0MethodCapture;
import org.jmock.function.Proc0;
import org.jmock.function.Proc0MethodCapture;
import org.jmock.internal.Cardinality;
import org.jmock.internal.InvocationExpectationBuilder;

public class Expec8ions0 extends BaseExpec8ions {

    public <T> T callTo(T mock) {
        myCopyOfCurrentBuilder = (InvocationExpectationBuilder) exactly(1); // cardinality will be changed later, but this gives us access to currentBuilder
        return myCopyOfCurrentBuilder.of(mock);
    }

    public <X extends Exception> Proc0MethodCapture.Proc0Will<X> allowing(Proc0<X> method) {
        return given(Cardinality.ALLOWING, method);
    }

    public <X extends Exception> Proc0MethodCapture.Proc0Will<X> given(Cardinality times, Proc0<X> method) {
        return new Proc0MethodCapture<>(method, times, myCopyOfCurrentBuilder).with();
    }

    public <R, X extends Exception> Func0MethodCapture.Func0Will<R, X> allowing(Func0<R, X> method) {
        return given(Cardinality.ALLOWING, method);
    }

    public <R, X extends Exception> Func0MethodCapture.Func0Will<R, X> given(Cardinality times, Func0<R, X> method) {
        return new Func0MethodCapture<>(method, times, myCopyOfCurrentBuilder).with();
    }
}
