package org.jmock.function.internal;

import org.hamcrest.core.IsAnything;
import org.jmock.api.Action;
import org.jmock.internal.*;

import java.util.ArrayList;
import java.util.List;

public class BaseExpec8ions implements ExpectationBuilder {

    private final List<BaseMethodCapture<?>> captures = new ArrayList<>();
    private InvocationExpectationBuilder currentBuilder;

    public <T> T callTo(T mock) {
        initialiseExpectationCapture(Cardinality.exactly(1));
        return currentBuilder.of(mock);
        // Cardinality will be changed later, but this initialises currentBuilder and wraps mock in a capturing thing
        // The currentBuilder captures the method called using the returned mock when the appropriate MethodCapture invokes
        // it with dummy args
    }

    protected <T extends BaseMethodCapture<?>> T remember(T capture) {
        captures.add(capture);
        return capture;
    }

    private void initialiseExpectationCapture(Cardinality cardinality) {
        currentBuilder = new InvocationExpectationBuilder();
        currentBuilder.setCardinality(cardinality);
    }

    public InvocationExpectationBuilder currentBuilder() {
        return currentBuilder;
    }

    public static ParametersMatcher anyParameters() {
        return new AnyParametersMatcher();
    }

    public void buildExpectations(Action defaultAction, ExpectationCollector collector) {
        for (BaseMethodCapture<?> capture : captures) {
            collector.add(capture.toExpectation(defaultAction));
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
}
