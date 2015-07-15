package org.jmock.function;

import org.jmock.integration.junit4.JUnitRuleMockery;

public class JunitRuleExpec8ionsMockery extends JUnitRuleMockery {

    public void checking(Expec8ions expectations) {
        expectations.buildExpectations(this);
    }

    public void checking(Proc1<Expec8ions, RuntimeException> consumer) {
        checking(Expec8ions.of(consumer));
    }
}
