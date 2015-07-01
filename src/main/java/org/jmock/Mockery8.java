package org.jmock;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matchers;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.jmock.function.FallibleSupplier;
import org.jmock.function.Function1;
import org.jmock.function.Function1Action;
import org.jmock.function.SupplierAction;
import org.jmock.internal.*;
import org.jmock.internal.matcher.AllParametersMatcher;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class Mockery8 extends Mockery {
    private List<CaptureControl> mocks = new ArrayList<>();

    @Override
    public <T> T mock(Class<T> typeToMock) {
        T result = super.mock(typeToMock);
        mocks.add((CaptureControl) result);
        return result;
    }

    public <P1, R, X extends Throwable> MethodCapture1<P1, R, X> allowing(Function1<P1, R, X> method) {
        return given(Cardinality.ALLOWING, method);
    }

    public <P1, R, X extends Throwable> MethodCapture1<P1, R, X> given(Cardinality times, Function1<P1, R, X> method) {
        return new MethodCapture1<>(method, times);
    }

    public class MethodCapture1<P1, R, X extends Throwable> {
        private final Function1<P1, R, X> function;
        private final InvocationExpectation expectation = new InvocationExpectation();

        public MethodCapture1(Function1<P1, R, X> function, Cardinality cardinality) {
            this.function = function;
            expectation.setCardinality(cardinality);
            expectation.setMethodMatcher(equalTo(methodFor(function)));
            expectation.setMethodMatcher(anyMethod());
            Mockery8.this.addExpectation(expectation);
        }

        private Method methodFor(Function1<P1, R, X> function) {
            checking(new Expectations() {
                {
                    mocks.forEach(c -> {
                        InvocationExpectationBuilder thing = new InvocationExpectationBuilder();
                        thing.of(c);
                        try {
                            function.apply(null);
                        } catch (Throwable ignored) {
                            System.out.println("DMCG: " + ignored);
                        }
                    });
                }});
            return null;
        }

        public Will with(P1 p1) {
            return with(new AllParametersMatcher(new Object[] {p1}));
        }

        public Will with(ParametersMatcher parametersMatcher) {
            expectation.setParametersMatcher(parametersMatcher);
            return new Will();
        }


        public class Will {
            public void will(FallibleSupplier<R, X> supplier) {
                will(new SupplierAction<>(supplier));
            }

            public void will(Action action) {
                expectation.setAction(action);
            }

            public void will(Function1<P1, R, X> f) {
                will(new Function1Action<>(f));
            }
        }
    }

    private static BaseMatcher<Method> anyMethod() {
        return new BaseMatcher<Method>() {
            @Override
            public void describeTo(Description description) {

            }

            @Override
            public boolean matches(Object o) {
                return true;
            }
        };
    }

}
