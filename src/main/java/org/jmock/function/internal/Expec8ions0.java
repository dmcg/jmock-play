package org.jmock.function.internal;

import org.jmock.function.Func0;
import org.jmock.function.Func0MethodCapture;
import org.jmock.function.Proc0;
import org.jmock.function.Proc0MethodCapture;
import org.jmock.internal.Cardinality;

public class Expec8ions0 extends BaseExpec8ions {

    public <X extends Exception> Proc0MethodCapture.Proc0Will<X> allowing(Proc0<X> method) {
        return given(Cardinality.ALLOWING, method);
    }

    public <X extends Exception> Proc0MethodCapture.Proc0Will<X> once(Proc0<X> method) {
        return given(Cardinality.exactly(1), method);
    }

    public <X extends Exception> Proc0MethodCapture.Proc0Will<X> given(Cardinality times, Proc0<X> method) {
        return new Proc0MethodCapture<>(method, times, currentBuilder()).with();
    }

    public <R, X extends Exception> Func0MethodCapture.Func0Will<R, X> once(Func0<R, X> method) {
        return given(Cardinality.exactly(1), method);
    }

    public <R, X extends Exception> Func0MethodCapture.Func0Will<R, X> allowing(Func0<R, X> method) {
        return given(Cardinality.ALLOWING, method);
    }

    public <R, X extends Exception> Func0MethodCapture.Func0Will<R, X> given(Cardinality times, Func0<R, X> method) {
        return new Func0MethodCapture<>(method, times, currentBuilder()).with();
    }
}
