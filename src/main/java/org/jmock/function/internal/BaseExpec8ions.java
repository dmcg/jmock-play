package org.jmock.function.internal;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsAnything;
import org.jmock.api.Action;
import org.jmock.internal.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BaseExpec8ions implements ExpectationBuilder {

    private final List<InvocationExpectationBuilder> builders = new ArrayList<>();
    private InvocationExpectationBuilder currentBuilder;

    public <T> T callTo(T mock) {
        initialiseExpectationCapture(Cardinality.exactly(1));
        return currentBuilder.of(mock);
        // Cardinality will be changed later, but this initialises currentBuilder and wraps mock in a capturing thing
        // The currentBuilder captures the method called using the returned mock when the appropriate MethodCapture invokes
        // it with dummy args
    }

    private void initialiseExpectationCapture(Cardinality cardinality) {
        currentBuilder = new InvocationExpectationBuilder();
        currentBuilder.setCardinality(cardinality);
        builders.add(currentBuilder);
    }

    public InvocationExpectationBuilder currentBuilder() {
        return currentBuilder;
    }

    public ParametersMatcher anyParameters() {
        return new AnyParametersMatcher();
    }

    public void buildExpectations(Action defaultAction, ExpectationCollector collector) {
        for (InvocationExpectationBuilder builder : builders) {
            List<Matcher<?>> capturedParameterMatchers = capturedParameterMatchersFrom(builder);
            if (!capturedParameterMatchers.isEmpty()) {
                ((InvocationExpectation) builder.toExpectation(defaultAction)).setParametersMatcher((ParametersMatcher) capturedParameterMatchers.get(0));
            }
            collector.add(builder.toExpectation(defaultAction));
        }
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
}
