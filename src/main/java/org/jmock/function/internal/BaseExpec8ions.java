package org.jmock.function.internal;

import org.hamcrest.core.IsAnything;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
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

    public List<BaseMethodCapture<?>> captures() {
        return captures;
    }

    public static ParametersMatcher anyParameters() {
        return new AnyParametersMatcher();
    }

    @Override
    public void buildExpectations(Action defaultAction, ExpectationCollector collector) {
        for (BaseMethodCapture<?> capture : captures()) {
            if (capture.hasBuilder()) {
                // the case where callTo created the builder
                collector.add(capture.toExpectation(defaultAction));
            } else {
                // no callTo - mockery will call back to us
                currentBuilder = new InvocationExpectationBuilder();
                collector.add(capture.toExpectation(currentBuilder, defaultAction));
                // this causes an invocation on the mock, which is sent to us by the mockery and captured
                // put into the currentBuilder
            }
        }
    }

    public void capture(Invocation invocation) {
        currentBuilder.of(invocation.getInvokedObject());
        currentBuilder.createExpectationFrom(invocation);
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
