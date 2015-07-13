package org.jmock;

import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.jmock.function.Expec8ions;
import org.jmock.function.Proc1;
import org.jmock.function.internal.BaseMethodCapture;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.internal.ExpectationCollector;
import org.jmock.internal.InvocationExpectationBuilder;

public class Expec8ionsMockery extends JUnitRuleMockery {

    private MyExpec8ions currentExpectations = null;

    public void checking(Proc1<Expec8ions, RuntimeException> expect) {
        try {
            currentExpectations = new MyExpec8ions();
            expect.call(currentExpectations);
            checking(currentExpectations);
        } finally {
            currentExpectations = null;
        }
    }

    @Override
    protected Object dispatch(Invocation invocation) throws Throwable {
        if (currentExpectations == null)
            return super.dispatch(invocation);
        else {
            currentExpectations.capture(invocation);
            return null;
        }
    }

    private class MyExpec8ions extends Expec8ions {

        private InvocationExpectationBuilder currentBuilder;

        public void capture(Invocation invocation) {
            currentBuilder.of(invocation.getInvokedObject());
            currentBuilder.createExpectationFrom(invocation);
        }

        @Override
        public void buildExpectations(Action defaultAction, ExpectationCollector collector) {
            for (BaseMethodCapture<?> capture : captures()) {
                currentBuilder = new InvocationExpectationBuilder();
                collector.add(capture.toExpectation(currentBuilder, defaultAction));
                    // this causes an invocation on the mock, which is sent to us by the mockery and captured
                    // put into the currentBuilder
            }
        }

    }
}
