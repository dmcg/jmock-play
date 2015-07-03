package org.jmock.function;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Action;
import org.jmock.api.Expectation;
import org.jmock.internal.*;
import org.jmock.internal.matcher.AllParametersMatcher;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Expec8ions extends Expectations {

    private InvocationExpectationBuilder currentBuilder = null;

    public <T> T calling(T mock) {
        currentBuilder = (InvocationExpectationBuilder) exactly(1);
        return currentBuilder.of(mock);
    }

    public void buildExpectations(Action defaultAction, ExpectationCollector collector) {
        for (InvocationExpectationBuilder builder : Expec8ions.<List<InvocationExpectationBuilder>>valueOfField(super.getClass().getSuperclass().getSuperclass(), this, "builders")) {
            List<Matcher<?>> capturedParameterMatchers = Expec8ions.<List<Matcher<?>>>valueOfField(builder.getClass(), builder, "capturedParameterMatchers");//
            ((InvocationExpectation) builder.toExpectation(defaultAction)).setParametersMatcher((ParametersMatcher) capturedParameterMatchers.get(0));
        }
        super.buildExpectations(defaultAction, collector);
    }

    public <P1, R, X extends Throwable> MethodCapture1<P1, R, X> allowing(Function1<P1, R, X> method) {
        return given(Cardinality.ALLOWING, method);
    }

    public <P1, R, X extends Throwable> MethodCapture1<P1, R, X> given(Cardinality times, Function1<P1, R, X> method) {
        return new MethodCapture1<>(method, times);
    }

    public class MethodCapture1<P1, R, X extends Throwable> {
        public MethodCapture1(Function1<P1, R, X> function, Cardinality cardinality) {
            currentBuilder.setCardinality(cardinality);
            try {
                function.apply(null); // captured by currentBuilder
            } catch (Throwable ignored) {
                System.out.println("DMCG: " + ignored);
            }
        }

        public Will with(P1 p1) {
            return with(new AllParametersMatcher(new Object[] {p1}));
        }

        public Will with(ParametersMatcher parametersMatcher) {
            currentBuilder.addParameterMatcher(parametersMatcher);
            return new Will();
        }


        public class Will {
            public void will(FallibleSupplier<R, X> supplier) {
                will(new SupplierAction<>(supplier));
            }

            public void will(Action action) {
                currentBuilder.setAction(action);
            }

            public void will(Function1<P1, R, X> f) {
                will(new Function1Action<>(f));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T valueOfField(Class<?> type, Object o, String name) {
        try {
            Field declaredField = type.getDeclaredField(name);
            declaredField.setAccessible(true);
            return (T) declaredField.get(o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
