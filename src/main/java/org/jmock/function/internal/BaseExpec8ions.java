package org.jmock.function.internal;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsAnything;
import org.jmock.Expectations;
import org.jmock.api.Action;
import org.jmock.internal.ExpectationCollector;
import org.jmock.internal.InvocationExpectation;
import org.jmock.internal.InvocationExpectationBuilder;
import org.jmock.internal.ParametersMatcher;

import java.lang.reflect.Field;
import java.util.List;

public class BaseExpec8ions extends Expectations {

    public <T> T callTo(T mock) {
        // the currentBuilder captures the method called using the returned mock when the appropriate MethodCapture invokes
        // it with dummy args
        exactly(1); // cardinality will be changed later, but this gives us access to currentBuilder
        return currentBuilder().of(mock);
    }


    public ParametersMatcher anyParameters() {
        return new AnyParametersMatcher();
    }

    public void buildExpectations(Action defaultAction, ExpectationCollector collector) {
        for (InvocationExpectationBuilder builder : builders()) {
            List<Matcher<?>> capturedParameterMatchers = capturedParameterMatchersFrom(builder);
            if (!capturedParameterMatchers.isEmpty()) {
                ((InvocationExpectation) builder.toExpectation(defaultAction)).setParametersMatcher((ParametersMatcher) capturedParameterMatchers.get(0));
            }
        }
        super.buildExpectations(defaultAction, collector);
    }


    private static class AnyParametersMatcher extends IsAnything<Object[]> implements ParametersMatcher {
        public AnyParametersMatcher() {
            super("(<any parameters>)");
        }

        public boolean isCompatibleWith(Object[] parameters) {
            return true;
        }
    }

    /* HERE BE DRAGONS */

    private static <T> T valueOfField(Object o, String name, Class<T> fieldType) {
        return valueOfField(o.getClass(), o, name, fieldType);
    }

    @SuppressWarnings({"unchecked", "UnusedParameters"})
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

    @SuppressWarnings("unchecked")
    private List<Matcher<?>> capturedParameterMatchersFrom(InvocationExpectationBuilder builder) {
        return BaseExpec8ions.valueOfField(builder, "capturedParameterMatchers", List.class);
    }

    @SuppressWarnings("unchecked")
    private List<InvocationExpectationBuilder> builders() {
        return BaseExpec8ions.valueOfField(this, "builders", List.class);
    }
}
