package org.jmock.function.internal;

import org.jmock.function.Func1;
import org.jmock.function.Func1MethodCapture;
import org.jmock.function.Proc1;
import org.jmock.function.Proc1MethodCapture;
import org.jmock.internal.Cardinality;

public class Expec8ions1 extends Expec8ions0 {

    public <P1, X extends Exception> Proc1MethodCapture<P1, X> allowing(Proc1<P1, X> method) {
        return given(Cardinality.ALLOWING, method);
    }

    public <P1, X extends Exception> Proc1MethodCapture<P1, X> once(Proc1<P1, X> method) {
        return given(Cardinality.exactly(1), method);
    }

    public <P1, X extends Exception> Proc1MethodCapture<P1, X> given(Cardinality times, Proc1<P1, X> method) {
        return remember(new Proc1MethodCapture<>(method, times, currentBuilder()));
    }

    public <P1, R, X extends Exception> Func1MethodCapture<P1, R, X> allowing(Func1<P1, R, X> method) {
        return given(Cardinality.ALLOWING, method);
    }

    public <P1, R, X extends Exception> Func1MethodCapture<P1, R, X> once(Func1<P1, R, X> method) {
        return given(Cardinality.exactly(1), method);
    }

    public <P1, R, X extends Exception> Func1MethodCapture<P1, R, X> given(Cardinality times, Func1<P1, R, X> method) {
        return remember(new Func1MethodCapture<>(method, times, currentBuilder()));
    }

}
