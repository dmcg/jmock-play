package org.jmock.function;

import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.api.Action;
import org.jmock.internal.*;
import org.jmock.internal.matcher.AllParametersMatcher;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class Expec8ions extends Expectations {

    private InvocationExpectationBuilder myCopyOfCurrentBuilder = null;

    public <T> T callTo(T mock) {
        myCopyOfCurrentBuilder = (InvocationExpectationBuilder) exactly(1); // cardinality will be changed later, but this gives us access to currentBuilder
        return myCopyOfCurrentBuilder.of(mock);
    }

    public <P1, X extends Throwable> Proc1Method<P1, X> allowing(Proc1<P1, X> method) {
        return given(Cardinality.ALLOWING, method);
    }

    public <P1, X extends Throwable> Proc1Method<P1, X> given(Cardinality times, Proc1<P1, X> method) {
        return new Proc1Method<>(method, times);
    }


    public <P1, R, X extends Throwable> Function1Method<P1, R, X> allowing(Function1<P1, R, X> method) {
        return given(Cardinality.ALLOWING, method);
    }

    public <P1, R, X extends Throwable> Function1Method<P1, R, X> given(Cardinality times, Function1<P1, R, X> method) {
        return new Function1Method<>(method, times);
    }

    public class Proc1Method<P1, X extends Throwable> {
        public Proc1Method(Proc1<P1, X> function, Cardinality cardinality) {
            myCopyOfCurrentBuilder.setCardinality(cardinality);
            try {
                function.apply(null); // captured by currentBuilder
            } catch (Throwable ignored) {
            }
        }

        public Will with(P1 p1) {
            return withMatching(new AllParametersMatcher(new Object[]{p1}));
        }

        public Will withMatching(Matcher<P1> p1) {
            return withMatching(new AllParametersMatcher(Arrays.asList(p1)));
        }

        public Will withMatching(ParametersMatcher parametersMatcher) {
            myCopyOfCurrentBuilder.addParameterMatcher(parametersMatcher);
            return new Will();
        }

        public class Will {
            public void will(FallibleCallable<X> callable) {
                will(new CallableAction<>(callable));
            }

            public void will(Action action) {
                myCopyOfCurrentBuilder.setAction(action);
            }

            public void will(Proc1<P1, X> f) {
                will(new Proc1Action<>(f));
            }
        }
    }

    public class Function1Method<P1, R, X extends Throwable> {
        public Function1Method(Function1<P1, R, X> function, Cardinality cardinality) {
            myCopyOfCurrentBuilder.setCardinality(cardinality);
            try {
                function.apply(null); // captured by currentBuilder
            } catch (Throwable ignored) {
            }
        }

        public Will with(P1 p1) {
            return withMatching(new AllParametersMatcher(new Object[]{p1}));
        }

        public Will withMatching(Matcher<P1> p1) {
            return withMatching(new AllParametersMatcher(Arrays.asList(p1)));
        }

        public Will withMatching(ParametersMatcher parametersMatcher) {
            myCopyOfCurrentBuilder.addParameterMatcher(parametersMatcher);
            return new Will();
        }

        public class Will {
            public void will(FallibleSupplier<R, X> supplier) {
                will(new SupplierAction<>(supplier));
            }

            public void will(Action action) {
                myCopyOfCurrentBuilder.setAction(action);
            }

            public void will(Function1<P1, R, X> f) {
                will(new Function1Action<>(f));
            }
        }
    }

    /* HERE BE DRAGONS */

    public void buildExpectations(Action defaultAction, ExpectationCollector collector) {
        for (InvocationExpectationBuilder builder : builders()) {
            List<Matcher<?>> capturedParameterMatchers = capturedParameterMatchersFrom(builder);
            if (!capturedParameterMatchers.isEmpty()) {
                ((InvocationExpectation) builder.toExpectation(defaultAction)).setParametersMatcher((ParametersMatcher) capturedParameterMatchers.get(0));
            }
        }
        super.buildExpectations(defaultAction, collector);
    }

    @SuppressWarnings("unchecked")
    private List<Matcher<?>> capturedParameterMatchersFrom(InvocationExpectationBuilder builder) {
        return Expec8ions.valueOfField(builder, "capturedParameterMatchers", List.class);
    }

    @SuppressWarnings("unchecked")
    private List<InvocationExpectationBuilder> builders() {
        return Expec8ions.valueOfField(this, "builders", List.class);
    }

    private static <T> T valueOfField(Object o, String name, Class<T> fieldType) {
        return valueOfField(o.getClass(), o, name, fieldType);
    }

    @SuppressWarnings("unchecked")
    private static <T> T valueOfField(Class<?> type, Object o, String name, Class<T> fieldType) {
        try {
            Field declaredField = type.getDeclaredField(name);
            declaredField.setAccessible(true);
            return (T) declaredField.get(o);
        } catch (NoSuchFieldException e) {
            Class<?> superclass = type.getSuperclass();
            if (superclass == null)
                throw new RuntimeException("Can't find field named " + name);
            return valueOfField(superclass, o, name, fieldType);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
