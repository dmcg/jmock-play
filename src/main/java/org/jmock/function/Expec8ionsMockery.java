package org.jmock.function;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.internal.ReturnDefaultValueAction;

public class Expec8ionsMockery extends JUnitRuleMockery {

    public void checking(Expec8ions expectations) {
        try {
            ReturnDefaultValueAction returnDefaultValueAction = new ReturnDefaultValueAction(imposteriser());
            setInterceptor((invocation, next) -> {
                expectations.capture(invocation);
                return returnDefaultValueAction.invoke(invocation);
            });
            super.checking(expectations);
        } finally {
            setInterceptor(null);
        }
    }
}
