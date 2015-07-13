package org.jmock;

import org.jmock.api.Invocation;
import org.jmock.function.Expec8ions;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.internal.ReturnDefaultValueAction;

public class Expec8ionsMockery extends JUnitRuleMockery {

    private Expec8ions currentExpectations = null;

    public void checking(Expec8ions expectations) {
        try {
            currentExpectations = expectations;
            super.checking(currentExpectations);
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
            return new ReturnDefaultValueAction(imposteriser).invoke(invocation);
        }
    }

}
